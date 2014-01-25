package visionprocessor;

import java.util.ArrayList;
import java.io.PrintWriter;

public class PixelCluster //used to find the bounding box
{
    public PixelCluster( int w , int h )
    {
        minX = w;
        maxX = 0;
        
        minY = h;
        maxY = 0;
        numberOfPixels = 0;
    }
    
    public void getClusterElements(PrintWriter out)
    {
        if (numberOfPixels > 100){
        out.print("Num px: " + numberOfPixels + "\t");
        out.print("Min X: " + minX + "\t");
        out.print("Max X: " + maxX + "\t");
        out.print("Min Y: " + minY + "\t");
        out.print("Max Y: " + maxY + "\n");
        out.println("");
        }
        
    }
    
    
    
    
    
    public void add( int i )
    {
        array.add( i );
        numberOfPixels++;
    }
        public void addX( int x )
        {
            if( x < minX )
            {
                minX = x;
            }
            if( x > maxX )
            {
                maxX = x;
            }
            
            add( x );
        }
        public void addY( int y )
        {
            if( y < minY )
            {
                minY = y;
            }
            if( y > maxY )
            {
                maxY = y;
            }
            
            add( y );
        }
    
    public void getRatio()
    {
        //Get the ratio of the hight of the cluster compared to the width
    }
    public void getCoordinate()
    {
        //Get the coordinates of the cluster's bounding box
    }
    public void getDistance()
    {
        //Return the distance of the object to the robot in ___
    }
    public void finalize()
    {
        //Set width, height, and coordinates
    }
    
    private ArrayList<Integer> array = new ArrayList<Integer>();
    
    private int width;
    private int height;
    
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int numberOfPixels;
}