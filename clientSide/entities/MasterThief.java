package clientSide.entities;

import java.rmi.RemoteException;

import interfaces.CollectionSiteInterface;
import interfaces.GeneralRepoInterface;
import interfaces.MuseumInterface;
import interfaces.PartiesInterface;

/**
 * MasterThief class
 * 
 * Communicates with the servers via stubs
 */
public class MasterThief extends Thief {

    /*
     * General stub
     */
    GeneralRepoInterface generalMemory;

    /**
     * ColSite stub
     */
    CollectionSiteInterface collectionSiteMemory;

    /**
     * Parties stub
     */
    PartiesInterface partiesMemory;

    /**
     * Museum stub
     */
    MuseumInterface museumMemory;

    /**
     * 
     * @param id 
     * @param generalMemory
     * @param collectionSiteMemory
     * @param partiesMemory
     * @param museumMemory
     */
    public MasterThief(
        int id,
        GeneralRepoInterface generalMemory,
        CollectionSiteInterface collectionSiteMemory,
        PartiesInterface partiesMemory,
        MuseumInterface museumMemory) 
    {
        super(id);
        this.generalMemory = generalMemory;
        this.collectionSiteMemory = collectionSiteMemory;
        this.partiesMemory = partiesMemory;
        this.museumMemory = museumMemory;
    }

    /**
     *  Main lifecycle
     * 
     */
    public void run() {

        char action;
        int partyId;
        System.out.println(this.collectionSiteMemory);
        startOperations();
        while (isHeistInProgress())
        {
            action = appraiseSit();
            switch (action)
            {
                case 'p':
                    partyId = prepareAssaultParty();
                    sendAssaultParty(partyId);
                    break;
                case 'r':
                    takeARest();
                    collectACanvas();
                    break;
                case 's':
                    break;
            }
        }
    }

    /**
     *  start operations
     */
    private void startOperations()
    {
        this.setThiefState(ThiefState.PLANNING_THE_HEIST);
        try {
            generalMemory.setMasterThiefState(ThiefState.PLANNING_THE_HEIST);
            collectionSiteMemory.startOperations();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 
     * @return bool wether or not heist is running
     */
    private boolean isHeistInProgress()
    {
        try {
            return collectionSiteMemory.getHeistStatus();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * decide what to do next with current resources
     * @return char, operation to do next
     */
    private char appraiseSit()
    {
        char c = ' ';
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        try {
            generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
            c = collectionSiteMemory.appraiseSit();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);

        }
        return c;
    }

    /**
     * prepare an assault party
     * @return
     */
    private int prepareAssaultParty()
    {
        int partyId = -1;
        this.setThiefState(ThiefState.ASSEMBLING_A_GROUP);
        try {
            generalMemory.setMasterThiefState(ThiefState.ASSEMBLING_A_GROUP);
            partyId = collectionSiteMemory.prepareAssaultParty();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return partyId;
    }

    /**
     * send the party
     * @param partyId id of the party
     */
    private void sendAssaultParty(int partyId)
    {
        //int targetRoom = museumMemory.getAvailableRoom();
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        try {
            partiesMemory.sendAssaultParty(partyId, -1);
            generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * wait for groups to arrive
     */
    private void takeARest()
    {
        this.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
        try {
            generalMemory.setMasterThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
            collectionSiteMemory.takeARest();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * collect a canvas
     */
    private void collectACanvas()
    {
        try {
            collectionSiteMemory.collectCanvas();
            generalMemory.setMasterThiefState(ThiefState.DECIDING_WHAT_TO_DO);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        this.setThiefState(ThiefState.DECIDING_WHAT_TO_DO);
    }
}

