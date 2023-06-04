package serverSide.objects;

import clientSide.entities.ThiefState;
import consts.HeistConstants;
import interfaces.GeneralRepoInterface;

/**
 * General shared region
 */
public class GeneralRepo implements GeneralRepoInterface {
    
    /**
     * master thief state reference
     */
    private int mtState;

    /**
     * ordinary thief state reference
     */
    private int [] otStates;

    /**
     * instatiation
     */
    public GeneralRepo()
    {
        int i;

        this.mtState = ThiefState.PLANNING_THE_HEIST;
        this.otStates = new int[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            otStates[i] = -1;
        }
    }

    /**
     * initialize logging
     * @param fileName
     */
    public synchronized void init(String fileName)
    {
        // TODO: LOGS
    }

    /**
     * set the state of the MT
     * @param ts
     */
    public synchronized void setMasterThiefState(int ts)
    {
        this.mtState = ts;
        System.out.println("[GENERAL] Update MasterThief state to " + ts);
    }

    /**
     * set Ot states
     * @param thiefId
     * @param ts
     */
    public synchronized void setOrdinaryThiefState(int thiefId, int ts)
    {
        System.out.println("[GENERAL] Update OrdinaryThief_" + thiefId + " state to " + ts);
        this.otStates[thiefId] = ts;
    }
}
