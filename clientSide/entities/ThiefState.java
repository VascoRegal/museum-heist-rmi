package clientSide.entities;

public class ThiefState {
    // MASTER THIEF
    public static final int PLANNING_THE_HEIST = 0;
    public static final int DECIDING_WHAT_TO_DO = 1;
    public static final int ASSEMBLING_A_GROUP = 2;
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;
    public static final int PRESENTING_THE_REPORT = 4;

    // ORDINARY THIEF
    public static final int CONCENTRATION_SITE = 5;
    public static final int CRAWLING_INWARDS = 6;
    public static final int AT_A_ROOM = 7;
    public static final int CRAWLING_OUTWARDS = 8;
    public static final int COLLECTION_SITE = 9;


    /**
     *  Labels used in logging 
     */
    
    private ThiefState () {}
}
