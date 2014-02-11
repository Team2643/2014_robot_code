/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory. const int left_drive_motor_A_PWM = 5;//1 const int
 * left_drive_motor_B_PWM = 6;//2 const int right_drive_motor_A_PWM = 1;//5
 * const int right_drive_motor_B_PWM = 2;//6 const int shooter_front_motor =
 * 4;//3 const int shooter_back_motor = 3; //4 const int climbing_motor_PWM = 7;
 *
 * #else const int left_drive_motor_A_PWM = 3; const int right_drive_motor_A_PWM
 * = 1; const int shooter_front_motor = 4; const int shooter_back_motor = 6;
 * #endif
 *
 * // Joysticks const int operator_joystick = 3; const int right_stick = 2;
 * const int left_stick = 1;
 *
 * // Joystick Buttons
 *
 * // Driver Stick Prim const int arcade_button = 10; const int tank_button =
 * 11; const int slow_drive_button_prim = 4;
 *
 * /*
 * Assigned But not tested
 */
/*const int climber_off_button_A = 8;
 const int climber_off_button_B = 9;
 const int climber_hold_down = 2;
 const int climber_hold_up = 3;
 const int climber_send_bottom = 7;
 const int climber_send_top = 6;
 /*
 * 
 */
/*
 // Driver Stick Sec
 const int slow_drive_button_sec = 4;

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
 #endif

 enum STATES
 {
 stabilizing, unstable, retracting, retract, fire
 };

 enum CLIMBER
 {
 send_up, go_up, go_down, stop, send_down
 };
 enum CLIMBER smart_climber_state = send_up;
 enum STATES smart_autonomous_state = unstable;

 //Solenoids
 const int SHOOTER_ANGLE_SOLENOID_1 = 3;
 const int SHOOTER_ANGLE_SOLENOID_2 = 4;
 #if YEAR_2013
 const int shooter_fire_piston_solenoid_A = 1;
 const int shooter_fire_piston_solenoid_B = 2;
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;

public class MotherSkill extends SimpleRobot {

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    final int left_drive_motor_A_PWM = 5;//1
    final int left_drive_motor_B_PWM = 6;//2
    final int right_drive_motor_A_PWM = 1;//5
    final int right_drive_motor_B_PWM = 2;//6
    RobotDrive OurRobot;
    Joystick RightJoystick,Leftjoystick;

    MotherSkill() {
        OurRobot = new RobotDrive(5, 6, 1, 2);
        OurRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        OurRobot.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        OurRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        OurRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        Leftjoystick = new Joystick(1);
        RightJoystick = new Joystick(2); 
    }

    public void autonomous() {
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl(GenericHID OurJoystick)
    {
        boolean switchToArcadeDrive = false;
        
        while (isOperatorControl()) {
            switchToArcadeDrive = OurJoystick.getRawButton(1);
            if (switchToArcadeDrive == true) {
             OurRobot.arcadeDrive(OurJoystick);   
            } else {
             OurRobot.tankDrive(Leftjoystick, RightJoystick);   
            }
        } 
    }
}