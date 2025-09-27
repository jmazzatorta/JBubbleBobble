package model.utils;


public class MinorEvent  {

    private final int x, y, scoredPoints;
    private final String type, user;

    public MinorEvent(int x, int y, String type, String user, int scoredPoints) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.user = user;
        this.scoredPoints = scoredPoints;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getUser() { return user; }
    public String getType() { return type; }
    public int getScoredPoints() { return scoredPoints; }

    
}
