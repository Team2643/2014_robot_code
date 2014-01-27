/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import javax.microedition.io.*;
import java.io.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        
        try {

            SocketConnection sc = (SocketConnection) Connector.open("socket://10.26.43.5:1180");

            sc.setSocketOption(SocketConnection.LINGER, 5);

            InputStream is = sc.openInputStream();
            OutputStream os = sc.openOutputStream();

            os.write("1234567890\r\n".getBytes());
            while (true){
            byte [] lineBuffer = new byte [16];  
            is.read(lineBuffer, 0, 16);
            String isHot = new String(lineBuffer); 
           
            //System.out.println("echo: " + isHot);
            
            if (isHot.indexOf("true")!=-1) {
                System.out.println("goal is hot");
            } else {
                System.out.println("goal is cold");
            }
            }
           /*
            is.close();
            os.close();
            sc.close();
            * */
        } catch (IOException e) {
            System.err.println(e);
        }

        /**
         * This function is called once each time the robot enters test mode.
         */
    }
}
