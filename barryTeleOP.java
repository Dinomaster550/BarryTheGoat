package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Moments;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@TeleOp(name="DecodeTeleOpFull")
public class DecodeTeleOpFull extends LinearOpMode {

    // Drivetrain motors
    private DcMotorEx fl, fr, bl, br;
    private DcMotor intake;
    private DcMotorEx flywheel;
    private Servo turret;

    // Turret PID
    private double turretPosition = 0.5;
    private final double turretKp = 0.002;

    // Flywheel PIDF for velocity control
    private final double flywheelTargetRPM = 3000; // adjust to your shooting speed
    private final double flywheelF = 12.0 / flywheelTargetRPM; // rough feedforward

    // OpenCV
    private OpenCvCamera webcam;
    private GoalPipeline pipeline;

    @Override
    public void runOpMode() throws InterruptedException {

    
        fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        fr = hardwareMap.get(DcMotorEx.class, "frontRight");
        bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        br = hardwareMap.get(DcMotorEx.class, "backRight");

        intake = hardwareMap.get(DcMotor.class, "intake");
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        turret = hardwareMap.get(Servo.class, "turret");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       
        int camViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance()
                .createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), camViewId);

        pipeline = new GoalPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) { }
        });

        telemetry.addLine("Initialized!");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double denom = Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);

            fl.setPower((y + x + rx)/denom);
            bl.setPower((y - x + rx)/denom);
            fr.setPower((y - x - rx)/denom);
            br.setPower((y + x - rx)/denom);

       
            intake.setPower(1.0); // default ON
            // if(gamepad1.left_bumper) intake.setPower(-1.0); // reverse if jam
            //IMPORTANT! Line above is commented to not use left bumper(Used in turret for now)
            
            double flyPower = gamepad1.right_trigger;
            flywheel.setVelocity(flyPower * flywheelTargetRPM);

            //Turret servo
            if (gamepad1.left_bumper) {
                turretPosition -= turretKp;
            }

            if (gamepad1.right_bumper) {
                turretPosition -= turretKp;
            }
            
            // Auto track using vision pipeline
            double error = pipeline.getTargetX() - 320; // center of 640px
            turretPosition -= error * turretKp;

            // Driver override
            if(Math.abs(gamepad1.right_stick_x) > 0.1){
                turretPosition += gamepad1.right_stick_x * 0.01;
            }

            turretPosition = Math.max(0.0, Math.min(1.0, turretPosition));
            turret.setPosition(turretPosition);

        
            telemetry.addData("Target X", pipeline.getTargetX());
            telemetry.addData("Turret Pos", turretPosition);
            telemetry.addData("Flywheel RPM", flywheel.getVelocity());
            telemetry.update();
        }
    }


    class GoalPipeline extends OpenCvPipeline {

        private double targetX = 320;

        public double getTargetX() { return targetX; }

        @Override
        public Mat processFrame(Mat input) {

            Mat hsv = new Mat();
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            // Example: Yellow goal detection
            Scalar lower = new Scalar(20, 100, 100);
            Scalar upper = new Scalar(30, 255, 255);

            Mat mask = new Mat();
            org.opencv.core.Core.inRange(hsv, lower, upper, mask);

            Moments moments = Imgproc.moments(mask);

            if(moments.m00 != 0){
                targetX = moments.m10 / moments.m00;
            }

            return mask;
        }
    }
}
