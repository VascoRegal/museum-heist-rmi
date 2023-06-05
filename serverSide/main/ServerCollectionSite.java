package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

import consts.Resolver;
import serverSide.objects.*;
import interfaces.*;

/**
 *    Instantiation and registering of a collection site object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ServerCollectionSite
{
  /**
   *  Flag signaling the end of operations.
   */

   private static boolean end = false;

  /**
   *  Main method.
   *
   */

   public static void main(String[] args)
   {

     /* instantiate a general repository object */

      CollectionSite repo = new CollectionSite();                      // general repository object
      CollectionSiteInterface cStub = null;                        // remote reference to the general repository object

      try
      { cStub = (CollectionSiteInterface) UnicastRemoteObject.exportObject (repo, Resolver.CollectionSitePort);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }

     /* register it with the general registry service */

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
      { registry.bind (Resolver.RMIColSiteName, cStub);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
      catch (AlreadyBoundException e)
      { 
        try {
            registry.rebind(Resolver.RMIColSiteName, cStub);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
      }

      System.out.println("[COLLECTION_SITE] Bound object to " + Resolver.RMIColSiteName);

     /* wait for the end of operations */
      try
      { while (!end)
          synchronized (Class.forName ("serverSide.main.ServerCollectionSite"))
          { try
            { (Class.forName ("serverSide.main.ServerCollectionSite")).wait ();
            }
            catch (InterruptedException e)
            { 
            }
          }
      }
      catch (ClassNotFoundException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }

     /* server shutdown */

      boolean shutdownDone = false;                                  // flag signalling the shutdown of the general repository service

      try
      { registry.unbind (Resolver.RMIColSiteName);
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

      try
      { shutdownDone = UnicastRemoteObject.unexportObject (repo, true);
      }
      catch (NoSuchObjectException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Close of operations.
   */

   public static void shutdown ()
   {
      end = true;
      try
      { synchronized (Class.forName ("serverSide.main.ServerCollectionSite"))
        { (Class.forName ("serverSide.main.ServerCollectionSite")).notify ();
        }
      }
     catch (ClassNotFoundException e)
     {
       e.printStackTrace ();
       System.exit (1);
     }
   }
}
