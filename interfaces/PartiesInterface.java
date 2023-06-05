package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PartiesInterface extends Remote {

    public void sendAssaultParty(int partyId, int i) throws RemoteException;

    public int prepareExcursion(int thiefId, int partyId) throws RemoteException;

    public void crawlIn(int thiefId, int roomLocation, int md) throws RemoteException;

    public void crawlOut(int thiefId, int md, boolean cv) throws RemoteException;

    public void shutdown() throws RemoteException;
    
}
