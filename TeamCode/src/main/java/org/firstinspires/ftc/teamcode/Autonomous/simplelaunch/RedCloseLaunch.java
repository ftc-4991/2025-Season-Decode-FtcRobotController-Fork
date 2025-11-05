package org.firstinspires.ftc.teamcode.Autonomous.simplelaunch;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *
 * Far refers to the larger launch zone, not the proximity to the drivers
 *
 */
@Autonomous
public class RedCloseLaunch extends LinearOpMode {
    DcMotor fr = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor bl = null;
    DcMotor launch = null;
    Servo hopper = null;

    /**
     * Turn the servo to its left-most position
     */
    public static final double LEFT = 0;
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

        waitForStart();

        double power = 1;
        //make the robot back up
        fr.setPower(-power);
        fl.setPower(-power);
        br.setPower(-power);
        bl.setPower(-power);

        sleep (1252);
        //turn off wheels and start the launcher
        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);
        launch.setPower(0.67);
        sleep(301);
        //Opens and closes the hopper 3 times to launch
        hopper.setPosition(RIGHT);
        sleep(150);
        hopper.setPosition(LEFT);
        sleep(200);
        hopper.setPosition(RIGHT);
        sleep(150);
        hopper.setPosition(LEFT);
        sleep(200);
        hopper.setPosition(RIGHT);
        sleep(150);
        hopper.setPosition(LEFT);
        sleep(200);
        launch.setPower(0);
        //Strafe left for 3/4 of a second
        fr.setPower(power);
        fl.setPower(-power);
        br.setPower(power);
        bl.setPower(-power);
        sleep(750);
        //turn off wheels
        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);


    }
}
