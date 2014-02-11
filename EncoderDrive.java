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
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Relay;
// import com.sun.squawk.util.MathUtils;
import javax.microedition.io.*;
import java.io.*;

public class EncoderDrive extends SimpleRobot {
    //preset booleans

    final boolean automaticRetract = false;
    /*
     * Port Numbers
     */
    final int armPotAnalogChannel = 4;
    final int pistonOut = 3;
    final int pistonIn = 4;
    final int frontSwitchPort = 3;
    final int backSwitchPort = 11;//3
    final int pickUpMotorPWM = 5;//9-5
    final int winchMotorPWM1 = 3;//6
    final int winchMotorPWM2 = 4;//7
    final int leftFrontMotorPWM = 9; //9= 2013 ports , 3 = 2014 ports
    final int rightFrontMotorPWM = 2; //2 , 1
    final int leftBackMotorPWM = 6; //6 , 4
    final int rightBackMotorPWM = 1; //1 , 2
    final int driveEncode1 = 8;
    final int driveEncode2 = 9;
    final int winchEncoder1 = 1;//4
    final int winchEncoder2 = 2;//5
    final int solenoid1 = 1;
    final int solenoid2 = 2;
    //final int victor1PWM = 4;
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
    private DigitalInput shooterSwitch = new DigitalInput(shooterPotAnalogChannel);
    private Joystick driveStick = new Joystick(1);
    private Joystick winchStick = new Joystick(3);
    private DigitalInput frontArmSwitch = new DigitalInput(frontSwitchPort);
    private DigitalInput backArmSwitch = new DigitalInput(backSwitchPort);
    // private DigitalInput pressureSwitch = new DigitalInput(14);
    private SpeedController pickUpMotor = new Jaguar(pickUpMotorPWM);
    private SpeedController leftFrontMotor = new Victor(leftFrontMotorPWM);
    private SpeedController rightFrontMotor = new Victor(rightFrontMotorPWM);
    private SpeedController leftBackMotor = new Victor(leftBackMotorPWM);
    private SpeedController rightBackMotor = new Victor(rightBackMotorPWM);
    private SpeedController winchMotor = new Talon(winchMotorPWM1);
    //placeholder motors
    //private SpeedController winchMotor1 = new Talon(winchMotorPWM);
    private SpeedController winchMotor2 = new Talon(winchMotorPWM2);
    private SpeedController armMotor = new Victor(7);//10
    private RobotDrive drive = new RobotDrive(leftFrontMotor,
            leftBackMotor,
            rightFrontMotor,
            rightBackMotor);
    // private Relay spike = new Relay(2);
    private Encoder leftDriveEncoder = new Encoder(driveEncode1, driveEncode2);
    private Encoder rightDriveEncoder = new Encoder(7, 12);
    private Encoder winchEncoder = new Encoder(winchEncoder1, winchEncoder2);
    private Timer Timer = new Timer();
    private Timer autonTimer = new Timer();
    private Timer shooterTimer = new Timer();
    private Compressor compressor = new Compressor(14, 2);
    private Solenoid shiftPiston1 = new Solenoid(solenoid1);
    private Solenoid shiftPiston2 = new Solenoid(solenoid2);
    private Solenoid winchPistonOut = new Solenoid(pistonOut);
    private Solenoid winchPistonIn = new Solenoid(pistonIn);
    final double inverter = -1.0;
    final double distanceMove = 152.4;
    final double autonDriveSpeed = .3;
    final double armLimitFront = .5;
    final double armLimitBack = -.5;
    final double shootSpeed = .75;
    double setPoint = 0;
    double currentPoint = 0;
    double armDifference = 0;
    double insertAutonDistance = 91.44; //3 feet in cm
    double wheelCircumference = .1;
    double distanceMemory = 0;
    double armSpeed = .5;
    boolean toggle = true;
    boolean driveChange = true;
    boolean shooterTimerInit = true;
    int shooterState = 2;
    int counter = 0;

    EncoderDrive() { // doing this here will make it effective for both auton and operatorcontrol


        compressor.start();
        // spike.set(Relay.Value.kOn);
    }

    /**
     * Take pot value and converts it to joystick values.
     *
     * @param potValue double
     * @return potValue double
     */
    public void armControl() {
        /*
         * moves arm to either front limit or back limit
         */
        boolean goFront = false;
        boolean goBack = true;
        goFront = winchStick.getRawButton(armFrontButton) && !frontArmSwitch.get();
        goBack = winchStick.getRawButton(armBackButton) && !backArmSwitch.get();
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

    public void shooterControl(boolean shootButton, boolean shootSwitch, boolean shootButton2) {
        //state machine for shooter 0 = pulled back 1= shooting 2= pulling back

        switch (shooterState) {
            case 0://pulled back
                if (shootButton) {
                    shooterState = 1;
                    winchControl(false, 0);
                    shooterTimer.reset();
                    shooterTimer.start();
                    break;
                }
                if (!shootSwitch) {
                    shooterState = 2;
                    winchControl(true, shootSpeed);
                    break;
                }
                break;
            case 1://shooting
                if (automaticRetract) {
                    if (shooterTimer.get() >= 1.0) {
                        shooterState = 2;
                        winchControl(true, shootSpeed);
                    }
                } else {
                    if (shootButton2) {
                        shooterState = 2;
                        winchControl(true, shootSpeed);
                    }
                }
                break;
            case 2://pulling back
                if (shootSwitch) {
                    shooterState = 0;
                    winchControl(true, 0);
                }
                break;
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

    public void DriverStationLCD(int i, String k) {

        DriverStationLCD.Line line = DriverStationLCD.Line.kUser1;

        if (i == 1) {
            line = DriverStationLCD.Line.kUser1;
        }
        if (i == 2) {
            line = DriverStationLCD.Line.kUser2;
        }
        if (i == 3) {
            line = DriverStationLCD.Line.kUser3;
        }
        if (i == 4) {
            line = DriverStationLCD.Line.kUser4;
        }
        if (i == 5) {
            line = DriverStationLCD.Line.kUser5;
        }

        DriverStationLCD.getInstance().println(line, 2, k);
        DriverStationLCD.getInstance().updateLCD();


        //println(DriverStationLCD.Line line, int startingColumn, StringBuffer text);
    }

    public void driveToggle() {
        //It toggles between arcade drive and tank drive

        if (driveStick.getRawButton(driveToggleButton) && toggle) {
            driveChange = !driveChange;
            toggle = false;
        }
        if (!driveStick.getRawButton(driveToggleButton)) {
            toggle = true;
        }
        if (driveChange) {
            drive.arcadeDrive(driveStick);
            DriverStationLCD(2, "Now in Arcade Drive");
        }
        if (!driveChange) {
            drive.tankDrive(driveStick, winchStick);
            DriverStationLCD(2, "Now in Tank Drive");
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
     * This function is called once each time the robot enters autonomous.
     */
    public void autonomous() {
        /*
         * Autonomous loop
         */
        DriverStationLCD(1, "Autonomous is ON");
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
        DriverStationLCD(1, "Teleoperated is ON");

        /*
         winchPistonOut.set(true);
         winchPistonIn.set(false);
         */

        boolean operatorControl = isOperatorControl();
        boolean enabled = isEnabled();

        //stateTimer.reset();
        Timer.start();
        leftDriveEncoder.start();
        rightDriveEncoder.start();
        //winchMotor.set(0.2);
        System.out.println(operatorControl);
        System.out.println(enabled);
        //winchControl(true, shootSpeed);
        //System.out.println("true?" + (operatorControl && enabled));

        while (operatorControl && enabled) {
            DriverStationLCD(1, "Teleoperated is ON");

            drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
            drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
            drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);


            //shifter();
            //armControl();
            //winchControl(driveStick.getRawButton(winchReleaseButton), winchStick.getY());
            //drive.arcadeDrive(driveStick);

            // driveToggle();

            // winchMotor.set(0.3);


            // winchControl(winchStick.getRawButton(winchReleaseButton), winchStick.getY());
            //activateCollector();
            //System.out.println("This is working");

            /*
             leftBackMotor.set(driveStick.getY());
             leftFrontMotor.set(driveStick.getY());
             rightBackMotor.set(-winchStick.getY());
             rightFrontMotor.set(-winchStick.getY());
             */
            drive.arcadeDrive(driveStick);

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

    public void test() {

        while (isTest() && isEnabled()) {

            System.out.println("test");
            DriverStationLCD(2, "Now in Test");

            double z_axis = driveStick.getZ();

            if (driveStick.getRawButton(6)) {
                leftFrontMotor.set(z_axis);
            } else {
                leftFrontMotor.set(0.0);
            }
            if (driveStick.getRawButton(7)) {
                leftBackMotor.set(z_axis);
            } else {
                leftBackMotor.set(0.0);
            }
            if (driveStick.getRawButton(10)) {
                rightFrontMotor.set(z_axis);
            } else {
                rightFrontMotor.set(0.0);
            }
            if (driveStick.getRawButton(11)) {
                rightBackMotor.set(z_axis);
            } else {
                rightBackMotor.set(0.0);
            }
            if(driveStick.getRawButton(3)){
                winchMotor.set(z_axis);
                
            }
            else{
                winchMotor.set(0.0);
            }
            if(driveStick.getRawButton(4)){
                winchMotor2.set(z_axis);
                
            }
            else{
                winchMotor2.set(0.0);
            }
            if(driveStick.getRawButton(1)){
                armMotor.set(z_axis);
                
            }
            else{
                armMotor.set(0.0);
            }


//        System.out.println(pressureSwitch.get());


        }
    }
}
