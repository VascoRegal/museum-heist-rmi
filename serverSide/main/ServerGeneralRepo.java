package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

import consts.HeistConstants;
import consts.Resolver;
import serverSide.objects.*;
import interfaces.*;

/**
 *    Instantiation and registering of a general repository object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ServerGeneralRepo
{
  /**
   *  Flag signaling the end of operations.
   */

   private static boolean end = false;

  /**
   *  Main method.
   *
   *        args[0] - port number for listening to service requests
   *        args[1] - name of the platform where is located the RMI registering service
   *        args[2] - port nunber where the registering service is listening to service requests
   */

   public static void main(String[] args)
   {

     /* instantiate a general repository object */

      GeneralRepo repo = new GeneralRepo ();                      // general repository object
      GeneralRepoInterface reposStub = null;                        // remote reference to the general repository object

      try
      { reposStub = (GeneralRepoInterface) UnicastRemoteObject.exportObject (repo, Resolver.GeneralPort);
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
      { registry.bind (Resolver.RMIGeneralName, reposStub);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
      catch (AlreadyBoundException e)
      { 
        System.out.println("Already bound, continuing...");
        try {
          registry.rebind(Resolver.RMIGeneralName, reposStub);
        } catch (AccessException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        } catch (RemoteException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }

     /* wait for the end of operations */
      try
      { while (!end)
          synchronized (Class.forName ("serverSide.main.ServerGeneralRepo"))
          { try
            { (Class.forName ("serverSide.main.ServerGeneralRepo")).wait ();
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
      { registry.unbind (Resolver.GeneralHost);
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
      { synchronized (Class.forName ("serverSide.main.ServerGeneralRepo"))
        { (Class.forName ("serverSide.main.ServerGeneralRepo")).notify ();
        }
      }
     catch (ClassNotFoundException e)
     {
       e.printStackTrace ();
       System.exit (1);
     }
   }
}
