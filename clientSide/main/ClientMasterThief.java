package clientSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import clientSide.entities.*;
import consts.Resolver;
import serverSide.main.*;
import interfaces.*;

/**
 *    Client side of the Sleeping Barbers (barbers).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ClientMasterThief
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - name of the platform where is located the RMI registering service
   *        args[1] - port number where the registering service is listening to service requests
   */

   public static void main (String [] args)
   {

     /* problem initialization */

      GeneralRepoInterface reposStub = null;                        // remote reference to the general repository object
      CollectionSiteInterface cStub = null;
      PartiesInterface pStub = null;
      Registry registry = null;                                      // remote reference for registration in the RMI registry service

      try
      { registry = LocateRegistry.getRegistry (Resolver.RMIHostName, Resolver.RMIPort);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { reposStub = (GeneralRepoInterface) registry.lookup (Resolver.RMIGeneralName);
        cStub = (CollectionSiteInterface) registry.lookup(Resolver.RMIColSiteName);
        pStub = (PartiesInterface) registry.lookup(Resolver.RMIPartiesName);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NotBoundException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }


      MasterThief mt = new MasterThief(0, reposStub, cStub, pStub, null);
      mt.start();
      try {
        mt.join();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
   }
}