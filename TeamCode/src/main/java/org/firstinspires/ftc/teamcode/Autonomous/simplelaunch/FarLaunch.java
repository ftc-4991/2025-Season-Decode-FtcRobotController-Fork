package org.firstinspires.ftc.teamcode.Autonomous.simplelaunch;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(group="Simple Launch")
public class FarLaunch extends LinearOpMode {
    DcMotor fr = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor bl = null;
    DcMotor launch = null;
    Servo hopper = null;
    public static final double LEFT = -0.3;
    /**
     * Turn the servo to its right-most position
     */
    public static final double RIGHT = 0.3;



    @Override
    public void runOpMode() throws InterruptedException {
        fr = hardwareMap.get(DcMotor.class,"fr");
        fl = hardwareMap.get(DcMotor.class,"fl");
        br = hardwareMap.get(DcMotor.class,"br");
        bl = hardwareMap.get(DcMotor.class,"bl");
        launch = hardwareMap.get(DcMotor.class, "launch");
        hopper = hardwareMap.get(Servo.class,"hS1");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        launch.setDirection(DcMotorSimple.Direction.REVERSE);
        launch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hopper.setPosition(RIGHT);

        waitForStart();
        double power = 1;
        launch.setPower(0.75);
        sleep(4000);
        //Opens and closes the hopper 3 times to launch
        hopper.setPosition(LEFT);
        sleep(600);
        hopper.setPosition(RIGHT);
        sleep(4000);
        hopper.setPosition(LEFT);
        sleep(600);
        hopper.setPosition(RIGHT);
        sleep(4000);
        hopper.setPosition(LEFT);
        sleep(600);
        hopper.setPosition(RIGHT);
        sleep(4000);
        launch.setPower(0);

        fr.setPower(-power);
        fl.setPower(-power);
        br.setPower(-power);
        bl.setPower(-power);
        sleep(300);
        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);
    }
}
