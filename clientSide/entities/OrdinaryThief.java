package clientSide.entities;

import java.rmi.RemoteException;

import consts.HeistConstants;
import interfaces.CollectionSiteInterface;
import interfaces.GeneralRepoInterface;
import interfaces.MuseumInterface;
import interfaces.PartiesInterface;
import structs.Utils;


/**
 *  Ordinary Thief class
 * 
 *  Represent an ordinary thief, comunicates via stubs
 * 
 */
public class OrdinaryThief extends Thief
{
    /**
     *  Position of the thief 
     */
    
    private int position;
    
    /**
     *  Thief maximum displacement
     */

    private int md;

    /**
     *  Thief canvas holding status
     */

    private boolean hasCanvas;

    /**
     *  Thief party identification
     */

    private int partyId;

    /**
     * Stub reference
     */
    private GeneralRepoInterface generalMemory;

    /**
     * Stub reference
     */
    private CollectionSiteInterface collectionSiteMemory;

    /**
     * Stub reference
     */
    private PartiesInterface partiesSiteMemory;

    /**
     * Stub reference
     */
    private MuseumInterface museumMemoryStub;


    /**
     * identification and reference to stubs
     * @param id
     * @param generalMemory
     * @param collectionSiteMemory
     * @param partiesSiteMemory
     * @param museumMemoryStub
     */
    public OrdinaryThief(
        int id,
        GeneralRepoInterface generalMemory,
        CollectionSiteInterface collectionSiteMemory,
        PartiesInterface partiesSiteMemory,
        MuseumInterface museumMemoryStub
    )
    {
        super(id);
        this.position = 0;      // initial position
        this.md = Utils.randIntInRange(HeistConstants.MIN_THIEF_MD, HeistConstants.MAX_THIEF_MD);   // random number
        this.state = ThiefState.CONCENTRATION_SITE;     //initial state
        this.partyId = -1;
        this.hasCanvas = false;
        this.generalMemory = generalMemory;
        this.collectionSiteMemory = collectionSiteMemory;
        this.partiesSiteMemory = partiesSiteMemory;
        this.museumMemoryStub = museumMemoryStub;
    }

    /**
     * 
     *  Main lifecylce
     */
    public void run() {
        int roomId;
        boolean hasCanvas;

        while (amINeeded())
        {
            roomId = prepareExcursion();
            crawlIn(roomId);
            hasCanvas = pickCanvas(roomId);
            crawlOut(hasCanvas);
            handACanvas(hasCanvas);
        }

    }

    /**
     *  Set Thief party identification
     * 
     *      @param id party id
     */

    public void setPartyId(int id) {
        this.partyId = id;
    }

    /**
     *  Get Thief party identification
     * 
     *      @return party id
     */

    public int getPartyId() {
        return this.partyId;
    }

    /**
     *  Get Thief max displace
     * 
     *      @return thief md
     */

    public int getMaxDisplacement() {
        return this.md;
    }

    /**
     *  Get Thief position
     * 
     *      @return thief position
     */

    public int getPosition() {
        return this.position;
    }

    /**
     *  Set Thief postion
     * 
     *      @param thief position
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    /**
     *  Toggle the canvas flag
     */

    public void pickCanvas() {
        this.hasCanvas = true;
    }

    /**
     * Remove the canvas
     */
    public void removeCanvas() {
        this.hasCanvas = false;
    }

    /**
     *  Get canvas state
     *  
     *      @return true if thief holding canvas
     *              false if not
     */

    public boolean hasCanvas() {
        return this.hasCanvas;
    }

    /**
     *  Move thief by an increment and return
     *  new position
     *      
     *      @param increment
     *      @return final position
     */

    public int move(int increment) {
        this.position += increment;
        return this.position;
    }

    /**
     * blocks until needed for a party
     * @return true if needed
     */
    private boolean amINeeded()
    {
        int party = -1;
        this.setThiefState(ThiefState.CONCENTRATION_SITE);
        try {
            generalMemory.setOrdinaryThiefState(id, ThiefState.CONCENTRATION_SITE);
            party = collectionSiteMemory.amINeeded(id);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.setPartyId(party);
        return (party != -1);
    }

    /**
     * tell the server thief is ready
     * @return int, party identification
     */
    private int prepareExcursion()
    {
        int roomId = -1;
        this.setThiefState(ThiefState.CRAWLING_INWARDS);
        try {
            generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_INWARDS);
            roomId = partiesSiteMemory.prepareExcursion(id, this.getPartyId());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return roomId;
    }

    /**
     * crawl to a room
     * @param roomId
     */
    private void crawlIn(int roomId)
    {
        try {
            int roomLocation = museumMemoryStub.getRoomLocation(roomId);
            partiesSiteMemory.crawlIn(id, this.getMaxDisplacement(), roomLocation);
            generalMemory.setOrdinaryThiefState(this.id, ThiefState.AT_A_ROOM);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.setThiefState(ThiefState.AT_A_ROOM);
    }

    /**
     * pick a canvas from a room
     * @param roomId
     * @return true if canvas picked
     */
    private boolean pickCanvas(int roomId)
    {
        boolean pickedCanvas = false;
        try {
            pickedCanvas = museumMemoryStub.pickCanvas(roomId);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pickedCanvas;
    }

    /**
     * crawl out of roo,
     * @param withCanvas true if canvas taken
     */
    private void crawlOut(boolean withCanvas)
    {
        try {
            generalMemory.setOrdinaryThiefState(id, ThiefState.CRAWLING_OUTWARDS);
            this.setThiefState(ThiefState.CRAWLING_OUTWARDS);
            partiesSiteMemory.crawlOut(id, md, withCanvas);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * hand a canvas
     * @param hasCanvas true if canvas taken
     */
    private void handACanvas(boolean hasCanvas)
    {
        try {
            this.setThiefState(ThiefState.COLLECTION_SITE);
            collectionSiteMemory.handCanvas(id, hasCanvas);
            this.setPartyId(-1);
            generalMemory.setOrdinaryThiefState(id, ThiefState.CONCENTRATION_SITE);
            this.setThiefState(ThiefState.CONCENTRATION_SITE);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
