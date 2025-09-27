package constants;

public class Constants {
	public static final int FPS= 60;
	public static final double TIME_PER_TICK_NS = 1_000_000_000.0 / FPS;
	
	public static final int ORIGINALTILESIZE = 16;
	public static final int SCALE= 3;
	public static final int TILESIZE= ORIGINALTILESIZE* SCALE;
	
	public static final int MAXSCREENCOL= 32;
	public static final int MAXSCREENROW= 28;
	
	public static final int SCREENWIDTH= TILESIZE* MAXSCREENCOL;
	public static final int SCREENHEIGHT= TILESIZE* MAXSCREENROW;
	public static final int HALF_SCREENWIDTH= SCREENWIDTH/2;
	public static final int HALF_SCREENHEIGHT= SCREENHEIGHT/2;
	
	public static final int LEFTBORDER = 1;
	public static final int RIGHTBORDER = 30;
	public static final int TOPBORDER = 2;
	public static final int BOTTOMBORDER = 28;
	
	public static final float MAX_FALL_SPEED = 10.0f;
	
	public static final int VOIDROW = (BOTTOMBORDER-1)*TILESIZE;
	
	public static final double GRAVITY = 0.6;
	
	public static final short MAX_ENEMIES = 6;
	public static final short MAX_BUBBLES = 15;
	public static final short MAX_VFX = 10;
	
	public static final int AVATARS_NUM = 8;
	public static final int RANK_DIMENSION = 5;
	
	
}
