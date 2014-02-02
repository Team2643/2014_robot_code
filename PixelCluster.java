package visionprocessor;

import java.util.ArrayList;
import java.io.PrintWriter;

public class PixelCluster //used to find the bounding box
{

    public PixelCluster(int w, int h) {
        topX = w;
        minX = w;
        maxX = 0;
        topY = h;
        minY = h;
        maxY = 0;
        numberOfPixels = 0;
        centX = 0;
        centY = 0;
    }

    public void getClusterElements(PrintWriter out) {
        if (numberOfPixels > 100) {
            out.print("Num px: " + numberOfPixels + "\t");
            out.print("Min X: " + minX + "\t");
            out.print("Max X: " + maxX + "\t");
            out.print("Min Y: " + minY + "\t");
            out.print("Max Y: " + maxY + "\t");
            out.print("Rectangularity: " + getRectangularity(maxX, maxY, minX, minY, numberOfPixels) + "\n");
            out.print("Aspect Ratio: " + getRatio(maxX, maxY, minX, minY) + "\t");
            if (!isRectangular()) {
                out.println("It is not rectangular");
            }
            if (!rightRatio()) {
                out.println("It is not the right ratio");
            }
            if (isHot()) {
                out.println("Goal is hot \t");
            }
            if (isGoalOnLeft()) {
                out.println("Goal is on left \t");
            }
            if (isGoalOnRight()) {
                out.println("Goal is on right \t");
            }
            out.println("");
            //this.getRectangularity();
        }

    }

    public void add(int i) {
        array.add(i);

    }

    public void addX(int x) {
        if (x < minX) {
            minX = x;
        }
        if (x > maxX) {
            maxX = x;
        }
        numberOfPixels++;
        centX = (centX + x) / numberOfPixels;
        add(x);
    }

    public void addY(int y) {
        if (y < minY) {
            minY = y;
        }
        if (y > maxY) {
            maxY = y;
        }
        centY = (centY + y) / numberOfPixels;
        add(y);
    }

    public boolean getLeftRight() {
        //true = on left false = on right
        boolean left = true;
        if (centX <= (topX / 2)) {
            left = false;
        }
        return left;
    }

    public boolean isGoalOnLeft() {
        boolean leftGoal = true;
        leftGoal = (isHot() && getLeftRight());
        return leftGoal;
    }

    public boolean isGoalOnRight() {
        boolean rightGoal = true;
        rightGoal = (isHot() && !getLeftRight());
        return rightGoal;
    }

    public double getRatio(float localMaxX, float localMaxY, float localMinX, float localMinY) {
        //Get the ratio of the height of the cluster compared to the width
        double rectLength = localMaxX - localMinX;
        double rectHeight = localMaxY - localMinY;
        double ratio = rectLength / rectHeight;
        return ratio;

    }//gets the number of filled pixe

    public double getRectangularity(float localMaxX, float localMaxY, float localMinX, float localMinY, float localNumPix)//get how much of the bounding box has color inside of it
    {
        double rectLength = localMaxX - localMinX;
        double rectHeight = localMaxY - localMinY;
        double area = rectLength * rectHeight;
        double rectangularity = localNumPix / area;
        //System.out.println("Rectangularity: " + rectangularity + "\n");
        return rectangularity;
    }

    public boolean isHot() {

        boolean hot = false;
        if (numberOfPixels > 100) {
           // if (this.getRectangularity(maxX, maxY, minX, minY, numberOfPixels) >= 0.8 && this.getRatio(maxX, maxY, minX, minY) >= 5) 
            if (rightRatio() && isRectangular()) {
                hot = true;
            }
        }

        return hot;
    }

    public boolean isRectangular() {
        boolean rectangular = false;
        if (this.getRectangularity(maxX, maxY, minX, minY, numberOfPixels) >= 0.8) {
            rectangular = true;
        }
        return rectangular;
    }

    public boolean rightRatio() {
        boolean isRatio = false;
        if (this.getRatio(maxX, maxY, minX, minY) >= 5) {
            isRatio = true;
        }

        return isRatio;
    }

    public void getCoordinate() {
        //Get the coordinates of the cluster's bounding box
    }

    public void getDistance() {
        //Return the distance of the object to the robot in ___
    }
    private ArrayList<Integer> array = new ArrayList<>();
    private int width;
    private int height;
    private int topX;
    private int topY;
    private int centX;
    private int centY;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int numberOfPixels;
}