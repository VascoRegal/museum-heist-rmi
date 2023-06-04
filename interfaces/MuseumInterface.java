package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MuseumInterface extends Remote {
    
    public int getRoomLocation(int roomId) throws RemoteException;

    public boolean pickCanvas(int roomId) throws RemoteException;
}
