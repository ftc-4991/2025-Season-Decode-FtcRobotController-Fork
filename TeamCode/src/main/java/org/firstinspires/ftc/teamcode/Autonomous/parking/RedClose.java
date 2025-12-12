package org.firstinspires.ftc.teamcode.Autonomous.parking;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *
 * Close refers to the larger launch zone, not the proximity to the drivers
 *
 */
@Autonomous(group="park")
public class RedClose extends LinearOpMode {
    DcMotor fr = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor bl = null;

    Servo hopper = null;
    /**
     * Turn the servo to its left-most (open) position
     */
    public static final double LEFT = 0;
    /**
     * Turn the servo to its right-most (closed) position
     */
    public static final double RIGHT = 0.3;



    @Override
    public void runOpMode() throws InterruptedException {
        fr = hardwareMap.get(DcMotor.class,"fr");
        fl = hardwareMap.get(DcMotor.class,"fl");
        br = hardwareMap.get(DcMotor.class,"br");
        bl = hardwareMap.get(DcMotor.class,"bl");
        hopper = hardwareMap.get(Servo.class, "hS1");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        hopper.setPosition(RIGHT);

        waitForStart();

        double power = 1;

        fr.setPower(power);
        fl.setPower(-power);
        br.setPower(-power);
        bl.setPower(power);

        sleep (1000);

        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);
    }
}
