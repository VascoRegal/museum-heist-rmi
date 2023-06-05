package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

import consts.Resolver;
import serverSide.objects.*;
import interfaces.*;

/**
 *   Instantiation and registering of an object that enables the registration of other objects located
 *   in the same or other processing nodes of a parallel machine in the local RMI registry service.
 *
 *     Communication is based on Java RMI.
 */

public class ServerRegister
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - port number for listening to service requests
   *        args[1] - name of the platform where is located the RMI registering service
   *        args[2] - port nunber where the registering service is listening to service requests
   */

   public static void main(String[] args)
   {

     /* instantiate a registration remote object and generate a stub for it */

    if (System.getSecurityManager () == null)
        System.setSecurityManager (new SecurityManager ());
      RegisterRemoteObject regEngine = new RegisterRemoteObject (Resolver.RMIHostName, Resolver.RMIPort);  // object that enables the registration
                                                                                                   // of other remote objects
      Register regEngineStub = null;                                                               // remote reference to it

      try
      { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, Resolver.RegisterPort);
      }
      catch (RemoteException e)
      { 
        System.exit (1);
      }

     /* register it with the local registry service */

      String nameEntry = Resolver.RMIRegisterName;                          // public name of the remote object that enables
                                                                     // the registration of other remote objects
      Registry registry = null;                                      // remote reference for registration in the RMI registry service

      try
      { registry = LocateRegistry.getRegistry (Resolver.RMIHostName, Resolver.RMIPort);
      }
      catch (RemoteException e)
      { 
        System.exit (1);
      }

      try
      { registry.rebind (nameEntry, regEngineStub);
      }
      catch (RemoteException e)
      { 
        System.exit (1);
      }
   }
}
