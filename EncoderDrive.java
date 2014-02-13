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
    final boolean debug = true;
    /*
     * Port Numbers
     */
    /* we think this is the 2013 config */
        //final int armPotAnalogChannel = 4; NO ARM POT!! raina and gary
    
    
    
    final int compressorPressureSwitch =  2;
    final int compressorRelayPort      = 14;
    
    final int solenoid1 = 1;
    final int solenoid2 = 2;
    final int pistonOut = 3;
    final int pistonIn  = 4;
    
    final int frontSwitchPort = 6;// 2013 : top sw is true when pushed
    final int backSwitchPort = 7;//  2013: bottom sw is false when pushed

    final int pickUpMotorPWM = 9;//9-5
    
    final int winchMotorPWM1 = 3; //3;//6 // pretend 2013 shooter motor
    final int winchMotorPWM2 = 4; //4;//7 // pretend 2013 shooter motor
    
    final int leftFrontMotorPWM = 5; //9= 2013 ports , 3 = 2014 ports
    final int rightFrontMotorPWM = 1; //2 , 1
    final int leftBackMotorPWM = 6; //6 , 4
    final int rightBackMotorPWM = 2; //1 , 2
    final int armMotorPWM = 7;         // 2013 climb
    
    
    final int LdriveEncode1 = 1;
    final int LdriveEncode2 = 2;
    final int RdriveEncode1 = 3; // see raina & gary
    final int RdriveEncode2 = 4;
    
    final int winchEncoder1 = 1;//4
    final int winchEncoder2 = 2;//5
   
    //final int victor1PWM = 4;
    final int shooterSwitchPort = 5;
    //Drive Stick Buttons
    
    
    final int shifterButton1 = 2;
    final int shifterButton2 = 3;
    
    final int collectorButton = 1;
    final int distanceResetButton = 11;
    final int driveToggleButton = 6;
    
    //Winch Stick Buttons
    
    final int armBackButton = 2;  // 2013 hook goes down
    final int armFrontButton = 3; // 2013 hook goes up
    final int winchReleaseButton = 1;
    final int winchPreset1 = 4; // was 2 2013 shooter 
    final int winchPreset2 = 5; // was 4 2013 shooter 
    
    // THE 2014 ROBOT SHOOTS OUT THE BACK, SAYS BEN AND JUSTIN TO RAINA AND GARY
    
    /*
    
    
    final int armPotAnalogChannel = 4;
    
  
    final int pistonOut = 3;
    final int pistonIn = 4;
    
    final int frontSwitchPort = 6;//8
    final int backSwitchPort = 7;//9
    final int pickUpMotorPWM = 5;//9-5
    final int winchMotorPWM1 = 3;//6
    final int winchMotorPWM2 = 4;//7
    final int leftFrontMotorPWM = 1; //9= 2013 ports , 3 = 2014 ports
    final int rightFrontMotorPWM = 3; //2 , 1
    final int leftBackMotorPWM = 2; //6 , 4
    final int rightBackMotorPWM = 4; //1 , 2
    
    * // THESE four ARE CORRECT PER Ben and Justin
    final int LdriveEncode1 = 3; 
    final int LdriveEncode2 = 4; 
    final int RdriveEncode1 = 1;
    final int RdriveEncode2 = 2;
    * 
    // NO WINCH ENCODERS  says Ben and Justin, to Raina and Gary
    * 
    final int solenoid1 = 1;
    final int solenoid2 = 2;
    //final int victor1PWM = 4;
    final int shooterSwitchPort = 5;
    //Drive Stick Buttons
    final int armBackButton = 2;
    final int armFrontButton = 3;
    final int shifterButton1 = 2;
    final int shifterButton2 = 3;
    final int collectorButton = 1;
    final int distanceResetButton = 11;
    final int driveToggleButton = 6;
    //Winch Stick Buttons
    final int winchReleaseButton = 1;
    final int winchPreset1 = 2;
    final int winchPreset2 = 4;
    * */
    /*
     * Declarations
     */
    private DigitalInput shooterSwitch = new DigitalInput(shooterSwitchPort);
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
    private SpeedController armMotor = new Victor(armMotorPWM);//10
    private RobotDrive drive = new RobotDrive(leftFrontMotor,
            leftBackMotor,
            rightFrontMotor,
            rightBackMotor);
    
    // private Relay spike = new Relay(2);
    private Encoder leftDriveEncoder = new Encoder(LdriveEncode1, LdriveEncode2);
    private Encoder rightDriveEncoder = new Encoder(RdriveEncode1,RdriveEncode2);
    //private Encoder winchEncoder = new Encoder(winchEncoder1, winchEncoder2);
    
    private Timer Timer = new Timer();
    private Timer autonTimer = new Timer();
    private Timer shooterTimer = new Timer();
    
    private Compressor compressor = new Compressor(compressorRelayPort, compressorPressureSwitch);
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
    double encoderSize = 250.0;
    boolean toggle = true;
    boolean driveChange = true;
    boolean shooterTimerInit = true;
    int shooterState = 2;
    int counter = 0;
    

    EncoderDrive() { // doing this here will make it effective for both auton and operatorcontrol

        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true); // raina & gary
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);

        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);


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
        
        goFront = winchStick.getRawButton(armFrontButton) && !frontArmSwitch.get(); // 2013 & 2014
        if (debug) {
            goBack = winchStick.getRawButton(armBackButton) && backArmSwitch.get();  // 2013 'bot
        } else {
            goBack = winchStick.getRawButton(armBackButton) && !backArmSwitch.get(); // 2014 'bot
        }
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
        winchMotor2.set(winchSpeed);

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
                winchControl(false, 0);
                autonDistance(insertAutonDistance);
            } else {
                if (autonTimer.get() >= waitTime) {
                    winchControl(false, 0);
                    autonDistance(insertAutonDistance);
                    autonTimer.reset();
                }

            }


            leftDriveEncoder.reset();
            rightDriveEncoder.reset();

        } catch (IOException e) {
            autonDistance(insertAutonDistance);
            winchControl(false, 0);
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
                if (shootSwitch) {
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
                if (!shootSwitch) {
                    shooterState = 0;
                    winchControl(true, 0);
                }
                break;
        }
    }

    public void dumbWinch() {
        double winchSpeed = winchStick.getZ();
        if (winchStick.getRawButton(winchPreset1)) {
            winchMotor.set(winchSpeed);
            winchMotor2.set(winchSpeed);
        } 
        
        else if (winchStick.getRawButton(winchPreset2)) {
            winchMotor.set(-winchSpeed);
            winchMotor2.set(-winchSpeed);
        } 
        
        else {
           
             winchMotor.set(0.0); 
             winchMotor2.set(0.0); // added raina & gary
        }
    }

    public void autonDistance(double distance) {
        /*
         * Moves robot in autonomous
         */
        double autoWC = 10.16; //four inches in centimeters
        double autoDEL = leftDriveEncoder.get() / encoderSize;
        double autoDER = rightDriveEncoder.get() / encoderSize;
        double autoDTL = (autoDEL * autoWC);
        double autoDTR = (autoDER * autoWC);

        while (autoDTL <= (distanceMove) && autoDTR <= (distance)) {
            autoDEL = leftDriveEncoder.get() / encoderSize;
            autoDER = rightDriveEncoder.get() / encoderSize;
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
        double distanceEncoder = leftDriveEncoder.get() / encoderSize;
        double distanceTravel = (distanceEncoder * wheelCircumference);
        double speed = distanceTravel / Timer.get();
        if (driveStick.getRawButton(distanceResetButton)) {
            leftDriveEncoder.reset();
            rightDriveEncoder.reset();
            distanceTravel = 0.0;
        }
        System.out.println("distance:  " + (distanceTravel + distanceMemory));
        System.out.println("speed:  " + speed);
        if (Timer.get() >= 5) {
            distanceMemory += distanceTravel;
            Timer.reset();
            leftDriveEncoder.reset();
            rightDriveEncoder.reset();
        }
    }

    public void autonomous() {
        /**
         * This function is called once each time the robot enters autonomous.
         */
        DriverStationLCD(1, "Autonomous is ON");
        System.out.println("Autonomous is ON");
        leftDriveEncoder.start();//encoders 250 ticks per turn
        Timer.start();
        getImgData();
        
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
        boolean operatorControl = isOperatorControl(); // raina and gary
        boolean enabled = isEnabled();                 // wonder why these are here.
        //stateTimer.reset();
        Timer.start();
        leftDriveEncoder.start();
        rightDriveEncoder.start();
        //winchMotor.set(0.2);
        System.out.println(operatorControl);
        System.out.println(enabled);
        //winchControl(true, shootSpeed);
        //System.out.println("true?" + (operatorControl && enabled));
        
       // while (operatorControl && enabled) {
        
        while (isOperatorControl() && isEnabled()) {  // see raina & gary
            DriverStationLCD(1, "Teleoperated is ON");
            
            //System.out.println(frontArmSwitch.get() +" "+ backArmSwitch.get()); // raina & gary
            
            //shooterControl((winchStick.getRawButton(1) && winchStick.getRawButton(4)),
             /*shooterSwitch.get()*///backArmSwitch.get(), winchStick.getRawButton(5));
             
            dumbWinch();
            shifter();  // enabled raina and gary - uses frisbee shooter piston
            armControl();
            //winchControl(driveStick.getRawButton(winchReleaseButton), winchStick.getY());
            //drive.arcadeDrive(driveStick);
            driveToggle();
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
            //  encoderDistances();
        }
    }

    public void test() {

        while (isTest() && isEnabled()) {
            DriverStationLCD(2, "front left motor: " + leftFrontMotor.get());
            DriverStationLCD(3, "front right motor: " + rightFrontMotor.get());
            DriverStationLCD(4, "back left motor: " + leftBackMotor.get());
            DriverStationLCD(5, "back right motor: " + rightBackMotor.get());
            System.out.println("test");
            DriverStationLCD(1, "Now in Test");

            double z_axis = driveStick.getZ();
            // buttons corrolate to physical positions of motors
            
            if (driveStick.getRawButton(6)) 
                leftFrontMotor.set(z_axis);
              else 
                leftFrontMotor.set(0.0);
            
            if (driveStick.getRawButton(7))
                leftBackMotor.set(z_axis);
            else 
                leftBackMotor.set(0.0);
               
            if (driveStick.getRawButton(10))
                rightFrontMotor.set(z_axis);
            else 
             rightFrontMotor.set(0.0);
           
            if (driveStick.getRawButton(11))
                rightBackMotor.set(z_axis);
            else
                rightBackMotor.set(0.0);
               
            if (driveStick.getRawButton(3))
                winchMotor.set(z_axis);

            else
                winchMotor.set(0.0);
            
            if (driveStick.getRawButton(4)) 
                winchMotor2.set(z_axis);
            else
                winchMotor2.set(0.0);
            
            
           
            if (driveStick.getRawButton(1)) 
                armMotor.set(z_axis);
             else 
                armMotor.set(0.0);
               
            
        }
    }
}
