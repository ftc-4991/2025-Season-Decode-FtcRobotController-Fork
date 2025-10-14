package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This program contains all of the default functions that our robot can use for the
 * FTC Decode season.
 * it contains controls for a mecanum chassis, chassis speed variables,
 * a launcher & and several speed variables for it
 */
@TeleOp
public class DecodeTeleOpV1 extends LinearOpMode {
    //Motors
    //Wheel motors
    DcMotor fr = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor bl = null;
    //Flywheel motor
    DcMotor launcher = null;
    //Wheel input variables (joysticks - left x, left y, right x)
    double x;
    double y;
    double rx;

    //Wheel power modifiers

    /**
     * modifies the overall speed of the chassis
      */
    double cSpeed = 1;
    /**
     * changes the direction of the front of the robot (value should ONLY be 1 or -1)
     */
    double direction = 1;

    //Flywheel power modifier
    double lSpeed = 0;
    /**
     * The speed the launcher must go in order to score from the back launch zone
     */
    public static final double FAR_RANGE_SPEED = 0.8;
    /**
     * The speed the launcher must go in order to score from the end of the large launch zone
     */
    public static final double MEDIUM_RANGE_SPEED = 0.7;
    /**
     * The speed the launcher must go in order to score from the "middle" of the large launch zone
     */
    public static final double CLOSE_RANGE_SPEED = 0.6;
    /**
     * Remove the power of the launcher
     */
    public static final double OFF = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        fr = hardwareMap.get(DcMotor.class, "fr");
        fl = hardwareMap.get(DcMotor.class, "fl");
        br = hardwareMap.get(DcMotor.class, "fr");
        bl = hardwareMap.get(DcMotor.class, "fr");

        waitForStart();
        while (opModeIsActive()) {
            x = gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            rx = gamepad1.right_stick_x;

            //Sets the power to each wheel  based on joystick inputs
            fr.setPower((y+x+rx) * cSpeed * direction);
            fl.setPower((y-x-rx) * cSpeed * direction);
            br.setPower((y-x+rx) * cSpeed * direction);
            bl.setPower((y+x-rx) * cSpeed * direction);

            if (gamepad1.left_stick_button) {
                flipDirection();
            }
            if (gamepad1.dpad_up) {
                adjustCSpeed(1);
            }
            if (gamepad1.dpad_down) {
                adjustCSpeed(0.5);
            }
            if (gamepad2.x) {
                adjustLSpeed(FAR_RANGE_SPEED);
            }
            if (gamepad2.y) {
                adjustLSpeed(MEDIUM_RANGE_SPEED);
            }
            if (gamepad2.b) {
                adjustLSpeed(CLOSE_RANGE_SPEED);
            }
            if (gamepad2.a) {
                adjustLSpeed(OFF);
            }

        }
    }

    /**
     * inverts the direction variable
     * This will make the robot go "backwards" when going "forwards"
     */
    public void flipDirection() {
        direction = direction * -1;
    }

    /**
     * Changes the overall speed the chassis can go to the new value
     * newSpeed should be a decimal between -1 and 1, and not 0.
     * @param newSpeed
     */
    public void adjustCSpeed(double newSpeed) {
        cSpeed = newSpeed;
    }

    /**
     * Changes the speed the chassis is going to the new value
     * newSpeed should be any value between -1 and 1
     * @param newSpeed
     */
    public void adjustLSpeed(double newSpeed) {
        lSpeed = newSpeed;
    }


}
