package visionprocessor;

import java.awt.*;

public class PixelAnalyzer
{
    public static boolean isColor( Color c )
    {
        double red = c.getRed();
        double green = c.getGreen();
        double blue = c.getBlue();
        
        boolean isRed = red < ( green / 4.0 );
        boolean isGreen = green > 100;
        boolean isBlue = blue < green;
        
        return( isRed && isGreen && isBlue );
    }
    
    
    
    
    
    /*
    public PixelAnalyzer( int r , int g , int b , int maxBright , int minBright , int color )
    {
        red = r;
        blue = b;
        green = g;
        
        double redDouble = (double)(red);
        double blueDouble = (double)(blue);
        double greenDouble = (double)(green);
        
        blueDisparity = ( blueDouble - redDouble ) / redDouble;
        greenDisparity = ( greenDouble - redDouble ) / redDouble;
        
        maxBrightnessTolerance = maxBright;
        minBrightnessTolerance = minBright;
        
        colorTolerance = (double)color / 100.0;
    }
    public boolean isColor( Color c )
    {
        
        double redDouble = (double)(c.getRed());
        double blueDouble = (double)(c.getBlue());
        double greenDouble = (double)(c.getGreen());
        
        double blueDifference = ( blueDouble - redDouble ) / redDouble;
        double greenDifference = ( greenDouble - redDouble ) / redDouble;
                
        if( Math.abs( blueDifference - blueDisparity ) > colorTolerance )
        {
            return false;
        }
        if( Math.abs( greenDifference - greenDisparity ) > colorTolerance )
        {
            return false;
        }
                
        if( ( redDouble - red ) > red * ( (double)maxBrightnessTolerance / 100.0 ) )
        {
            return false;
        }
        if( ( greenDouble - green ) > green * ( (double)maxBrightnessTolerance / 100.0 ) )
        {
            return false;
        }
        if( ( blueDouble - blue ) > blue * ( (double)maxBrightnessTolerance / 100.0 ) )
        {
            return false;
        }
        
        return true;
    }
    private int red;
    private int blue;
    private int green;
    
    private double blueDisparity;
    private double greenDisparity;
    
    private int maxBrightnessTolerance;
    private int minBrightnessTolerance;
    private double colorTolerance;
    */
}