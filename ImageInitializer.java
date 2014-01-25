package visionprocessor;

import java.net.URL;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class ImageInitializer
{
    public static BufferedImage returnImage( String s ) throws Exception
    {
        URL imageURL = new URL( s );

        BufferedImage img = ImageIO.read( imageURL );
        return img;
    }
}
