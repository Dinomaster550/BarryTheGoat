@TeleOp(name = "Mecanum Drive TeleOp", group = "Drive")
public class MecanumDriveTeleOp extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // 🔽 EASY SPEED CONTROL HERE 🔽
    private double speedMultiplier = 0.7; // change this while testing

    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {

        double y  = -gamepad1.left_stick_y;
        double x  =  gamepad1.left_stick_x;
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

        // 🔽 APPLY SPEED MULTIPLIER 🔽
        frontLeft.setPower(frontLeftPower * speedMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier);
        backLeft.setPower(backLeftPower * speedMultiplier);
        backRight.setPower(backRightPower * speedMultiplier);
    }
}
