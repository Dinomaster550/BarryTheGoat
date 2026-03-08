package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;

@TeleOp(name = "TeleOp 2026")
public class TeleOp2026 extends OpMode{

    AprilTagWebcam turretCam, sideCam;

    private Blinker control_Hub;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    private DcMotorEx accelMotor;
    private CRServo turret;

    private IMU imu;

    private boolean active = true;

    private double rotatedFromIdentity;

    private int goalTagID = 0;
    private int centerTagID = 1;
    private int OppTagID = 2;

    private double driveSpeed = 2;

    @Override
    public void init(){

        turretCam = new AprilTagWebcam();
        sideCam = new AprilTagWebcam();
        
        hardwareInit(hardwareMap);
        telemetry.addLine("Hardware Initialised");
        turretCam.init(hardwareMap, telemetry,"Webcam 1");
        sideCam.init(hardwareMap, telemetry, "Webcam 2");
        telemetry.addLine("Cameras Initialised");
        

        telemetry.addLine("Press to start...");
    }

    @Override
    public void loop(){

        if (gamepad1.dpadDownWasPressed()){active = !active;}

        if (active){

            turretCam.update();
            turretUpdate();
            rotatedFromIdentity = getRotation();
            
    
        
        }
        else{telemetry.addLine("Press Dpad-Down to activate");}
    }

    public void hardwareInit(HardwareMap hdwr){
        turret = hdwr.get(CRServo.class, "servo1");

        leftFrontMotor = hdwr.get(DcMotor.class, "lf");
        leftBackMotor = hdwr.get(DcMotor.class, "lb");
        rightFrontMotor = hdwr.get(DcMotor.class, "rf");
        rightBackMotor = hdwr.get(DcMotor.class, "rb");

        imu = hardwareMap.get(IMU.class, "IMU");
        imu.resetYaw();
        
    }
    
    public void turretUpdate(){
        AprilTagDetection goalTag = turretCam.getTagBySpecificId(goalTagID);
        
        if(goalTag != null){
            double power = -goalTag.ftcPose.bearing/10;
            turret.setPower(power);
            telemetry.addData("Servo Power", power);
        }   
    }
    
    public double getRotation(){
        AprilTagDetection centerTag = sideCam.getTagBySpecificId(centerTagID);
        AprilTagDetection goalTag = sideCam.getTagBySpecificId(goalTagID);
        AprilTagDetection OppTag = sideCam.getTagBySpecificId(OppTagID);

        if (centerTag == null && goalTag == null && OppTag == null){

        }

        ArrayList<Double> angles = new ArrayList<Double>();

        if(centerTag != null){
            angles.add(centerTag.ftcPose.bearing - Math.PI/2);
        }
        
        
        if(goalTag != null){
            angles.add(goalTag.ftcPose.bearing - Math.PI/4);
        }

        
        if(OppTag != null){
            angles.add(OppTag.ftcPose.bearing - Math.PI*3/4);
        }

        double totalAngle = 0;
        for (int i = 0; i < angles.size(); i++) {
            totalAngle += angles.get(i);
        }

        return (totalAngle/angles.size());
    }
}
