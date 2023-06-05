package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GeneralRepoInterface extends Remote {
    
    public void setMasterThiefState(int state) throws RemoteException;

    public void setOrdinaryThiefState(int orindaryThiefId, int state) throws RemoteException;

    public void shutdown() throws RemoteException;
}
