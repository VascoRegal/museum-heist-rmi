package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MuseumInterface extends Remote {
    
    /**
     * Get room location operation
     * 
     * Called by OTs to find where they need to go when crawling in
     * @param roomId
     * @return location of the room
     * @throws RemoteException
     */
    public int getRoomLocation(int roomId) throws RemoteException;

    /**
     * Pick a canvas operation
     * 
     * Called by OTs when at a room
     * @param roomId
     * @return true if canvas picked, false if room empty
     * @throws RemoteException
     */
    public boolean pickCanvas(int roomId) throws RemoteException;

    /**
     * Shutdown server operations
     * @throws RemoteException
     */
    public void shutdown() throws RemoteException;
}
