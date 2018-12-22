package org.firstinspires.ftc.teamcode.ThatStuff;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.ftcrobotcontroller.BuildConfig;
import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp (name = ("ThatStuffTeleOp"), group = ("RunOpModes"))
// good code stuffs
public class ThatStuffTeleOp extends LinearOpMode {
    //orion this is not your place kthxleave

    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorExtend;
    private Servo phatServo;
    private Servo tiltServo;
    double phatvar = 0.1;
    double tiltvar = 0.75;
    boolean locks;

    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorExtend = hardwareMap.dcMotor.get("motorExtend");
        phatServo = hardwareMap.servo.get("phatServo");
        tiltServo = hardwareMap.servo.get("tiltServo");
        motorLeft.setDirection(DcMotor.Direction .REVERSE);
        motorExtend.setDirection(DcMotor.Direction.REVERSE);
        locks = false;
        waitForStart();
//reeeeeeeeeeeeeeeeeeee
        while (opModeIsActive())
        {
            if (gamepad1.left_bumper) {
                motorLeft.setPower(gamepad1.right_stick_y/2);
                motorRight.setPower(gamepad1.left_stick_y/2);
            }else{
                motorLeft.setPower(gamepad1.right_stick_y);
                motorRight.setPower(gamepad1.left_stick_y);
            }
            if (gamepad2.right_bumper){
                motorExtend.setPower(gamepad2.right_stick_y/2);
            }else{
                motorExtend.setPower(gamepad2.right_stick_y);
            }
            if (gamepad2.a) {
                tiltServo.setPosition(.5);
                tiltvar = .5;
             }
             if (gamepad2.y){
                tiltServo.setPosition(.9);
                tiltvar = .9;
             }

             if (gamepad2.x){
                phatServo.setPosition(.2);
                phatvar = .2;
             }
             if (gamepad2.b){
                phatServo.setPosition(.7);
                phatvar = .4;
             }

             if (gamepad2.dpad_down){
                if (phatvar <= -1 || !locks){
                    phatvar = phatvar + .01;
                    phatServo.setPosition(phatvar);
                    sleep(50);
                }
             }
            if (gamepad2.dpad_up){
                if (phatvar >= .35 || !locks){
                    phatvar = phatvar - .01;
                    phatServo.setPosition(phatvar);
                    sleep(50);
                }
            }
            if (gamepad2.dpad_right){
                if (tiltvar >= .86 || !locks){
                    tiltvar = tiltvar - .01;
                    tiltServo.setPosition(tiltvar);
                    sleep(50);
                }
            }
            if (gamepad2.dpad_left){
                if (tiltvar <= .5 || !locks){
                    tiltvar = tiltvar + .01;
                    tiltServo.setPosition(tiltvar);
                    sleep(50);
                }
            }
            if (gamepad2.left_bumper){
                locks = false;
            }else{
                locks = true;
            }
            if (gamepad2.start){
                tiltvar = .8;
            }
            if (gamepad2.back){
                phatvar = .8;
            }
            if (tiltvar >= 1){
                tiltvar = 0;
            }
            if (phatvar >= 1){
                phatvar = 0;
            }
            telemetry.addData("Tilt Pos", tiltvar);
            telemetry.addData("Phat Pos", phatvar);
            telemetry.update();

            idle();

        }
    }
        }
// bad things.exe
