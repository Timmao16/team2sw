package org.firstinspires.ftc.teamcode.competitionopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.BlueCarouselRunnerV2;
import org.firstinspires.ftc.teamcode.autonomous.BlueCorner;
import org.firstinspires.ftc.teamcode.autonomous.BlueHomeRunner;
import org.firstinspires.ftc.teamcode.autonomous.BlueMiddle;
import org.firstinspires.ftc.teamcode.autonomous.IAutonomousRunner;
import org.firstinspires.ftc.teamcode.autonomous.RedCarouselRunnerV2;
import org.firstinspires.ftc.teamcode.autonomous.RedCorner;
import org.firstinspires.ftc.teamcode.autonomous.RedHomeRunnerV1;
import org.firstinspires.ftc.teamcode.autonomous.RedMiddle;
import org.firstinspires.ftc.teamcode.wrappers.ArmWrapper;
import org.firstinspires.ftc.teamcode.wrappers.DrivingWrapper;
import org.firstinspires.ftc.teamcode.wrappers.OpenCvDetection;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.testopmodes.AprilTagAutonomousInitDetectionExample;

import org.firstinspires.ftc.teamcode.testopmodes.VuforiaWebcamLocalization;


public class AutonomousWrapper {



    SampleMecanumDrive roadRunner;
    DrivingWrapper driver;
    ArmWrapper arm;
    double speed = .25; //Unused
    double rotSpeed = .25; //Unused
    HardwareMap hardwareMap;
    Telemetry telemetry;

    public DcMotor crMotor;

    public OpenCvDetection OpenCVWrapper;
    public AprilTagAutonomousInitDetectionExample initDetection;

    SampleMecanumDrive drive;

    IAutonomousRunner runner = null;

    int signalInt;



    public AutonomousWrapper(HardwareMap map, Telemetry inTelemetry, VuforiaWebcamLocalization.ELocation location,LinearOpMode opMode){
        hardwareMap = map;
        telemetry = inTelemetry;
        roadRunner = new SampleMecanumDrive(hardwareMap);
        crMotor  = hardwareMap.get(DcMotor.class, "carouselMotor");
        init(location,opMode);
    }

    public void init(VuforiaWebcamLocalization.ELocation location,LinearOpMode opMode){



        driver = new DrivingWrapper(hardwareMap,telemetry);
        arm = new ArmWrapper(hardwareMap, telemetry);


        initDetection = new AprilTagAutonomousInitDetectionExample(telemetry,hardwareMap,this,opMode);
        OpenCVWrapper = new OpenCvDetection(telemetry, hardwareMap);


//        OpenCVWrapper.init(true);
        initDetection.run(true);

        runner = null;

        drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addData("barcode", OpenCVWrapper.barcodeInt);
        if(location == VuforiaWebcamLocalization.ELocation.BLUECAROUSEL){
            runner = new BlueCarouselRunnerV2(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.BLUEHOME) {
            runner = new BlueHomeRunner(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.BLUECORNER) {
            runner = new BlueCorner(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.BLUEMIDDLE) {
            runner = new BlueMiddle(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDCAROUSEL) {
            runner = new RedCarouselRunnerV2(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDHOME) {
            runner = new RedHomeRunnerV1(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDCORNER) {
            runner = new RedCorner(drive, arm, opMode, this);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDMIDDLE) {
            runner = new RedMiddle(drive, arm, opMode, this);
        }
    }
    public void RunAutonomous(VuforiaWebcamLocalization.ELocation location, LinearOpMode opMode){


//        telemetry.addData("Location" ,location.toString());
//        telemetry.addData("Barcode", OpenCVWrapper.barcodeInt);




        telemetry.update();


        arm.init(false);



        runner.run();




        /*
        arm.init(false);
        opMode.sleep(1000);
        arm.SetLevel(OpenCVWrapper.barcodeInt);
        opMode.sleep(1000);
        driver.AutonomousDrive(DrivingWrapper.Direction.FORWARD, .4, speed);
        opMode.sleep(1700);
        driver.AutonomousDriveStop();


        if(location == VuforiaWebcamLocalization.ELocation.BLUECAROUSEL){
            driver.AutonomousDrive(DrivingWrapper.Direction.SPINLEFT, .9, rotSpeed);

        }else if(location == VuforiaWebcamLocalization.ELocation.BLUEHOME) {
            driver.AutonomousDrive(DrivingWrapper.Direction.SPINRIGHT, .8, rotSpeed);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDCAROUSEL) {
            driver.AutonomousDrive(DrivingWrapper.Direction.SPINRIGHT, .8, rotSpeed);
        }else if(location == VuforiaWebcamLocalization.ELocation.REDHOME) {
            driver.AutonomousDrive(DrivingWrapper.Direction.SPINLEFT, .9, rotSpeed);
        }
        opMode.sleep(900);
        driver.AutonomousDriveStop();

        driver.AutonomousDrive(DrivingWrapper.Direction.FORWARD, 1, speed);

        opMode.sleep(1000);
        driver.AutonomousDriveStop();

        arm.Intake(1);
        opMode.sleep(1000);
        arm.StopIntake();


        //Checks if is Carousel, because if we are not going to the carousel, we need to go over the barrier.
        if(location == VuforiaWebcamLocalization.ELocation.BLUECAROUSEL) {
//            arm.Carousel(3, 1, false);
            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1.5, speed);
            opMode.sleep(1500);
            driver.AutonomousDriveStop();

            arm.ResetArm();

            driver.AutonomousDrive(DrivingWrapper.Direction.SPINLEFT, .6, rotSpeed);
//            arm.Carousel(3, 1, false);
            opMode.sleep(600);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 2, speed);
            opMode.sleep(2000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.RIGHT, 2.4, speed);
            opMode.sleep(2400);
            driver.AutonomousDriveStop();

            crMotor.setPower(.5);
            opMode.sleep(4000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.LEFT, 2.2, speed);
            opMode.sleep(2200);
            driver.AutonomousDriveStop();


        }else if(location == VuforiaWebcamLocalization.ELocation.REDCAROUSEL) {
            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1.5, speed);
            opMode.sleep(1500);
            driver.AutonomousDriveStop();

            arm.ResetArm();

            driver.AutonomousDrive(DrivingWrapper.Direction.SPINLEFT, .6, rotSpeed);
//            arm.Carousel(3, 1, false);
            opMode.sleep(600);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 2, speed);
            opMode.sleep(2000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.RIGHT, 3, speed);
            opMode.sleep(2400);
            driver.AutonomousDriveStop();

            crMotor.setPower(-.5);
            opMode.sleep(4000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.LEFT, 2.2, speed);
            opMode.sleep(2200);
            driver.AutonomousDriveStop();
        }else if(location == VuforiaWebcamLocalization.ELocation.BLUEHOME) {
            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1, speed);
            opMode.sleep(1000);
            driver.AutonomousDriveStop();


            driver.AutonomousDrive(DrivingWrapper.Direction.SPINLEFT, .7, rotSpeed);
//            arm.Carousel(3, 1, false);
            opMode.sleep(700);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 2, speed);
            opMode.sleep(2000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.RIGHT, 5, speed);
            opMode.sleep(5000);
            driver.AutonomousDriveStop();

            arm.ResetArm();
            opMode.sleep(5000);

        }else if(location == VuforiaWebcamLocalization.ELocation.REDHOME) {
            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1, speed);
            opMode.sleep(1000);
            driver.AutonomousDriveStop();


            driver.AutonomousDrive(DrivingWrapper.Direction.SPINRIGHT, .7, rotSpeed);
//            arm.Carousel(3, 1, false);
            opMode.sleep(700);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 2, speed);
            opMode.sleep(2000);
            driver.AutonomousDriveStop();

            driver.AutonomousDrive(DrivingWrapper.Direction.LEFT, 5, speed);
            opMode.sleep(5000);
            driver.AutonomousDriveStop();

            arm.ResetArm();
            opMode.sleep(5000);

        }else {
            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1.5, speed*2);
            opMode.sleep(1500);
            driver.AutonomousDriveStop();
        }

//
//            driver.AutonomousDrive(DrivingWrapper.Direction.RIGHT, 2, speed);
//            arm.AutonomousArmMove(1);
//            driver.AutonomousDrive(DrivingWrapper.Direction.FORWARD, 1,speed);
//            //arm.AutonomousIntake(1);
//            arm.AutonomousOutput(2);
//            driver.AutonomousDrive(DrivingWrapper.Direction.BACKWARD, 1,speed);
//            driver.AutonomousDrive(DrivingWrapper.Direction.LEFT, 4,speed);
//            arm.Carousel(10, -1);
        arm.SetLevel(0);

         */

    }
//    public void RunCarousel(boolean isBlue){
//        if(isBlue){
//            driver.AutonomousDrive(DrivingWrapper.Direction.RIGHT, 1, speed);
//        }else {
//            driver.AutonomousDrive(DrivingWrapper.Direction.LEFT, 1, speed);
//            arm.Carousel(2, 1);
//        }
//
//
//    }
    public void RunAutonomousV2(VuforiaWebcamLocalization.ELocation location, LinearOpMode opMode){
//        arm.init(false);
//        opMode.sleep(1000);
//        arm.SetLevel(2);
//        Pose2d start = new Pose2d(0,0);
//        Trajectory tr1;
//        Trajectory tr2;
//        Trajectory tr3;
//        double rot;
//        DrivingWrapper.Direction rotDir;
//        if(location == VuforiaWebcamLocalization.ELocation.BLUECAROUSEL){
//            tr1 = roadRunner.trajectoryBuilder(start).forward(14*2).build();
//            tr2 = roadRunner.trajectoryBuilder(start).forward(17*2).build();
//            tr3 = roadRunner.trajectoryBuilder(start).back(34*2).build();
//            rot = -45;
//            rotDir = DrivingWrapper.Direction.SPINLEFT;
//
//        }else if(location == VuforiaWebcamLocalization.ELocation.REDCAROUSEL) {
//            tr1 = roadRunner.trajectoryBuilder(start).forward(14*2).build();
//            tr2 = roadRunner.trajectoryBuilder(start).forward(17*2).build();
//            tr3 = roadRunner.trajectoryBuilder(start).back(34*2).build();
//            rot = 45;
//            rotDir = DrivingWrapper.Direction.SPINRIGHT;
//        }else {
//            telemetry.addData("Bad","bad");
//            return;
//        }
//        roadRunner.followTrajectory(tr1);
//        driver.AutonomousDrive(rotDir, 1, rotSpeed);
//        roadRunner.followTrajectory(tr2);
//        arm.IntakeReverse(1);
//        opMode.sleep(1000);
//        arm.StopIntake();
//        roadRunner.followTrajectory(tr3);
//        crMotor.setPower(1);
//        opMode.sleep(1000);
//        crMotor.setPower(0);
    }
    public void RunAutonomousV3(VuforiaWebcamLocalization.ELocation location, LinearOpMode opMode){
//        arm.init(false);
//        opMode.sleep(1000);
//        arm.SetLevel(2);
//        Pose2d start = new Pose2d(0,0);
//        Trajectory tr1;
//        Trajectory tr2;
//        Trajectory tr3;
//        double rot;
//        DrivingWrapper.Direction rotDir;
//        if(location == VuforiaWebcamLocalization.ELocation.BLUECAROUSEL){
//            tr1 = roadRunner.trajectoryBuilder(start).splineTo(new Vector2d(-24,34), roadRunner).build();
//            tr2 = roadRunner.trajectoryBuilder(start).forward(17*2).build();
//            tr3 = roadRunner.trajectoryBuilder(start).back(34*2).build();
//            rot = -45;
//            rotDir = DrivingWrapper.Direction.SPINLEFT;
//
//        }else if(location == VuforiaWebcamLocalization.ELocation.REDCAROUSEL) {
//            tr1 = roadRunner.trajectoryBuilder(start).forward(14*2).build();
//            tr2 = roadRunner.trajectoryBuilder(start).forward(17*2).build();
//            tr3 = roadRunner.trajectoryBuilder(start).back(34*2).build();
//            rot = 45;
//            rotDir = DrivingWrapper.Direction.SPINRIGHT;
//        }else {
//            telemetry.addData("Bad","bad");
//            return;
//        }
//        roadRunner.followTrajectory(tr1);
//        driver.AutonomousDrive(rotDir, 1, rotSpeed);
//        roadRunner.followTrajectory(tr2);
//        arm.IntakeReverse(1);
//        opMode.sleep(1000);
//        arm.StopIntake();
//        roadRunner.followTrajectory(tr3);
//        crMotor.setPower(1);
//        opMode.sleep(1000);
//        crMotor.setPower(0);
    }
}
