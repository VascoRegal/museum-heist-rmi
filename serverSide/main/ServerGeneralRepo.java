package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

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
   */

   public static void main(String[] args)
   {

     /* instantiate a general repository object */


      if (System.getSecurityManager () == null)
        System.setSecurityManager (new SecurityManager ());
      GeneralRepo repo = new GeneralRepo ();                      // general repository object
      GeneralRepoInterface reposStub = null;                        // remote reference to the general repository object
      Register reg = null;

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

      try {
        reg = (Register) registry.lookup(Resolver.RMIRegisterName);
      } catch (RemoteException | NotBoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      try
      { reg.bind (Resolver.RMIGeneralName, reposStub);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
      catch (AlreadyBoundException e)
      { 
        try {
          reg.rebind(Resolver.RMIGeneralName, reposStub);
        } catch (AccessException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        } catch (RemoteException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }

      System.out.println("[GENERAL] Bound object to " + Resolver.RMIGeneralName);

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
      { reg.unbind (Resolver.RMIGeneralName);
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
