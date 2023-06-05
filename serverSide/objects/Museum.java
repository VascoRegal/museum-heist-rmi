package serverSide.objects;

import consts.HeistConstants;
import interfaces.MuseumInterface;
import serverSide.entities.Room;
import serverSide.entities.RoomState;



/**
 *  MuseumMemory class
 *
 *  Shared memory containing rooms and room operations
 *
 */
public class Museum implements MuseumInterface {
    
     /*
     *   Array of rooms
     */

    private final Room [] rooms;

    /**
     *  Museum Memory memory instantiation.
     *
     *    @param generalMemory general memory reference
     */

    public Museum() {
        int totalpaintings = 0;
        rooms = new Room [HeistConstants.NUM_ROOMS];
        for (int i=0; i < HeistConstants.NUM_ROOMS; i++) {
            rooms[i] = new Room(i);
            totalpaintings += rooms[i].getNumHangingPaintings();
            System.out.println("ROOM " + i + "[@ " + rooms[i].getLocation() + "] - " + rooms[i].getNumHangingPaintings() + " paintings");
        }
        System.out.println("Total :" + totalpaintings );
    }

    /**
     * Get location of a room
     * 
     * @param roomId
     * @return location of the room
     */
    public int getRoomLocation(int roomId)
    {
        return rooms[roomId].getLocation();
    }


    /**
     *  
     *  Roll a canvas.
     * 
     *  Nothing happens if there are no canvas on the walls
     *
     *    @param roomId room identification
     */
    public synchronized boolean pickCanvas(int roomId) {
        Room targetRoom;
        boolean picked = false;
        targetRoom = rooms[roomId];

        System.out.println(String.format("[MUSEUM] Room_%d Lost a painting (we can roll it up)", roomId));
        if (!targetRoom.isEmpty()) {
            targetRoom.removePainting();
            picked = true;
        } else {
            targetRoom.setRoomState(RoomState.COMPLETED);
        }

        System.out.println(String.format("[MUSEUM] Room_%d has %d paintings remaining", roomId, rooms[roomId].getNumHangingPaintings()));
        return picked;
    }
}