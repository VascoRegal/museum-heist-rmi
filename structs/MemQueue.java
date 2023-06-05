package structs;

/**
 *  MemQueue Class
 * 
 *  parametric FIFO queue
 * 
 *  used to manage available and arriving thieves
 *  
 */
public class MemQueue<T> {
    /**
     * pointers and size
     */
    private int in, out, size;

    /**
     * empty queue flag
     */
    private boolean empty;

    /**
     * actual data array
     */
    private T[] array;

    /**
     * constructor
     * @param array
     */
    public MemQueue( T [] array) {
        this.array = array;
        this.in = this.out = 0;
        this.size = 0;
        empty = true;
    }

    /**
     * enqueue an object
     * @param object
     */
    public void enqueue(T object) {
        if ((in != out) || empty) {
            array[in] = object;
            in = (in + 1) % array.length;
            empty = false;
            size++;
        }
    }

    /**
     * dequeue an object
     * @return
     */
    public T dequeue() {

        T obj = null;

        if (!empty) {
            obj = array[out];
            out = (out + 1) % array.length;
            empty = (in == out);
            size--;
        }
        return obj;
    }

    /**
     * get the current queue size
     * @return
     */
    public int size() {
        return this.size;
    }

    /**
     * get the empty flag
     * @return
     */
    public boolean empty()
    {
        return this.size == 0;
    }
}

