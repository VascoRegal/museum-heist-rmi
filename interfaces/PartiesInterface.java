package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PartiesInterface extends Remote {

    /**
     * Send assault party operation
     * 
     * Called by the MT to send a party to a room
     * @param partyId
     * @param room
     * @throws RemoteException
     */
    public void sendAssaultParty(int partyId, int room) throws RemoteException;

    /**
     * Prepare excursion operation
     * 
     * Called by OTs to notify they are ready to start crwling
     * @param thiefId 
     * @param partyId
     * @return id of the room assigned
     * @throws RemoteException
     */
    public int prepareExcursion(int thiefId, int partyId) throws RemoteException;

    /**
     * Crawl in operation
     * 
     * Called by OTs to start the crawling in movement
     * @param thiefId
     * @param roomLocation
     * @param md
     * @throws RemoteException
     */
    public void crawlIn(int thiefId, int roomLocation, int md) throws RemoteException;

    /**
     * Crawl Out operation
     * 
     * Called by Ots to start crawling out, wether or not with a canvas
     * @param thiefId
     * @param md
     * @param cv
     * @throws RemoteException
     */
    public void crawlOut(int thiefId, int md, boolean cv) throws RemoteException;

    /**
     * Shutdown server operations
     * @throws RemoteException
     */
    public void shutdown() throws RemoteException;
    
}
