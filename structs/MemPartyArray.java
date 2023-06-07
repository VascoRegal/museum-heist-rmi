package structs;

import clientSide.entities.ThiefState;
import consts.HeistConstants;

/**
 *  MemPartyArray
 *  
 *  Memory structure to manage the positions and
 *  movements of thieves in a party
 * 
 *  Has pointers to the logical head and tail of the
 *  movement
 */
public class MemPartyArray {
    
    /**
     *  Ordinary Thief in front of the line
     */
    private int head;

    /**
     *  Ordinary Thief at back
     */
    private int tail;

    /**
     *  Array with Ordinary Thief references
     */
    private int [] data;

    /**
     * Party member states
     */
    private int [] states;

    /**
     * Party member positions
     */
    private int [] positions;

    /**
     *  Init
     * 
     *      @param data array to stor sthieves
     */
    public MemPartyArray(int[] data) {
        this.data = data;

        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            data[i] = -1;
        }

        states = new int[HeistConstants.NUM_THIEVES];
        positions = new int[HeistConstants.NUM_THIEVES];
        for (int i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            states[i] = ThiefState.CRAWLING_INWARDS;
            positions[i] = 0;
        }
        head = -1;
        tail = -1;
    }

    /**
     *  Add a thief to the structure
     * 
     *  Called by the Master Thief when forming parties
     * 
     *      @param thief object to add
     *      @throws MemException when party is full
     */
    public void join(int thief) throws MemException {
        int insertIdx;

        if (head == -1) {         // if head is undefinied, this is the first thief
            head = thief;           // assign it to head
            data[0] = thief;        // add it to the array
            return;
        }

        insertIdx = -1;         
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++) {
            if (data[i] == -1) {  // find next insertion position
                data[i] = thief;    // add it
                insertIdx = i;
                break;
            }
        }

        if (insertIdx == -1) {
            throw new MemException("Party Array is full");
        }

        if (insertIdx == (data.length - 1) && tail == -1) {   // if its the last thief
            tail = thief;                                       // assign it to tail
        }

        states[thief] = ThiefState.CRAWLING_INWARDS;
    }

    /**
     *  Find the thief to remove and set its index to null
     *  If thief is head, assign next to head
     * 
     *      @param thief
     */
    public void leave(int thief) {
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++) {
            if (data[i] != -1 && (data[i] == thief)) {
                data[i] = -1;

                if (head == thief) {
                    head = getNext(thief);
                }
            }
        }
    }

    /**
     *  Get the current moving thief
     * 
     *      @return thief
     
    private int getCurrentThief() {
        return ((int) Thread.currentThread());
    }
    */

    /**
     *  Get the next logical thief that should move
     * 
     *      @return thief to wake up next
     */
    public int getNext(int currentThief) {

        if (currentThief == tail) {   // if current is tail
            return head;                                        // next is head
        } else {
            return getClosest(currentThief);                                // else get the closest thief to current
        }
    }

    /**
     *   Get the closest thief to current
     * 
     *   Compares position between current and other thieves
     *   in the array and gets the lowest absolute difference
     * 
     *   If minimums are equal, return the one behind
     * 
     *      @return closest theif to current
     */
    public int getClosest(int currentThief) {
        int i, curThiefPosition, curThiefDistance, minDistance;
        int closestThief;

        closestThief = -1;
        curThiefPosition = positions[currentThief];
        for (i = 0; i < HeistConstants.PARTY_SIZE; i++) {
            if (data[i] != -1) {                                                       // ignore thieves no longer in party
                if ( states[data[i]] != states[currentThief] ||          // ignore thieves with different states
                currentThief == data[i])                       // and dont compare current with itself!
                {
                    continue;
                }

                if (closestThief == -1) {                                                 // if closest is unedifined
                    closestThief = data[i];                                                 // iteration is the closest so far
                } else{
                    curThiefDistance = Math.abs(curThiefPosition - positions[data[i]]);  // distance between current and iteraation thief
                    minDistance = Math.abs(curThiefPosition - positions[closestThief]);  // distance between current and closest
                    if (curThiefDistance == minDistance) {                                  // if distances are equal, return the one behind
                        if (states[currentThief] == ThiefState.CRAWLING_INWARDS) {  // the concept "behind" depends on the direction of the movement
                            if (positions[currentThief] > positions[data[i]]) {
                                closestThief = data[i];
                            }
                        } else if (positions[currentThief] < positions[data[i]]) {    // condition if movement is outwards
                            closestThief = data[i];                                         // update the closest
                        }
                    }
                    else if (curThiefDistance < minDistance) {                              // if found a new minimum
                        closestThief = data[i];                                             // update the closest
                    }
                } 
            }
        }
        return closestThief;
    }

    /**
     *  Checks if current thief can move
     *  
     *      @return true, if there are possible movements
     *              false, if movements would violate rules
     */
    public boolean canMove(int currentThief) {
        int closestThief;
        int closestDistance;

        closestThief = getClosest(currentThief);
        if (tail == currentThief ||                               // if current is tail
            closestThief == -1                                                            // or there's no closest thief (current is the only still moving)
        ) {                          
            return true;                                                                    // he can always move (no thief will be left behind)
        }

        closestDistance = Math.abs(positions[currentThief] - positions[closestThief]);// distance to the closest thief
        return ( closestDistance < HeistConstants.MAX_CRAWLING_DISTANCE );                  // if closest distance less than max distance, ret true
    }

    /**
     *  Execute the best possible movement and update
     *  pointers to head and tail
     */
    public void doBestMove(int currentThief, int md) {
        int finalPosition, increment, minDistance;
        int newTail, closestThief;

        closestThief = getClosest(currentThief);
        if (closestThief == -1) {                                                // there is no closestThief, current is alone in the movement
            increment = md;                         // so he can always move the max possible distance
            if (states[currentThief] == ThiefState.CRAWLING_OUTWARDS) {
                increment = - increment;                                           // for the outwards movement, increments are negative
            }

            positions[currentThief] = positions[currentThief] + increment;
            tail = currentThief;                                                
            return;
        }

        if (tail == currentThief) {         // if current is tail                                    
            increment =  md;           // no need to check if thieves are being left behind
        } else                                                        // else compare the distances to find maximum increment possible
        {
            increment = Math.min(md, HeistConstants.MAX_CRAWLING_DISTANCE - (Math.abs(positions[currentThief] - positions[closestThief])));
        }
        
        if (states[currentThief] == ThiefState.CRAWLING_OUTWARDS) {     // increment is negative on outwards movement
            increment = - increment;
        }


        System.out.println("Pos BEFORE for thief OT_" + currentThief + " : " + positions[currentThief]);
        positions[currentThief] += increment;
        System.out.println("Pos AFTER for thief OT_" + currentThief + " : " + positions[currentThief]);
        finalPosition = positions[currentThief];                          // move the thief

        if (states[currentThief] == ThiefState.CRAWLING_INWARDS) {      // update head if position is new maximum
            if (finalPosition > positions[head]) {
                head = currentThief;
            }
        } else {
            if (finalPosition < positions[head]) {
                head = currentThief;
            }
        }


        if (currentThief == tail) {                   // update tail if there's a new minimum
            newTail = tail;
            minDistance = positions[tail];
            for (int i = 0 ; i < HeistConstants.PARTY_SIZE; i++) {
                if (data[i] != -1 && (currentThief != data[i]))
                {
                    if (states[currentThief] == ThiefState.CRAWLING_INWARDS) {
                        if (positions[data[i]] < minDistance) {
                            minDistance = positions[data[i]];
                            newTail = data[i];
                        }
                    } else {
                        if (positions[data[i]] > minDistance) {
                            minDistance = positions[data[i]];
                            newTail = data[i];
                        }
                    }
                }
            }
            tail = newTail;
        }
    }

    /**
     *  Return current head
     *      @return head
     */
    public int head() {
        return this.head;
    }

    /**
     *  Return current tail
     *      @return tail
     */
    public int tail() {
        return this.tail;
    }

    /**
     * Get the position of a thief in a party
     * @param thiefId
     * @return thief pos
     */
    public int getPosition(int thiefId)
    {
        return this.positions[thiefId];
    }

    /**
     * set the position of a thief in a party
     * @param thiefId
     * @param position
     */
    public void setPosition(int thiefId, int position)
    {
        this.positions[thiefId] = position;
    }

    /**
     * set the state of a thief in a party
     * @param thiefId
     * @param state
     */
    public void setState(int thiefId, int state)
    {
        this.states[thiefId] = state;
    }
    
    /**
     * Check if a party is ready to go
     * @return true if it is, false if not
     */
    public boolean partyReady()
    {
        boolean ready = false;
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            if (data[i] == -1)
            {
                return ready;
            }
        }
        return true;
    }

    /**
     * Helper printer for debugging
     */
    public void printPartyStatus()
    {
        System.out.println("\n--------------");
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            System.out.println(String.format("%d - OT_%d (pos=%d, state=%d, closest=%d)", i, data[i], positions[data[i]], states[data[i]], getClosest(data[i])));
        }
        System.out.println("head=" + head);
        System.out.println("tail=" + tail);
        System.out.println("----------------\n");
    }
}