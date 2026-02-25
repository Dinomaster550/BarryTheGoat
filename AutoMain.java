/**Autonomous code*/
/**W Barry*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Moments;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


@Autonomous(name="Auto Main")
public class AutoMain extends LinearOpMode {
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
            
        }
