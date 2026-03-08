package org.firstinspires.ftc.teamcode.mechanisms;
import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.*;
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AprilTagWebcam {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public Telemetry telemetry;
    public void init(HardwareMap hwMap, Telemetry telemetry, String CameraName) {
        this.telemetry = telemetry;

        AprilTagLibrary myLibrary = new AprilTagLibrary.Builder()
            .addTag(0, "Tag 0", 0.0508, DistanceUnit.CM)
            .addTag(1, "Tag 1", 0.0508, DistanceUnit.CM)
            .addTag(2, "Tag 2", 0.0508, DistanceUnit.CM)
            .build();

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(myLibrary)
                .setLensIntrinsics(578,578,320,240)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.RADIANS)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, CameraName));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    public void update() {
            detectedTags = aprilTagProcessor.getDetections();
        }

    public List<AprilTagDetection> getDetectedTags() {
            return detectedTags;
        }

    public void displayDetectionTelemetry(AprilTagDetection detectedId) {
        if (detectedId ==null) {return;}
        if (detectedId.metadata != null)
        {
            telemetry.addLine(String.format("\n==== (ID %d) %s", detectedId.id, detectedId.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (cm)", detectedId.ftcPose.x, detectedId.ftcPose.y, detectedId.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectedId.ftcPose.pitch, detectedId.ftcPose.roll, detectedId.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (cm, deg, deg)", detectedId.ftcPose.range, detectedId.ftcPose.bearing, detectedId.ftcPose.elevation));
        } 
        else 
        {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectedId.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectedId.center.x, detectedId.center.y));
        }
    } 


    public AprilTagDetection getTagBySpecificId(int id) {
            for (AprilTagDetection detection : detectedTags) {
                if (detection.id == id) {
                    return detection;
                }
            }
            return null;
        }

    public void stop() {
            if (visionPortal != null) {
                visionPortal.close();
            }
        }


    }
