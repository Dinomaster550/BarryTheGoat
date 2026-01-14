package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum Drive TeleOp", group = "Drive")
public class MecanumDriveTeleOp extends OpMode {

    // Declare motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // ===============================
    // EASY SPEED CONTROL (EDIT THIS)
    // ===============================
    private double speedMultiplier = 0.7; // Range: 0.0 – 1.0

    @Override
    public void init() {

        // Map motors from Robot Configuration
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        // Reverse left side motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Brake when power is zero
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {

        // ===============================
        // CONTROLLER INPUTS
        // ===============================
        double y  = -gamepad1.left_stick_y;   // Forward / Back
        double x  =  gamepad1.left_stick_x;   // Strafe
        double rx =  gamepad1.right_stick_x;  // Turn

        // ===============================
        // MECANUM DRIVE CALCULATIONS
        // ===============================
        double frontLeftPower  = y + x + rx;
        double backLeftPower   = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower  = y + x - rx;

        // ===============================
        // NORMALIZE MOTOR POWERS
        // ===============================
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

        // ===============================
        // APPLY SPEED MULTIPLIER
        // ===============================
        frontLeft.setPower(frontLeftPower * speedMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier);
        backLeft.setPower(backLeftPower * speedMultiplier);
        backRight.setPower(backRightPower * speedMultiplier);
    }
}
