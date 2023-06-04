package consts;

/**
 * Collection of problem constants
 */
public final class HeistConstants {

	private HeistConstants()
	{
	}


	public static final int NUM_THIEVES = 6;
	public static final int NUM_ROOMS = 5;
	public static final int PARTY_SIZE = 3;
	public static final int MAX_NUM_PARTIES = Math.floorDiv(NUM_THIEVES, PARTY_SIZE);

	public static final int MAX_CRAWLING_DISTANCE = 3;

	public static final int MIN_THIEF_MD = 2;
	public static final int MAX_THIEF_MD = 5;

	public static final int MIN_DISTANCE_OUTSIDE = 15;
	public static final int MAX_DISTANCE_OUTSIDE = 30;

	public static final int MIN_NUM_PAINTINGS = 8;
	public static final int MAX_NUM_PAINTINGS = 16;
}
