package clientSide.entities;

public class Thief extends Thread {
    
    /**
     *  Thief identification
     */

     protected final int id;
    
     /**
      *  Thief state
      */
 
     protected int state;
 

 
     /**
      *  Instantiation
      * 
      *      @param id thief identification
      */
 
     public Thief(int id) {
         this.id = id;
         Thread.currentThread().setName(this.toString());
     }
 
     /**
      *  Get Thief identification
      * 
      *      @return thief id
      */
 
     public int getThiefId() {
         return this.id;
     }
 
     /**
      *  Get Thief state
      * 
      *      @return thief state
      */
 
     public int getThiefState() {
         return state;
     }
 
     /**
      *  Set Thief state
      * 
      *      @param thief state
      */
     
     public void setThiefState(int state) {
         this.state = state;
     }
}
