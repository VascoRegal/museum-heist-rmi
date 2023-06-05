package serverSide.objects;

import clientSide.entities.ThiefState;
import consts.HeistConstants;
import interfaces.CollectionSiteInterface;
import structs.MemQueue;

/**
 * Collection Site shared memory
 */
public class CollectionSite implements CollectionSiteInterface {
    
    /**
     * num of thieves currently available
     */
    private int numAvailableThieves;

    /**
     * heist status
     */
    private boolean heistInProgress;

    /**
     * num running parties
     */
    private int activeParties;

    /**
     * num cleared rooms
     */
    private int clearedRooms;

    /**
     * num rooms available
     */
    private int availableRooms;

    /**
     * master thief request proxy
     */
    private Thread mt;

    /**
     * mt state
     */
    private int mtState;

    /**
     * ordinary thief request proxy
     */
    private Thread[] ots;

    /**
     * orindary thief stats
     */
    private int[] otStates;

    /**
     * ordinary thief canvas tracking
     */
    private boolean[] otcanvas;

    /**
     * party active flags
     */
    private int[] partyActivity;

    /**
     * ordiary thief party assignments
     */
    private int[] partyAssignments;

    /**
     * status of canvas collection for each party
     */
    private int[] partyCollections;

    /**
     * status of room clearing for each party
     */
    private int[] partyClears;

    /**
     * Queue of available thieves wanting to joing parties
     */
    private MemQueue<Integer> availableThieves;

    /**
     * Queue of thieves wanting to hand canvas
     */
    private MemQueue<Integer> collectingThieves;

    /**
     * total num of paingings
     */
    private int paintings;

    /**
     * constructor
     */
    public CollectionSite()
    {
        int i;

        heistInProgress = false;
        numAvailableThieves = 0;
        activeParties = 0;
        clearedRooms = 0;
        paintings = 0;
        availableRooms = HeistConstants.NUM_ROOMS;

        ots = new Thread[HeistConstants.NUM_THIEVES];
        otStates = new int[HeistConstants.NUM_THIEVES];
        otcanvas = new boolean[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] = null;
            otStates[i] = -1;
            otcanvas[i] = false;
        }
        partyAssignments = new int[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            partyAssignments[i] = -1;
        }

        partyCollections = new int[HeistConstants.MAX_NUM_PARTIES];
        partyClears = new int[HeistConstants.MAX_NUM_PARTIES];
        partyActivity = new int[HeistConstants.MAX_NUM_PARTIES];
        for (i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            partyCollections[i] = 0;
            partyClears[i] = 0;
            partyActivity[i] = 0;
        }

        availableThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
        collectingThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
    }

    /**
     * 
     * @return heist flag
     */
    public synchronized boolean getHeistStatus()
    {
        return this.heistInProgress;
    }

    public synchronized void startOperations()
    {
        mt = (Thread.currentThread());
        mtState = (ThiefState.PLANNING_THE_HEIST);
        this.heistInProgress = true;
        System.out.println("[COLLECTION_SITE] MasterThief starting operations");
    }

    /**
     * enqueue thief in queue and wait until notified
     * @return partyId
     */
    public synchronized int amINeeded(int ordinaryThiefId)
    {

        ots[ordinaryThiefId] = Thread.currentThread();
        otStates[ordinaryThiefId] = (ThiefState.CONCENTRATION_SITE);
        availableThieves.enqueue(ordinaryThiefId);
        this.numAvailableThieves++;


        notifyAll();
        System.out.println("[COLLECTION_SITE] OrdinaryThief_" + ordinaryThiefId+  " waiting to joing party");
        while (otStates[ordinaryThiefId] == ThiefState.CONCENTRATION_SITE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("[COLLECTION_SITE] OrdinaryThief_" + ordinaryThiefId+  " called for Party_" + partyAssignments[ordinaryThiefId]);
        return partyAssignments[ordinaryThiefId];
    }

    /**
     * Decide what operation to do next based on resources
     * @return operation
     */
    public synchronized char appraiseSit()
    {
        mt = Thread.currentThread();
        mtState = ThiefState.DECIDING_WHAT_TO_DO;

        System.out.println("[COLLECTION_SITE] MasterThief deciding what to do with " + numAvailableThieves + " Thieves, " + activeParties + " Active Parties and " +  availableRooms + " Free Rooms");


        if (clearedRooms == HeistConstants.NUM_ROOMS && activeParties == 0)
        {
            System.out.println("[COLLECTION_SITE] MasterThief - end the heist");

            mtState = (ThiefState.PRESENTING_THE_REPORT);
            heistInProgress = false;
            return 's';
        }
        if (activeParties == HeistConstants.MAX_NUM_PARTIES ||
            (availableRooms <= 0 && activeParties == 1))
        {
            mtState = (ThiefState.WAITING_FOR_GROUP_ARRIVAL);
            System.out.println("[COLLECTION_SITE] MasterThief - Wait for group arrival");
            return 'r';
        } else {
            System.out.println("[COLLECTION_SITE] MasterThief - Wait for thieves to form a party");
            mtState = (ThiefState.ASSEMBLING_A_GROUP);
            while (numAvailableThieves < HeistConstants.PARTY_SIZE)
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                System.out.println("[COLLECTION_SITE] MasterThief - Thief joined, thieves available : " + numAvailableThieves);
            }
            return 'p';
        }
    }

    /**
     * create party structures
     * @return identification of party
     */
    public synchronized int prepareAssaultParty()
    {
        System.out.println("[COLLECTION_SITE] MasterThief - Prepare an assault party");
        mt = Thread.currentThread();
        int availableThiefId, partyId;
        if (activeParties == HeistConstants.MAX_NUM_PARTIES) {
            return -1;
        }

        partyId = getAvailablePartySlot();
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            availableThiefId = availableThieves.dequeue();
            otStates[availableThiefId] = ThiefState.CRAWLING_INWARDS;
            partyAssignments[availableThiefId] = partyId;
            numAvailableThieves--;
            notifyAll();
        }
        System.out.println("[COLLECTION_SITE] MasterThief - Created Party_" + partyId);
        partyActivity[partyId] = 1;
        activeParties++;
        availableRooms--;
        return partyId;
    }

    /**
     * wait for canvas handing
     */
    public synchronized void takeARest()
    {
        mt = Thread.currentThread();
        mtState = ThiefState.WAITING_FOR_GROUP_ARRIVAL;
        System.out.println("[COLLECTION_SITE] MasterThief - Wait for thieves to deliver canvas");
        while (collectingThieves.empty())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("[COLLECTION_SITE] MasterThief - Thief arrived, collecting canvas");
    }

    /**
     * enqueue ordinary thief in canvas collection
     */
    public synchronized void handCanvas(int ordinaryThiefId, boolean cv)
    {
        ots[ordinaryThiefId] = Thread.currentThread();
        otStates[ordinaryThiefId] = (ThiefState.COLLECTION_SITE);
        otcanvas[ordinaryThiefId] = cv;
        collectingThieves.enqueue(ordinaryThiefId);
        notifyAll();

        System.out.println("[COLLECTION_SITE] OrdinaryThief_" + ordinaryThiefId+  " waiting for MasterThief to collect my canvas");


        while (otStates[ordinaryThiefId] == ThiefState.COLLECTION_SITE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("[COLLECTION_SITE] OrdinaryThief_" + ordinaryThiefId+  " canvas collected");

    }

    /**
     * collect a cnavas from a thief in queue
     */
    public synchronized void collectCanvas()
    {
        

        int party, thiefId;

        thiefId = collectingThieves.dequeue();
        party = partyAssignments[thiefId];

        System.out.println("[COLLECTION_SITE] MasterThief - Collecting OrdinaryThief_" + thiefId + " canvas");

        if (otcanvas[thiefId])
        {
            paintings++;
            System.out.println(paintings);
        } else {
            if (partyClears[party] == 0)
            {
                System.out.println("Room cleared");
                partyClears[party] = 1;
                clearedRooms++;
            }
        }


        partyCollections[party]++;

        
        if (partyCollections[party] == HeistConstants.PARTY_SIZE)
        {
            System.out.println("Finished party " + party);
            if (partyClears[party] == 0)
            {
                availableRooms++;
            }
            partyClears[party] = 0;
            partyAssignments[thiefId] = -1;
            partyCollections[party] = 0;
            partyActivity[party] = 0;
            activeParties--;
        }

        otStates[thiefId] = (ThiefState.CONCENTRATION_SITE);
        otcanvas[thiefId] = false;
        notifyAll();
        
    }

    /**
     * get free party slot
     * @return partyId
     */
    private int getAvailablePartySlot()
    {
        for (int i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            if (partyActivity[i] == 0)
            {
                return i;
            }
        }

        return -1;
    }
}
