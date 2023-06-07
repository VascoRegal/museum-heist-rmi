package serverSide.objects;

import clientSide.entities.ThiefState;
import consts.HeistConstants;
import interfaces.PartiesInterface;
import serverSide.entities.RoomState;
import serverSide.main.ServerParties;
import structs.MemException;
import structs.MemPartyArray;

/**
 * Parties memory region
 */
public class Parties implements PartiesInterface {
    
    /**
     * ordinary thieves thread reference
     */
    private Thread[] ots;

    /**
     * 
     */
    private int[] states;

    /**
     * reference to thieves party assignment
     */
    private int partyMembers[];

    /**
     * reference to parties ready to be sent
     */
    private int readyParties[];

    /**
     * reference to memebers ready in party
     */
    private int readyMembers[];

    /**
     * array of party MemArrays for movement operations
     */
    private final MemPartyArray[] parties;

    /**
     * reference to the next thief to move
     */
    private int nextMovingThief[];

    /**
     * track of party target room
     */
    private int partyRooms[];

    /**
     * state of rooms
     */
    private RoomState rooms[];

    /**
     * constructor
     */
    public Parties()
    {
        int i;

        ots = new Thread[HeistConstants.NUM_THIEVES];
        states = new int[HeistConstants.NUM_THIEVES];
        partyMembers = new int[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] = null;
            states[i] = -1;
            partyMembers[i] = -1;
        }

        readyParties = new int[HeistConstants.MAX_NUM_PARTIES];
        parties = new MemPartyArray[HeistConstants.MAX_NUM_PARTIES];
        nextMovingThief = new int[HeistConstants.MAX_NUM_PARTIES];
        partyRooms = new int[HeistConstants.MAX_NUM_PARTIES];
        readyMembers = new int[HeistConstants.MAX_NUM_PARTIES];
        for (i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            readyParties[i] = 0;
            parties[i] = null;
            nextMovingThief[i] = -1;
            partyRooms[i] = -1;
            readyMembers[i] = 0;
        }
        rooms = new RoomState[HeistConstants.NUM_ROOMS];
        for (i = 0; i < HeistConstants.NUM_ROOMS; i++)
        {
            rooms[i] = RoomState.AVAILABLE;
        }
    }
    
    /**
     * send assault party to room
     * @param partyId
     * @param roomId
     */
    public synchronized void sendAssaultParty(int partyId, int roomId)
    {
        roomId = findNonClearedRoom();
        System.out.println("[PARTY_" + partyId + "] to Room_" + roomId);
        rooms[roomId] = RoomState.IN_PROGRESS;
        readyParties[partyId] = -1;
        partyRooms[partyId] = roomId;


        while (readyMembers[partyId] != HeistConstants.PARTY_SIZE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        readyParties[partyId] = 1;
        readyMembers[partyId] = 0;
        notifyAll();
    }

    /**
     * OT waits for party to start
     * @param partyId
     * @return
     */
    public synchronized int prepareExcursion(int ordinaryThiefId, int partyId)
    {

        ots[ordinaryThiefId] = Thread.currentThread();
        states[ordinaryThiefId] = ThiefState.CRAWLING_INWARDS;
        partyMembers[ordinaryThiefId] = partyId;
        if (parties[partyId] == null)
        {
            parties[partyId] = new MemPartyArray(new int[HeistConstants.PARTY_SIZE]);
            nextMovingThief[partyId] = ordinaryThiefId;
        }

        try {
            parties[partyId].join(ordinaryThiefId);
        } catch (MemException e) {
            e.printStackTrace();
            System.exit(1);
        }

        readyMembers[partyId]++;

        if (ordinaryThiefId == parties[partyId].tail())
        {
            notifyAll();
        }

        while (true)
        {
            try {
                System.out.println("[PARTY_" + partyId + "] OT_" + ordinaryThiefId + " preparing for excursion");
                wait();
                if (readyParties[partyId] == 1 && partyMembers[ordinaryThiefId] == partyId)
                {
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[PARTY_" + partyId + "] OT_" + ordinaryThiefId + " starting movement");
        return partyRooms[partyId];
    }

    /**
     * start crawling movement to room
     * @param roomLocation
     */
    public synchronized void crawlIn(int ordinaryThiefId, int md, int roomLocation)
    {
        int currentThief, closestThief;
        int partyId;
        currentThief = ordinaryThiefId;
        partyId = partyMembers[currentThief];

        while (true) {

            while (nextMovingThief[partyId] != currentThief)
            {
                try {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") trying to move");
            while (parties[partyId].canMove(currentThief) && parties[partyId].getPosition(currentThief) < roomLocation) {
                System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") can move");
                parties[partyId].doBestMove(currentThief, md);
            }
            System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") can no longer move");
            closestThief = parties[partyId].getNext(currentThief);
            nextMovingThief[partyId] = closestThief;
            notifyAll();

            if (parties[partyId].getPosition(currentThief) >= roomLocation) {
                states[currentThief] = ThiefState.AT_A_ROOM;
                parties[partyId].setState(currentThief, ThiefState.AT_A_ROOM);
                parties[partyId].setPosition(currentThief, roomLocation);
                System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") arrived at location");
                if (currentThief == parties[partyId].tail())
                {
                    nextMovingThief[partyId] = -1;
                }
                break;
            }
        }
    }

    /**
     * crawl back to site
     */
    public synchronized void crawlOut(int ordinaryThiefId, int md, boolean cv)
    {
        int currentThief, closestThief;
        int partyId;
        int siteLocation;

        currentThief = ordinaryThiefId;
        siteLocation = 0;

        System.out.println("OT_" + currentThief + " entered crawlo out");
        partyId = partyMembers[currentThief];
        readyMembers[partyId]++;
        parties[partyId].setState(currentThief, ThiefState.CRAWLING_OUTWARDS);
    
        if (readyMembers[partyId] == HeistConstants.PARTY_SIZE)
        {
            nextMovingThief[partyId] = parties[partyId].head();
            notifyAll();
        }

        while(readyMembers[partyId] != HeistConstants.PARTY_SIZE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        while (true) {

            while (nextMovingThief[partyId] != currentThief)
            {
                try {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            while (parties[partyId].canMove(currentThief) && parties[partyId].getPosition(currentThief) > siteLocation) {
                System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") trying to move");
                parties[partyId].doBestMove(currentThief, md);
            }
            System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") can no longer move");

            closestThief = parties[partyId].getNext(currentThief);
            nextMovingThief[partyId] = closestThief;
            notifyAll();

            if (parties[partyId].getPosition(currentThief) <= siteLocation) {
                states[currentThief] = ThiefState.COLLECTION_SITE;
                parties[partyId].setState(currentThief, ThiefState.COLLECTION_SITE);
                parties[partyId].setPosition(currentThief, siteLocation);
                
                partyMembers[currentThief] = -1;


                System.out.println(String.format("[PARTY%d] [ROOM%d] [OT%d] :  Has canvas - %s", partyId, partyRooms[partyId] ,currentThief, (cv) ? "true" : "false" ));
                if (!cv && rooms[partyRooms[partyId]] != RoomState.COMPLETED)
                {
                    rooms[partyRooms[partyId]] = RoomState.COMPLETED;
                    System.out.println("UPDATED ROOM " + partyRooms[partyId] + " TO COMPLETED");
                }

                if (currentThief == parties[partyId].tail())
                {

                    if (rooms[partyRooms[partyId]] != RoomState.COMPLETED)
                    {
                        rooms[partyRooms[partyId]] = RoomState.AVAILABLE;
                    }

                    System.out.println("[PARTY_" + partyId + "] OT_" + currentThief + " (pos=" + parties[partyId].getPosition(currentThief) + ", md=" + md + ") arrived at collection site");
                    parties[partyId] = null;
                    partyRooms[partyId] = -1;
                    nextMovingThief[partyId] = -1;
                    readyParties[partyId] = -1;
                    readyMembers[partyId] = 0;
                }

                break;
            }
        }
    }

    /**
     * find next available room
     * @return
     */
    private int findNonClearedRoom()
    {
        for (int i = 0; i < HeistConstants.NUM_ROOMS; i++)
        {
            if (rooms[i] == RoomState.AVAILABLE)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * end operations
     */
    public void shutdown()
    {
        System.out.println("Shutting down...");
        ServerParties.shutdown();
    }
}