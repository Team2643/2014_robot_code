package visionprocessor;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class VisionProcessor {

    public static void main(String[] args) throws Exception {
        int portNumber = 1180;
        //try (
                ServerSocket serverSocket =
                new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
      /*){  } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        * */
        
        PrintWriter sysOut = new PrintWriter(System.out, true);
//while (true){
for (int ii = 0; ii < 10000; ii++) {        
        BufferedImage img = ImageInitializer.returnImage("http://10.26.43.11/jpg/image.jpg");
//        BufferedImage img = ImageInitializer.returnImage("file:///C:/Users/User/Documents/NetBeansProjects/FRCVision2014/VisionImages/2014%20Vision%20Target/Center_18ft_On.jpg");
//          BufferedImage img = ImageInitializer.returnImage("http://10.26.43.11/axis-cgi/jpg/image.cgi");
         
                
        int width = img.getWidth();
        int height = img.getHeight();
//        int[] pixel = new int[width*height];

//        img.getRGB(0, 0, width, height, pixel, 0, width);;

        boolean[][] array = initializeArray(width, height);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int pixel = img.getRGB(w, h);
                Color c = new Color(pixel);
//                Color c = new Color(pixel[w+h*width]);

                array[w][h] = PixelAnalyzer.isColor(c);
            }
        }

        ArrayAnalyzer arrayAnalyzer = new ArrayAnalyzer(array, width, height);
        //ArrayList<PixelCluster> pixelClusters = 
        arrayAnalyzer.analyze();
        
        boolean targetHot = false;
        for (int i = 0; i < arrayAnalyzer.getClusters().size(); i++) {  
        arrayAnalyzer.getClusters().get(i).getClusterElements(sysOut);
       targetHot = targetHot || arrayAnalyzer.getClusters().get(i).isHot();
        
        //arrayAnalyzer.getClusters().get(i).getRectangularity();
        }
        out.println(targetHot);
}
                
                
    }

    public static boolean[][] initializeArray(int w, int h) {
        boolean[][] array = new boolean[w][h];
        for (int i = 0; i < w; i++) {
            array[i] = new boolean[h];
        }

        return array;
    }
}
