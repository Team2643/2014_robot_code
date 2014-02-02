package visionprocessor;

import java.util.ArrayList;
//import visionprocessor.PixelCluster;

public class ArrayAnalyzer {

    private ArrayList<PixelCluster> pixelClusters = new ArrayList<>();
    private boolean[][] boolArray;
    private boolean[][] edgeArray;
    private int width;
    private int height;

    public ArrayAnalyzer(boolean[][] array, int w, int h) {
        boolArray = array;
        edgeArray = visionprocessor.VisionProcessor.initializeArray(w, h);

        for (int x = 0; x > w; x++) {
           for (int y = 0; y > h;y++){
               edgeArray[x][y] = false;
           }
        }
        width = w;
        height = h;
    }

    public ArrayList<PixelCluster> getClusters() {
        //Get the pixel cluster at position 1
        return pixelClusters;
    }

    public void analyze() //made a void function because we it didn't seem to be doing anything with pixelClusters
    {
        // ArrayList<PixelCluster> pixelClusters = new ArrayList<PixelCluster>();

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {

                if (boolArray[w][h]) //runs analyzeCluster over all the true pixels in the array
                {
                    analyzeCluster(w, h);
                }
            }
        }


    }

    public int size() {
        return pixelClusters.size();
    }

    public void analyzeCluster(int x, int y) {
        ArrayList<Integer> points = new ArrayList<>();

        PixelCluster cluster = new PixelCluster(width, height);

        points.add(x);
        points.add(y);

        cluster.addX(x);
        cluster.addY(y);

        boolArray[x][y] = false;

        while (true) {
            ArrayList<Integer> temp = new ArrayList<>();
            ArrayList<Integer> edge = new ArrayList<>();
            for (int i = 0; i < points.size(); i += 2) {
                int xCoordinate = points.get(i);
                int yCoordinate = points.get(i + 1);
                int[] sides = {
                    xCoordinate + 1, yCoordinate,
                    xCoordinate - 1, yCoordinate,
                    xCoordinate, yCoordinate + 1,
                    xCoordinate, yCoordinate - 1
                };

                for (int j = 0; j < sides.length; j += 2) {
                    int xSide = sides[ j];
                    int ySide = sides[ j + 1];
                    //checks for adjacency (?) and adds adjacent true pixels to cluster
                    if (xSide >= 0 && xSide < width && ySide >= 0 && ySide < height) {
                        if (boolArray[xSide][ySide]) {
                            temp.add(xSide);
                            temp.add(ySide);

                            cluster.addX(xSide);
                            cluster.addY(ySide);


                            boolArray[xSide][ySide] = false;
                        } else {
                            edge.add(xSide);
                            edge.add(ySide);
                        }
                    }
                }
            }
            points = temp;

            if (temp.size() <= 0) {
                break;
            }
        }

        pixelClusters.add(cluster);
    }
}