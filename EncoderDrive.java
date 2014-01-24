/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.             
 *const int SHOOTER_ANGLE_SOLENOID_1 = 3;
 const int SHOOTER_ANGLE_SOLENOID_2 = 4;
 #if YEAR_2013
 const int shooter_fire_piston_solenoid_A = 1;
 const int shooter_fire_piston_solenoid_B = 2;
 #else
 const int shooter_fire_piston_solenoid_A = 4;
 const int shooter_fire_piston_solenoid_B = 5;
 #endif*/
/*#if YEAR_2013
 const int left_drive_motor_A_PWM = 5;//1
 const int left_drive_motor_B_PWM = 6;//2
 const int right_drive_motor_A_PWM = 1;//5
 const int right_drive_motor_B_PWM = 2;//6
 const int shooter_front_motor = 4;//3
 const int shooter_back_motor = 3; //4
 const int clconstimbing_motor_PWM = 7;

 #else
 const int left_drive_motor_A_PWM = 3;
 const int right_drive_motor_A_PWM = 1;
 const int shooter_front_motor = 4;
 const int shooter_back_motor = 6;
 #endif

 // Joysticks
 const int operator_joystick = 3;
 const int right_stick = 2;
 const int left_stick = 1;

 // Joystick Buttons

 // Driver Stick Prim
 const int arcade_button = 10;
 const int tank_button = 11;
 const int slow_drive_button_prim = 4;

 /*
 * Assigned But not tested
 
 const int climber_off_button_A = 8;
 const int climber_off_button_B = 9;
 const int climber_hold_down = 2;
 const int climber_hold_up = 3;
 const int climber_send_bottom = 7;
 const int climber_send_top = 6;
 /*
 * 
 */
// Driver Stick Sec
/*const int slow_drive_button_sec = 4;

 // Operator Stick
 const int shooter_piston_button = 1;
 const int tilt_up_button = 5;
 const int tilt_down_button = 4;
 const int back_position_middle_goal_button = 3;//was front_position_button
 const int back_position_button_1 = 2;
 const int back_position_button_2 = 6;
 const int back_position_button_3 = 7;
 const int back_position_button_4 = 11;
 const int front_position_button = 10;//was back_position_button_5
 const int dumper_button_A = 8;
 const int dumper_button_B = 9;

 //Speeds
 const float front_position_RPS = 39.0;
 const float middle_goal_RPS = 39.0;
 const float back_position_RPS_1 = 41.75;//latest was 46  // was 36.5 at  34 degrees
 const float back_position_RPS_2 = 41.0;//36.5
 const float back_position_RPS_3 = 38.0;
 const float back_position_RPS_4 = 36.8;
 const float back_position_RPS_5 = 36.9;
 const float dumper_RPS = 20;

 //Limit Switches -DI
 const int top_claw_limit_switch_port = 6;
 const int bottom_claw_limit_switch_port = 7;//7

 //Encoders DI
 //
 const int shooter_motor_front_encoder_A_port = 1;//single wire
 const int shooter_motor_front_encoder_B_port = 2;//triple wire
 #if YEAR_2013
 //Back Shooter Encoders
 const int shooter_motor_back_encoder_A_port = 8;//single wire
 const int shooter_motor_back_encoder_B_port = 9;//triple wire
 #else
 const int shooter_motor_back_encoder_A_port = 7;
 const int shooter_motor_back_encoder_B_port = 8;
 #endif*/
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

    private AnalogChannel armPot = new AnalogChannel(4);
    private Joystick stick1 = new Joystick(1);
    private Joystick climberstick = new Joystick(3);
    private SpeedController winchMotor = new Jaguar(3);
    private Encoder winchEncoder = new Encoder(4, 5);
    private Solenoid winchPistonOut = new Solenoid(3);
    private Solenoid winchPistonIn = new Solenoid(4);
    private DigitalInput winchLimitSwitch = new DigitalInput(3);
    private SpeedController pickUpMotor = new Jaguar(5);
    private RobotDrive drive = new RobotDrive(1, 2);
    private Encoder driveEncoder = new Encoder(8, 9);
    private Timer Timer = new Timer();
    private Solenoid shiftPiston1 = new Solenoid(1);
    private Solenoid shiftPiston2 = new Solenoid(2);
    private SpeedController armMotor = new Victor(4);
    double wheelCircumference = .1;
    double distanceMemory = 0;
    final int clconstimbing_motor_PWM = 7;
    private SpeedController climbing_motor = new Victor(clconstimbing_motor_PWM);
    final double inverter = -1.0;
    double setPoint = 0;
    double currentPoint = 0;
    double armDifference = 0;
    /**
     * Take pot value and converts it to joystick values.
     * @param potValue double
     * @return potValue double
     */
    public double potConvert(double potValue) {
        double maxAllowedValue = 1.0;
        double minAllowedValue = -1.0;
        potValue = potValue - 500;
        potValue = potValue / 500;

        if (potValue >= maxAllowedValue) {
            potValue = maxAllowedValue;
        }

        if (potValue <= minAllowedValue) {
            potValue = minAllowedValue;
        }
        return potValue;
    }
    
    public void armControl() {
        setPoint = stick1.getZ() * inverter;
        currentPoint = potConvert(armPot.getValue());
        armDifference = (setPoint - currentPoint) / 2;
    // TO DO:
    }

    public void shifter() {
        if (stick1.getRawButton(2)) {
            shiftPiston1.set(true);
            shiftPiston2.set(false);
        }
        if (stick1.getRawButton(3)) {
            shiftPiston1.set(false);
            shiftPiston2.set(true);
        }
    }

    public void winchControl() {
        if (!winchLimitSwitch.get()) {
            winchMotor.set(stick1.getY());

        }
        if (stick1.getTrigger()) {
            winchPistonOut.set(false);
            winchPistonIn.set(true);
        } else {
            winchPistonOut.set(true);
            winchPistonIn.set(false);
        }
    }

    public void encoderDistances() {
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
        double motorSpeed = 0;
        double previouspValue = 0;

        double climbspeed;


        double armSetPoint = 0.5;

        //stateTimer.reset();
        Timer.start();
        driveEncoder.start();
        //winchMotor.set(0.2);
        while (isOperatorControl() && isEnabled()) {
            shifter();
            armControl();
            winchControl();
            //  encoderDistances();

            /*climbspeed = climberstick.getY();
             
             System.out.println("JS3 Y= " + climbspeed);
             
             climbing_motor.set(climbspeed);
             */


            // drive.arcadeDrive(stick1);



            /*
             if (stick1.getTrigger()) {
             pickUpMotor.set(stick1.getZ());
             }
             */
            System.out.println(potConvert(armPot.getValue()));








        }

    }
}
