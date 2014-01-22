/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import com.sun.squawk.util.MathUtils;

public class EncoderDrive extends SimpleRobot {
    private AnalogChannel armPot = new AnalogChannel(1);
    private Joystick stick1 = new Joystick(1);
    private SpeedController winchMotor = new Jaguar(3);
    private Encoder winchEncoder = new Encoder(4, 5);
    private Solenoid winchPistonOut = new Solenoid(3);
    private Solenoid winchPistonIn = new Solenoid(4);
    private DigitalInput winchLimitSwitch = new DigitalInput(3);
    private SpeedController pickUpMotor = new Jaguar(5);
    private RobotDrive drive = new RobotDrive(1, 2);
    private Encoder driveEncoder = new Encoder(8, 9);
    private Timer Timer = new Timer();
    private Solenoid shiftPiston1 = new Solenoid (1); 
    private Solenoid shiftPiston2 = new Solenoid (2);
    double wheelCircumference = .1;
    double distanceMemory = 0;
    public static void armControl(){
        
    }
    public void shifter(){
         if (stick1.getRawButton(2))
            {
            shiftPiston1.set(true);
            shiftPiston2.set(false);
            }
            if (stick1.getRawButton(3))
            {
            shiftPiston1.set(false);
            shiftPiston2.set(true);
            }
    }
    public void winchControl(){
         if (!winchLimitSwitch.get()) {
                 winchMotor.set(stick1.getY());
                
                 }
                 if ( stick1.getTrigger() ) {
                 winchPistonOut.set(false);
                 winchPistonIn.set(true);
                 }
                 else
                 {
                 winchPistonOut.set(true);
                 winchPistonIn.set(false);
                 }
    }
    public void encoderDistances(){
          double distanceEncoder = driveEncoder.get() / 360.0;
            double distanceTravel = (distanceEncoder * wheelCircumference);
            double speed = distanceTravel / Timer.get();
            if (stick1.getRawButton(11)) {
                driveEncoder.reset();
                distanceTravel = 0.0;
            }

           
            System.out.println("distance  " + (distanceTravel + distanceMemory));

            System.out.println("speed  " + speed);
            if (Timer.get() >= 5) {
                distanceMemory += distanceTravel;
                Timer.reset();
                driveEncoder.reset();
            }
    }
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void autonomous() {
        System.out.println("Autonomous is ON");
        driveEncoder.start();
        double autoWC = 2.54; //one inch in centimeters
        double autoDE = driveEncoder.get() / 360.0;
        double autoDT = (autoDE * autoWC);


        while (autoDT <= (2.54 * 60)) //5 feet in centimeters
        {
            autoDE = driveEncoder.get() / 360.0;
            autoDT = (autoDE * autoWC);
            winchMotor.set(.3);
        }
        driveEncoder.reset();
        winchMotor.set(0);


    }

    public void operatorControl() {
        System.out.println("works");
        winchPistonOut.set(true);
        winchPistonIn.set(false);

        int counter = 0;
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        int switchNumber = 0;
        int oldSwitchNumber = 0;

        /*
         * US==UltraSonic
         * Range:0-500
         */
        int dummy = 5;
        int usValue = 0;
        int smallestUSValue = 10000;//
        int largestUSValue = -100;
        boolean rorshach = false;//
        boolean algernon = true;
        double pValue = 0;
        double iValue = 0;
        double dValue = 0;
        double pModifier = 0.0000001;
        double iModifier = 0.0000001;
        double dModifier = 0.0000001;
        double setPoint = 180;
        double motorSpeed = 0;
        double previouspValue = 0;
       
        
        double armSetPoint = 0.5;
        
        //stateTimer.reset();
        Timer.start();
        driveEncoder.start();
        //winchMotor.set(0.2);
        while (isOperatorControl() && isEnabled()) {
            shifter();
            armControl();
            winchControl();
            encoderDistances();
                

            // drive.arcadeDrive(stick1);



            /*
             if (stick1.getTrigger()) {
             pickUpMotor.set(stick1.getZ());
             }
             */
          //  System.out.println(armPot.getValue());
          


                
                
                 

              
            }

        }
    
    }
