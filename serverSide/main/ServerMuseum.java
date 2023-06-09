package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

import consts.Resolver;
import serverSide.objects.*;
import interfaces.*;

/**
 *    Instantiation and registering of a museum object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ServerMuseum
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

      Museum repo = new Museum();                      // general repository object
      MuseumInterface mStub = null;                        // remote reference to the general repository object
      Register reg = null;

      try
      { mStub = (MuseumInterface) UnicastRemoteObject.exportObject (repo, Resolver.MuseumPort);
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
      { reg.bind (Resolver.RMIMuseumName, mStub);
      }
      catch (RemoteException e)
      { 
        e.printStackTrace ();
        System.exit (1);
      }
      catch (AlreadyBoundException e)
      { 
        try {
          reg.rebind(Resolver.RMIMuseumName, mStub);
        } catch (AccessException e1) {
          e1.printStackTrace();
        } catch (RemoteException e1) {
          e1.printStackTrace();
        }
      }

      System.out.println("[MUSEUM] Bound object to " + Resolver.RMIMuseumName);

     /* wait for the end of operations */
      try
      { while (!end)
          synchronized (Class.forName ("serverSide.main.ServerMuseum"))
          { try
            { (Class.forName ("serverSide.main.ServerMuseum")).wait ();
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
      { reg.unbind (Resolver.RMIMuseumName);
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
      { synchronized (Class.forName ("serverSide.main.ServerMuseum"))
        { (Class.forName ("serverSide.main.ServerMuseum")).notify ();
        }
      }
     catch (ClassNotFoundException e)
     {
       e.printStackTrace ();
       System.exit (1);
     }
   }
}
