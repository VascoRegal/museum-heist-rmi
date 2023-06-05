package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type BarberShop.
 *
 *     It provides the functionality to access the Barber Shop.
 */

public interface CollectionSiteInterface extends Remote
{

  /**
   *  Operation get the heist status.
   *
   *  It is called by a master thief to get the status of the hesit
   *
   *     @return true, if heist in progress
   *             false, otherwise
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public boolean getHeistStatus() throws RemoteException;


  /**
   *  Operation start Operations.
   *
   *  It is called by the Master Thief to start the heist.
   *
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */


   public void startOperations () throws RemoteException;

  /**
   *  Operation am I Needed.
   *
   *  It is called by the Ordinary THieves to queue up for parties
   *
   *     @return true, if needed -
   *             false, otherwise
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public int amINeeded (int ordinaryThiefId) throws RemoteException;

  /**
   *  Operation appraiseSit.
   *
   *  Tells the master thief what operation to do next.
   *
   *     @return key of the operation
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public char appraiseSit () throws RemoteException;

  /**
   *  Operation prepare assault party.
   *
   *   prepares to form a party
   *
   *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                              service fails
   */

   public int prepareAssaultParty () throws RemoteException;


   /**
    * Opearion take a rest
    *
    * Called by the MT, waits for thieves to arrive with canvas
    * @throws RemoteException
    */
  public void takeARest() throws RemoteException;

  /**
   * Operation collect canvas
   * 
   * Called by the MT, collects a canvas from a thief in queue
   * @throws RemoteException
   */
  public void collectCanvas() throws RemoteException;

  /**
   * Operation hand a canvas
   * 
   * Called by the OTs, queues up the thief to be received later by the waiting MT
   * 
   * @param thiefId id of the thief waiting
   * @param cv wether or not he has a canvas
   * @throws RemoteException
   */
  public void handCanvas(int thiefId, boolean cv) throws RemoteException;

  /**
   * shutdown server operations
   * @throws RemoteException
   */
  public void shutdown() throws RemoteException;
}
