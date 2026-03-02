package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@TeleOp(name = "Mecanum Drive TeleOp", group = "Drive")
public class NewMain extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor intakeMotor;
    private DcMotor flywheelMotor;
    
    private Servo turretServo;

    private double speedMultiplier = 0.7;

    private double flywheelPower = 1.0; 
    private double flywheelCurrentPower = 0.0;
    private double flywheelTargetPower = 0.0;
    private double flywheelAccel = 0.00375;
    
    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        intakeMotor   = hardwareMap.get(DcMotor.class, "intakeMotor");
        flywheelMotor = hardwareMap.get(DcMotor.class, "flywheelMotor");
        
        turretServo = hardwareMap.get(Servo.class, "turretServo");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
    }

    @Override
    public void loop() {

            double y  = gamepad1.left_stick_y;
            double x  =  -gamepad1.left_stick_x;
            double rx =  gamepad1.right_stick_x;
        
        double frontLeftPower  = y + x + rx;
        double backLeftPower   = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower  = y + x - rx;

        double max = Math.max(
                Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))
        );

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        frontLeft.setPower(frontLeftPower * speedMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier);
        backLeft.setPower(backLeftPower * speedMultiplier);
        backRight.setPower(backRightPower * speedMultiplier);
        
        boolean reverseIntake = gamepad1.a;
        if (reverseIntake) {
            intakeMotor.setPower(-(gamepad1.left_trigger));
        } else {
            intakeMotor.setPower(gamepad1.left_trigger);
        }
        // Flywheel code
        if (gamepad1.right_trigger > 0.1) {
            flywheelTargetPower = gamepad1.right_trigger;
        } else {
            flywheelTargetPower = 0.0;
        }
        
        if (flywheelCurrentPower < flywheelTargetPower) {
            flywheelCurrentPower += flywheelAccel;
        } else if (flywheelCurrentPower > flywheelTargetPower) {
            flywheelCurrentPower -= flywheelAccel;
        }

        flywheelCurrentPower = Math.max(0.0, Math.min(1.0, flywheelCurrentPower));
        flywheelMotor.setPower(flywheelCurrentPower);
        
        
        // Turret code
        double turretPos = turretServo.getPosition();

        if (gamepad1.left_bumper) {
            turretPos += 0.0025;
        }
        if (gamepad1.right_bumper) {
            turretPos -= 0.0025;
        }
        turretServo.setPosition(turretPos);
    }
}
