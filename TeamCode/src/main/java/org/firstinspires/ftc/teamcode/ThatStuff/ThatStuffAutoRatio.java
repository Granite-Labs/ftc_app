/* __-----_________________{]__________________________________________________
{&&&&&&&#%%&#%&%&%&%&%#%&|]__________________________________________________\
                         {]
*/

package org.firstinspires.ftc.teamcode.ThatStuff;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="That Stuff Auto Ratio", group="Linear Opmode")
public class ThatStuffAutoRatio extends LinearOpMode {

    //Add motor vars
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorExtend;
    private Servo phatServo;
    private Servo tiltServo;
    private GoldAlignDetector detector;
    boolean turn;
    int waitcount;
    boolean firstrun;
    double goldfix;



    @Override
    public void runOpMode() {
        //Setup Hardware Map
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorExtend = hardwareMap.dcMotor.get("motorExtend");
        phatServo = hardwareMap.servo.get("phatServo");
        tiltServo = hardwareMap.servo.get("tiltServo");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorExtend.setDirection(DcMotor.Direction.REVERSE);
        //Init the CV
        setupcv();
        firstrun = true;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if(firstrun){
                motorExtend.setPower(-20);
                timeWait(1000);
                motorExtend.setPower(0);
                firstrun = false;
            }
            while((waitcount != 10) && opModeIsActive()){
                //wait until CV finds and aligns robot to gold
                align();
            }
            driveForward(20);
            timeWait(10000);
            driveForward(0);

        }
        //Close CV
        detector.disable();
    }




    public void align() {
        //Attempts to move the robot to be head on with the gold mineral, **Make sure you run it in a loop
        if(detector.isFound()){
            if(detector.getXPosition() <= 320){
                turnRight(detector.getXPosition()/16);
                turn = false;
            } else {
                //Turn the robot right
                goldfix = detector.getXPosition() - 320;
                turnRight(goldfix/16);
                turn = true;
            }
        } else if(!detector.isFound()) {
            //Do some movement to try and get gold mineral in camera frame
            turnRight(10);
            timeWait(500);
            turnRight(0);
            turnLeft(10);
            timeWait(500);
            turnLeft(0);
        }
        if(detector.getAligned()){
            waitcount = waitcount+1;
            timeWait(500);
        }else{
            waitcount = 0;
        }
        updateTele();
    }

    public void setupcv() {
        //Inits Backend dogeCV stuff
        // Set up detector
        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!
    }

    public void updateTele(){
        telemetry.addData("Found",detector.isFound());
        telemetry.addData("Gold X",detector.getXPosition());
        telemetry.addData("AlignedCount", waitcount);
        /*telemetry.addData("alignx",detector.getAlignX());
        telemetry.addData("alignmax",detector.getAlignMax());
        telemetry.addData("alignmin",detector.getAlignMin());
        */
        telemetry.addData("RightVal",goldfix/16);
        telemetry.addData("LeftVal",20-detector.getXPosition()/16);
        telemetry.update();
    }

    //Movement Functions
    public void driveForward(double power){
        //Code block to make robot move forward for a power interval
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }
    public void driveReverse(double power){
        //Code block to make robot move in reverse for a power interval
        motorLeft.setPower(-power);
        motorRight.setPower(-power);
    }
    public void turnLeft(double power){
        //Code block to make robot turn left for a power interval
        motorLeft.setPower(-power);
        motorRight.setPower(power);
    }
    public void turnRight(double power){
        //Code block to make robot turn right for a power interval
        motorLeft.setPower(power);
        motorRight.setPower(-power);
    }
    public void timeWait(long time) {
        //Codeblock to make the robot stop and wait for set amount of time
        sleep(time);
    }
}
