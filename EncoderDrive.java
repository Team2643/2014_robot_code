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
    
    private Joystick stick1 = new Joystick( 1 );
    private SpeedController winchMotor = new Jaguar(3);
    private Encoder winchEncoder = new Encoder ( 1 , 2 );
    private Solenoid winchPistonOut = new Solenoid (1);
    private Solenoid winchPistonIn = new Solenoid( 2 );
    private DigitalInput winchLimitSwitch = new DigitalInput (3);
    private SpeedController pickUpMotor = new Jaguar(5);
    private RobotDrive drive = new RobotDrive( 1,2 );
    
    
    edu.wpi.first.wpilibj.DigitalInput arman = new edu.wpi.first.wpilibj.DigitalInput(1);
            

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        
        //while (arman.get() == true)
         //   {
          //      String dead = "ramzi";
          //  }
        winchPistonOut.set(true);
        winchPistonIn.set(false);
        
        int counter = 0;
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft , true );
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft , true );
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight , true );
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight , true );
        int switchNumber = 0;
        int oldSwitchNumber = 0;
        
        /*
         * US==UltraSonic
         * Range:0-500
         */
        int usValue = 0;
        int smallestUSValue = 10000;//
        int largestUSValue = -100;
        boolean rorshach = false;//
        boolean algernon = true;
        
        
        //stateTimer.reset();
        while (isOperatorControl() && isEnabled() )
        {
            drive.arcadeDrive(stick1);
            if (stick1.getTrigger())
            {
                pickUpMotor.set( stick1.getZ() );
            }
  
            
            /*
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
            */
            
            /*
            double leftTankY = (Math.abs(leftTankStick.getY())*leftTankStick.getY());
            double leftTankZ = Math.abs(leftTankStick.getThrottle())*leftTankStick.getThrottle();
            double leftTankX = Math.abs(leftTankStick.getX()) * leftTankStick.getX();
            
            double leftTankM = Math.sqrt( Math.abs(leftTankX) + Math.abs(leftTankY) );
            double leftTankA = Math.toDegrees(MathUtils.atan2(-leftTankStick.getX(), -leftTankStick.getY()));
            System.out.println("M: "+leftTankM);
            System.out.println("A: " +leftTankA);
            double leftMotorCos = leftTankM * Math.cos(Math.toRadians(leftTankA - 45));
            double rightMotorCos = leftTankM * Math.cos(Math.toRadians(leftTankA + 45));
            System.out.println("Left Motor Output:" + leftMotorCos);
            System.out.println("Right Motor Output:" + rightMotorCos);
            System.out.println();
            
            
            if(leftTankStick.getRawButton(7))
            {
                if(algernon)
                {
                rorshach = !rorshach;
                algernon = false;
                }
                
            }
            else
            {
                algernon = true;
            }
            
           
            if (rorshach)
            { 
                leftTankY = 0.5 * leftTankY;
                leftTankZ = 0.5 * leftTankZ;    
            }
            
            
          
            
            if (leftTankStick.getRawButton(2)) //2 is the A button
            { arcadeOn = true;
            System.out.println("Arcademode is ON"); 
            
                    }
            if (leftTankStick.getRawButton(3)) //3 is the B button
            { arcadeOn = false;
            System.out.println("Arcademode is OFF"); 
            
            }
           
         if (arcadeOn == true)
         {
         left.set(rightMotorCos);
            right.set(-1 * leftMotorCos);
         }
         else
         {
         left.set(-1 * leftTankY);
            right.set(leftTankZ);
         }
            
            
            
            
            
                
            
            
            
            
            
            
            
            oldSwitchNumber = switchNumber;
            * */
           
            /*
            switch (switchNumber)
            {
                case 0: 
                    
                    left.set(.3);
                    right.set(-.333);
                    switchNumber= 1;
                    
                    break;
                    
                case 1:
                    usValue=  itsSupaSonic.getAverageValue();
                    if (usValue < smallestUSValue)
                        smallestUSValue = usValue;
                    if (usValue > largestUSValue)
                        largestUSValue = usValue;
                    if ( usValue <30)
                    {
                    switchNumber= 2;
                    }
                    
                    
                    break;
                    
                    
                case 2:
                    stateTimer.start();
                    stateTimer.reset();
                    left.set(.33);
                    right.set(.3);
                    
                    switchNumber= 3;
                    
                    break;
                    
                case 3:
                    if(stateTimer.get()>=2.5){
                        stateTimer.reset();
                        switchNumber= 0;
                    }
                    
                    break;
                    
                    
                    default: System.out.println("The Switch is Out");
            }//switch
            */
            
            /*
            if (switchNumber != oldSwitchNumber)
            {
                System.out.print("Switch has changed. Old: " + oldSwitchNumber + " New: " + switchNumber);
                System.out.println(" Ultrasound number: " + usValue);
                System.out.println("Lowest Ultrasound Value: " + smallestUSValue);
                System.out.println("Highest Ultrasound Value: " + largestUSValue);
                
                
            }
            
            System.out.println( leftTankStick.getX() + " " + leftTankStick.getY() + " " + leftTankStick.getZ() + " " + leftTankStick.getThrottle());
           */
            
            /*
            TestPistonOut.set(true);
            
            //System.out.println("Works");
            if( counter < 25000 )
            {
                counter++;
            }
            else
            {
                counter = 0;
                System.out.println( itsSupaSonic.getAverageValue() );
            }
            
            if( itsSupaSonic.getAverageValue() < 60 )
            {
                if( arcadeStick.getY() > 0 )
                {
                    drive.arcadeDrive( arcadeStick );
                }
                else
                {
                    drive.arcadeDrive( 0 , 0 );
                }
            }
            else
            {
                drive.arcadeDrive( arcadeStick );
            }
            */
        }
        
    }
}
