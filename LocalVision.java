package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class LocalVision extends SimpleRobot {

    public AxisCamera camera;
            ColorImage img;
            BinaryImage binImg;

    public void robotInit() {
        camera = AxisCamera.getInstance();
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);        
        boolean imageIsAvailable = false;
        while (!imageIsAvailable){
             try {
               img = camera.getImage();
               img.free();
               imageIsAvailable = true;
             } catch (NIVisionException ex) {
                ex.printStackTrace();
             } catch (AxisCameraException ex) {
                ex.printStackTrace();
             }
        }
    }
    
    public void autonomous() {

        while (isAutonomous() && isEnabled()) {
           

            try {
                img = camera.getImage();

                try {
                    //img.write("/ChauSecretImageInColor.jpg");
                    binImg = img.thresholdHSV(80, 113, 160, 255, 0, 255);
                    img.free();
                    //binImg = img.thresholdHSV( 0 , 255 , 0 , 255 , 0 , 255 );
                    try {
                        // binImg.write("/GeneralChauSuperSecretImage.jpg");
                        // binImg.removeSmallObjects(true, 2);
                        // binImg.write( "/GeneralChauWithoutSmallObjects.jpg" );
                        ParticleAnalysisReport[] reports = binImg.getOrderedParticleAnalysisReports(3);
                        binImg.free();
                        for (int i = 0; i < reports.length; i++) {
                            ParticleAnalysisReport report = reports[ i];
                            if (report.particleArea > 100) {
                                System.out.println("Number: " + i + " Height: " 
                                        + report.boundingRectHeight + " Width: "
                                        + report.boundingRectWidth + " Area: " +
                                        report.particleArea);
                            }
                            else{
                                break;
                            }
                        }
                    } catch (NIVisionException ex) {
                        ex.printStackTrace();
                    }
                } catch (NIVisionException ex) {
                    ex.printStackTrace();
                }
                //H:150 | 130 - 160
                //S:70 - 100
                //V:80 - 100

//                try {
//                    img.free();
//                } catch (NIVisionException ex) {
//                    ex.printStackTrace();
//                }
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    }
}
