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
// import com.sun.squawk.util.MathUtils;
import javax.microedition.io.*;
import java.io.*;

public class EncoderDrive extends SimpleRobot {

    /*
     * Port Numbers
     */
    final int armPotAnalogChannel = 4;
    final int winchMotorPWM = 3;
    final int pistonOut = 3;
    final int pistonIn = 4;
    final int frontSwitchPort = 3;
    final int backSwitchPort = 3;
    final int pickUpMotorPWM = 7;//9-5
    final int leftFrontMotorPWM = 5; //1
    final int rightFrontMotorPWM = 2; //2
    final int leftBackMotorPWM = 6; //9
    final int rightBackMotorPWM = 1; //10
    /*
     final int leftFrontMotorPWM = 1; //1
     final int rightFrontMotorPWM = 2; //2
     final int leftBackMotorPWM = 9; //9
     final int rightBackMotorPWM = 10; //10
     */
    final int driveEncode1 = 8;
    final int driveEncode2 = 9;
    final int winchEncoder1 = 4;
    final int winchEncoder2 = 5;
    final int solenoid1 = 1;
    final int solenoid2 = 2;
    final int victor1PWM = 4;
    final int shooterPotAnalogChannel = 5;
    //Drive Stick Buttons
    final int armBackButton = 5;
    final int armFrontButton = 4;
    final int shifterButton1 = 2;
    final int shifterButton2 = 3;
    final int collectorButton = 1;
    final int distanceResetButton = 11;
    final int driveToggleButton = 6;
    //Winch Stick Buttons
    final int winchReleaseButton = 1;
    final int winchPreset1 = 2;
    final int winchPreset2 = 4;
    /*
     * Declarations
     */
    private AnalogChannel shooterPot = new AnalogChannel(shooterPotAnalogChannel);
    private Joystick driveStick = new Joystick(1);
    private Joystick winchStick = new Joystick(3);
    private DigitalInput frontArmSwitch = new DigitalInput(frontSwitchPort);
    private DigitalInput backArmSwitch = new DigitalInput(backSwitchPort);
    private SpeedController pickUpMotor = new Jaguar(pickUpMotorPWM);
    private SpeedController leftFrontMotor = new Victor(leftFrontMotorPWM);
    private SpeedController rightFrontMotor = new Victor(rightFrontMotorPWM);
    private SpeedController leftBackMotor = new Victor(leftBackMotorPWM);
    private SpeedController rightBackMotor = new Victor(rightBackMotorPWM);
    private SpeedController winchMotor = new Jaguar(winchMotorPWM);
    private SpeedController armMotor = new Victor(victor1PWM);
    private RobotDrive drive = new RobotDrive(leftFrontMotor,
            rightFrontMotor,
            leftBackMotor,
            rightBackMotor);
    private Encoder leftDriveEncoder = new Encoder(driveEncode1, driveEncode2);
    private Encoder rightDriveEncoder = new Encoder(7, 12);
    private Encoder winchEncoder = new Encoder(winchEncoder1, winchEncoder2);
    private Timer Timer = new Timer();
    private Timer autonTimer = new Timer();
    private Solenoid shiftPiston1 = new Solenoid(solenoid1);
    private Solenoid shiftPiston2 = new Solenoid(solenoid2);
    private Solenoid winchPistonOut = new Solenoid(pistonOut);
    private Solenoid winchPistonIn = new Solenoid(pistonIn);
    final double inverter = -1.0;
    final double distanceMove = 152.4;
    final double autonDriveSpeed = .3;
    final double armLimitFront = .5;
    final double armLimitBack = -.5;
    double setPoint = 0;
    double currentPoint = 0;
    double armDifference = 0;
    double insertAutonDistance = 91.44; //3 feet in cm
    double wheelCircumference = .1;
    double distanceMemory = 0;
    double armSpeed = .5;
    boolean toggle = true;
    boolean driveChange = true;

    /*
     EncoderDrive() { // doing this here will make it effective for both auton and operatorcontrol

     drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
     drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
     drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     }
     */
    /**
     * Take pot value and converts it to joystick values.
     *
     * @param potValue double
     * @return potValue double
     */
    public double potConvert(double potValue) {
        /*
         * Converts pot value to Joystick values( -1 to 1 )
         */
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
        /*
         * moves arm to either front limit or back limit
         */
        boolean goFront = false;
        boolean goBack = true;
        if (goFront) {
            armDifference = armSpeed;
        }
        if (goBack) {
            armDifference = -armSpeed;
        }
        if (!(goFront || goBack)) {
            armDifference = 0;
        }
        //setPoint = driveStick.getZ() * inverter;

        armMotor.set(armDifference);


        // TO DO:
    }

    public void winchControl(boolean winchShift, double winchSpeed) {
        /*
         * Shifts solenoid + sets speed for winch motor
         */
        winchPistonOut.set(winchShift);
        winchPistonIn.set(!winchShift);

        winchMotor.set(winchSpeed);

    }

    public void shifter() {
        /*
         * Shifts solenoid for winch 
         */
        if (driveStick.getRawButton(shifterButton1)) {
            shiftPiston1.set(true);
            shiftPiston2.set(false);
        }
        if (driveStick.getRawButton(shifterButton2)) {
            shiftPiston1.set(false);
            shiftPiston2.set(true);
        }
    }

    public void activateCollector() {
        /*
         * Turns on collector
         */
        if (driveStick.getRawButton(collectorButton)) {
            pickUpMotor.set(1.0);
        }
    }

    public void getImgData() {
        /*
         * Sets up a socket client
         */
        autonTimer.start();
        try {

            SocketConnection sc = (SocketConnection) Connector.open("socket://10.26.43.5:1180");

            sc.setSocketOption(SocketConnection.LINGER, 5);

            InputStream is = sc.openInputStream();
            OutputStream os = sc.openOutputStream();


            byte[] lineBuffer = new byte[16];
            is.read(lineBuffer, 0, 16);
            String isHot = new String(lineBuffer);

            long waitTime = 5;
            boolean Heat = (isHot.indexOf("true") != -1);
            //System.out.println("echo: " + isHot);
            if (Heat) {
                winchPistonOut.set(true);
                winchPistonIn.set(false);
                autonDistance(insertAutonDistance);
            } else {
                if (autonTimer.get() >= waitTime) {
                    winchPistonOut.set(true);
                    winchPistonIn.set(false);
                    autonDistance(insertAutonDistance);
                    autonTimer.reset();
                }

            }


            leftDriveEncoder.reset();
            rightDriveEncoder.reset();






        } catch (IOException e) {
            System.err.println(e);
        }


    }

    public void shooterControl(double setPoint) {
        //Different buttons set the shooter motor to different speeds
        double shooterPotValue = potConvert(shooterPot.getValue());

        double shooterDifference = (setPoint - shooterPotValue) / 2;

        if (winchStick.getRawButton(winchPreset1)) 
        {
            setPoint = -0.75;
        }
        if (winchStick.getRawButton(winchPreset2)) 
        {
            setPoint = -0.5;
        }
        if (winchStick.getRawButton(5)) 
        {
            setPoint = 0.0;
        }

        if (shooterPotValue < setPoint) 
        {
            winchControl(true, shooterDifference);
        }
        if (shooterPotValue >= setPoint) 
        {
            winchMotor.set(0);
        }

    }

    public void autonDistance(double distance) {
        /*
         * Moves robot in autonomous
         */
        double autoWC = 2.54; //one inch in centimeters
        double autoDEL = leftDriveEncoder.get() / 360.0;
        double autoDER = rightDriveEncoder.get() / 360.0;
        double autoDTL = (autoDEL * autoWC);
        double autoDTR = (autoDER * autoWC);
        double distanceMove = distance;

        while (autoDTL <= (distanceMove) && autoDTR <= (distanceMove)) {
            autoDEL = leftDriveEncoder.get() / 360.0;
            autoDER = rightDriveEncoder.get() / 360.0;
            autoDTL = (autoDEL * autoWC);
            autoDTR = (autoDER * autoWC);

            leftFrontMotor.set(autonDriveSpeed);
            rightFrontMotor.set(-autonDriveSpeed);
            leftBackMotor.set(autonDriveSpeed);
            rightBackMotor.set(-autonDriveSpeed);
        }
        leftDriveEncoder.reset();
        rightDriveEncoder.reset();
        leftFrontMotor.set(0);
        rightFrontMotor.set(0);
        leftBackMotor.set(0);
        rightBackMotor.set(0);

    }

    public void driveToggle() {
        //It toggles between arcade drive and tank drive
        if (driveStick.getRawButton(11) && toggle) {
            driveChange = !driveChange;
            toggle = false;
        }
        if (!driveStick.getRawButton(11)) {
            toggle = true;
        }
        if (driveChange) {
            drive.arcadeDrive(driveStick);
        }
        if (!driveChange) {
            drive.tankDrive(driveStick, winchStick);
        }


    }

    public void encoderDistances() {
        /*
         * Prints distance traveled in total and resets encoder
         */
        double distanceEncoder = leftDriveEncoder.get() / 360.0;
        double distanceTravel = (distanceEncoder * wheelCircumference);
        double speed = distanceTravel / Timer.get();
        if (driveStick.getRawButton(distanceResetButton)) {
            leftDriveEncoder.reset();
            rightDriveEncoder.reset();
            distanceTravel = 0.0;
        }

        System.out.println("distance  " + (distanceTravel + distanceMemory));

        System.out.println("speed  " + speed);
        if (Timer.get() >= 5) {
            distanceMemory += distanceTravel;
            Timer.reset();
            leftDriveEncoder.reset();
            rightDriveEncoder.reset();
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void autonomous() {
        /*
         * Autonomous loop
         */
        System.out.println("Autonomous is ON");
        leftDriveEncoder.start();//encoders 360 ticks per turn
        Timer.start();
        getImgData();
        autonDistance(insertAutonDistance);
        winchControl(false, 0);

    }

    public void operatorControl() {
        /*
         * This is the operator control loop
         */
        System.out.println("works");

        /*
         winchPistonOut.set(true);
         winchPistonIn.set(false);
         */

        

        

        boolean operatorControl = isOperatorControl();
        boolean enabled = isEnabled();

        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);


        //stateTimer.reset();
        Timer.start();
        leftDriveEncoder.start();
        rightDriveEncoder.start();
        //winchMotor.set(0.2);
        System.out.println(operatorControl);
        System.out.println(enabled);
        while (operatorControl && enabled) {
            //shifter();
            //armControl();
            //winchControl(driveStick.getRawButton(winchReleaseButton), winchStick.getY());
            System.out.println("Works");

            //driveToggle();

            // winchMotor.set(0.3);
            // drive.arcadeDrive(driveStick);
            // winchControl(winchStick.getRawButton(winchReleaseButton), winchStick.getY());
            activateCollector();


            //  encoderDistances();

            /*climbspeed = climberstick.getY();
             
             System.out.println("JS3 Y= " + climbspeed);
             
             climbing_motor.set(climbspeed);
             
             
            
             if (driveStick.getTrigger()) {
             pickUpMotor.set(driveStick.getZ());
             }
             
             System.out.println(potConvert(armPot.getValue()));
             */
        }

    }
}
