package visionprocessor;

public class Coordinate
{
    public Coordinate( int xCoordinate , int yCoordinate )
    {
        x = xCoordinate;
        y = yCoordinate;
    }
    
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    
    private int x;
    private int y;
}