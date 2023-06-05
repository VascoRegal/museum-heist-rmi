package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GeneralRepoInterface extends Remote {
    
    /**
     * Set the state of the master thief
     * @param state
     * @throws RemoteException
     */
    public void setMasterThiefState(int state) throws RemoteException;

    /**
     * Set the sttate of an ordinary thief
     * @param orindaryThiefId
     * @param state
     * @throws RemoteException
     */
    public void setOrdinaryThiefState(int orindaryThiefId, int state) throws RemoteException;

    public void shutdown() throws RemoteException;
}
