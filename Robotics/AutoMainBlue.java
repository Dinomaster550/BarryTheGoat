/**Autonomous code*/
/**W Barry*/

// i pasted in the AutoMainRed for now here but blue is just the strafe and rotate amounts but opposite sign

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Auto Main")
public class AutoMain extends LinearOpMode {
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private DcMotor intakeMotor;
    private DcMotorEx flywheelMotor;
    private Servo turretServo;

    // Turret PID
    private double turretPosition = 0.5;
    private final double turretKp = 0.002;

    // Flywheel PIDF for velocity control
    private final double flywheelTargetRPM = 3000;
    private final double flywheelF = 12.0 / flywheelTargetRPM;
    
    static final double TICKS_PER_REV = 28;
    static final double WHEEL_DIAMETER = 104; //Millimeters
    static final double GEAR_RATIO = 20;
    
    static final double TICKS_PER_REV_FLYWHEEL =

    public void setFlywheelRPM(double rpm) {
    double ticksPerSecond = (rpm / 60.0) * TICKS_PER_REV_FLYWHEEL;
    flywheelMotor.setVelocity(ticksPerSecond);
}

    public void encoderDrive(double speed, double distanceMM) {

        int ticks = (int) (
                (distanceMM / (Math.PI * WHEEL_DIAMETER))
                * TICKS_PER_REV
                * GEAR_RATIO
        );

        int frontLeftTarget = frontLeft.getCurrentPosition() + ticks;
        int frontRightTarget = frontRight.getCurrentPosition() + ticks;
        int backLeftTarget = backLeft.getCurrentPosition() + ticks;
        int backRightTarget = backRight.getCurrentPosition() + ticks;

        frontLeft.setTargetPosition(frontLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backLeft.setTargetPosition(backLeftTarget);
        backRight.setTargetPosition(backRightTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (opModeIsActive() &&
            (frontLeft.isBusy() || frontRight.isBusy() ||
            backLeft.isBusy() || backRight.isBusy())) {

            telemetry.addData("FL", frontLeft.getCurrentPosition());
            telemetry.addData("FR", frontRight.getCurrentPosition());
            telemetry.update();
        }

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}

    public void strafe(double speed, double distanceMM) {

    int ticks = (int) ((distanceMM / (Math.PI * WHEEL_DIAMETER)) * TICKS_PER_REV * GEAR_RATIO);

    frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
    frontRight.setTargetPosition(frontRight.getCurrentPosition() - ticks);
    backLeft.setTargetPosition(backLeft.getCurrentPosition() - ticks);
    backRight.setTargetPosition(backRight.getCurrentPosition() + ticks);

    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    frontLeft.setPower(Math.abs(speed));
    frontRight.setPower(Math.abs(speed));
    backLeft.setPower(Math.abs(speed));
    backRight.setPower(Math.abs(speed));


    while (opModeIsActive() &&
        (frontLeft.isBusy() || frontRight.isBusy() ||
        backLeft.isBusy() || backRight.isBusy())) {

        telemetry.addData("FL", frontLeft.getCurrentPosition());
        telemetry.addData("FR", frontRight.getCurrentPosition());
        telemetry.update();
    }

    frontLeft.setPower(0);
    frontRight.setPower(0);
    backLeft.setPower(0);
    backRight.setPower(0);

    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}

    public void rotate(double speed, double angleDegrees) {

    double robotWidth = 359;
    double wheelDistance = (robotWidth * Math.PI) * (angleDegrees / 360.0);

    int ticks = (int)((wheelDistance / (Math.PI * WHEEL_DIAMETER)) * TICKS_PER_REV * GEAR_RATIO);

    frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
    frontRight.setTargetPosition(frontRight.getCurrentPosition() - ticks);
    backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);
    backRight.setTargetPosition(backRight.getCurrentPosition() - ticks);

    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    frontLeft.setPower(Math.abs(speed));
    frontRight.setPower(Math.abs(speed));
    backLeft.setPower(Math.abs(speed));
    backRight.setPower(Math.abs(speed));

    while (opModeIsActive() &&
        (frontLeft.isBusy() || frontRight.isBusy() ||
        backLeft.isBusy() || backRight.isBusy())) {

        telemetry.addData("FL", frontLeft.getCurrentPosition());
        telemetry.addData("FR", frontRight.getCurrentPosition());
        telemetry.update();
    }

    frontLeft.setPower(0);
    frontRight.setPower(0);
    backLeft.setPower(0);
    backRight.setPower(0);

    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}

    @Override
    public void runOpMode() throws InterruptedException {

    
        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight  = hardwareMap.get(DcMotorEx.class, "backRight");

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        turretServo = hardwareMap.get(Servo.class, "turretServo");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0, 0, 0, flywheelF); 
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        
        double driveSpeed = 0.6; 
        double slowSpeed = 0.3;

        telemetry.addLine("Initialized!");
        telemetry.update();
        waitForStart();

        if(opModeIsActive()) {
            //there are 3 helper functions
            //encoderDrive(speed, distanceMM) -ve is backwards, +ve is forwards
            //strafe(speed, distanceMM) -ve is left, +ve is right
            //rotate(speed, angleDegrees) -ve is counterclockwise,  +ve is clockwise
            
            //MAKE THE ROBOT:
            //1. Move 1981mm forwards
            //2.Rotate 45degrees ccw
            //3.At the same time rev the flywheel(threads)
            //4.Spin intake to shoot balls (after turn off flywheel)
            //5.Return to collect 2nd set of balls(rotate 135, then strafe -609.5, then forwards 1219, then rotate 90 cw, then strafe -125.25 then drive forwards 609.5 fowards, then drive forwards slower 609.5 while threading intake, strafe 125.25, rotate 180, drive 609.5 forward, then rotate -90, then drive 1828.5 forward, then Rotate -45 and at the same time rev the flywheel(threads), Spin intake to shoot balls (after turn off flywheel))
            //6.Strafe 609.5 to get off launching line
            //tip: in threads check for the end to not move while it is done
            //and then we good for auto
            
            //code below:
            
            
            encoderDrive(driveSpeed, 1981);


            Thread revFlywheel1 = new Thread(() -> {
                setFlywheelRPM(flywheelTargetRPM); 
                try { Thread.sleep(1500); } catch (InterruptedException e) {} 
            });
            revFlywheel1.start();
            
            rotate(0.5, -45);
            

            while(opModeIsActive() && revFlywheel1.isAlive()) {
                sleep(10);
            }


            intakeMotor.setPower(1.0);
            sleep(2000); 
            intakeMotor.setPower(0);
            setFlywheelRPM(0);


            rotate(0.5, 135);
            strafe(driveSpeed, -609.5);
            encoderDrive(driveSpeed, 1219);
            rotate(0.5, 90);
            strafe(driveSpeed, -125.25);
            encoderDrive(driveSpeed, 609.5);


            Thread runIntake = new Thread(() -> {
                intakeMotor.setPower(0.7);
                try { Thread.sleep(2500); } catch (InterruptedException e) {}
            });
            runIntake.start();
            
            encoderDrive(slowSpeed, 609.5); 
            

            while(opModeIsActive() && runIntake.isAlive()) {
                sleep(10);
            }
            intakeMotor.setPower(0); 


            strafe(driveSpeed, 125.25);
            rotate(0.5, 180);
            encoderDrive(driveSpeed, 609.5);
            rotate(0.5, -90);
            encoderDrive(driveSpeed, 1828.5);


            Thread revFlywheel2 = new Thread(() -> {
                setFlywheelRPM(flywheelTargetRPM);
                try { Thread.sleep(1500); } catch (InterruptedException e) {}
            });
            revFlywheel2.start();
            
            rotate(0.5, -45);
            
            while(opModeIsActive() && revFlywheel2.isAlive()) {
                sleep(10);
            }


            intakeMotor.setPower(1.0);
            sleep(2000); 
            intakeMotor.setPower(0);
            setFlywheelRPM(0);
            

            telemetry.addLine("Parking...");
            telemetry.update();
            strafe(driveSpeed, 609.5);
            
            telemetry.addLine("Autonomous Complete!");
            telemetry.update();
        }
    }
}