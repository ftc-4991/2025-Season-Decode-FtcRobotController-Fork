package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * This program contains all of the default functions that our robot can use for the
 * FTC Decode season.
 * it contains controls for a mecanum chassis, chassis speed variables,
 * a launcher & and several speed variables for it, servo control for a hopper (prevent
 * artifacts from entering the launch area before they should), and intake motors
 *
 */
@TeleOp
public class DecodeTeleOpV2 extends LinearOpMode {
    //Motors
    //Wheel motors
    DcMotor fr = null;
    DcMotor fl = null;
    DcMotor br = null;
    DcMotor bl = null;
    //Flywheel motor
    DcMotorEx launcher = null;

    //Intake Motors
    DcMotor leftIntake = null;
    DcMotor rightIntake = null;
    //Hopper Servo
    Servo hopper = null;
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

    //Intake power modifier
    double iSpeed = 0;
    //Flywheel power modifier
    double lSpeed = 0;
    /**
     * The speed the launcher must go in order to score from the back launch zone
     */
    @Deprecated
    public static final double FAR_RANGE_SPEED = 0.75;
    /**
     * The speed the launcher must go in order to score from the end of the large launch zone
     */
    @Deprecated
    public static final double MEDIUM_RANGE_SPEED = 0.65;
    /**
     * The speed the launcher must go in order to score from the "middle" of the large launch zone
     */
    @Deprecated
    public static final double CLOSE_RANGE_SPEED = 0.55;
    /**
     * The Velocity the launcher should be set to in order to score from the back launch zone
     */
    public static final double FAR_RANGE_VELOCITY = 980;
    /**
     * The Velocity the launcher should be set to in order to score from the "end" of the large launch zone
     */
    public static final double MEDIUM_RANGE_VELOCITY = 730;
    /**
     * The speed the launcher must go in order to score from the "middle" of the large launch zone
     */
    public static final double CLOSE_RANGE_VELOCITY = 729;
    /**
     * The Value that "F" Should be set to score at "medium" range
     */
    public static final double MEDIUM_RANGE_F_VALUE = 26.4;
    /**
     * The Value that "P" Should be set to to score at "medium" range
     */
    public static final double MEDIUM_RANGE_P_VALUE = 80;
    /**
     * The Value that "F" Should be set to score at "far" range
     */
    public static final double FAR_RANGE_F_VALUE = 30;
    /**
     * The Value that "P" Should be set to to score at "far" range
     */
    public static final double FAR_RANGE_P_VALUE = 25;
    /**
     * The Value that "F" Should be set to score at "close" range
     */
    public static final double CLOSE_RANGE_F_VALUE = 26.4;
    /**
     * The Value that "P" Should be set to to score at "close" range
     */
    public static final double CLOSE_RANGE_P_VALUE = 80;
    /**
     * The Velocity the launcher is attempting to reach
     */
    public double currentTargetVelocity = 0;
    /**
     *  F = feedforward/power prediction; how much power you think you need to maintain your
     *  current speed
     *  */
    double F = 0;
    /** P= power; the power being sent to the motor; the farther away from the target velocity,
     * the more power you want to send; the closer you are, the less power you want to send.
     * most important constant to fine-tune/control
     */
    double P = 0;
    /**
     * Remove the power of the launcher
     */
    public static final double OFF = 0;

    //Servo Positions
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
        fr = hardwareMap.get(DcMotor.class, "fr");
        fl = hardwareMap.get(DcMotor.class, "fl");
        br = hardwareMap.get(DcMotor.class, "br");
        bl = hardwareMap.get(DcMotor.class, "bl");
        launcher = hardwareMap.get(DcMotorEx.class, "launch");
        leftIntake = hardwareMap.get(DcMotor.class,"lI");
       // rightIntake = hardwareMap.get(DcMotor.class,"rI");
        hopper = hardwareMap.get(Servo.class,"hS1");
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

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
                sleep(200);
            }
            if (gamepad1.dpad_up) {
                adjustCSpeed(1);
            }
            if (gamepad1.dpad_down) {
                adjustCSpeed(0.5);
            }
            //when "y" is pressed on Gamepad 2, the target velocity cycles between close, medium, and far
            if (gamepad2.y) {
                    if (currentTargetVelocity == FAR_RANGE_VELOCITY){
                        currentTargetVelocity = MEDIUM_RANGE_VELOCITY;
                        F = MEDIUM_RANGE_F_VALUE;
                        P = MEDIUM_RANGE_P_VALUE;
                        telemetry.addData("Target Velocity at Medium: ", MEDIUM_RANGE_VELOCITY);
                        sleep(250);
                    }
                    else if (currentTargetVelocity == MEDIUM_RANGE_VELOCITY){
                        currentTargetVelocity = CLOSE_RANGE_VELOCITY;
                        F = CLOSE_RANGE_F_VALUE;
                        P = CLOSE_RANGE_P_VALUE;
                        telemetry.addData("Target Velocity at Close: ", CLOSE_RANGE_VELOCITY);
                        sleep(250);

                    } else {
                        currentTargetVelocity = FAR_RANGE_VELOCITY;
                        F = FAR_RANGE_F_VALUE;
                        P = FAR_RANGE_P_VALUE;
                        telemetry.addData("Target Velocity at Far: ", FAR_RANGE_VELOCITY);
                        sleep(250);
                    }

            }
            if (gamepad2.x) {
                adjustLSpeed(MEDIUM_RANGE_SPEED);
                launcher.setPower(lSpeed);
            }
            if (gamepad2.a) {
                adjustLSpeed(CLOSE_RANGE_SPEED);
                launcher.setPower(lSpeed);
            }
            if (gamepad2.b) {
                currentTargetVelocity = 0;
                F = 0;
                P = 0;
                PIDFCoefficients newPidfCoefficients = new PIDFCoefficients(P, 0,0, F);
                launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, newPidfCoefficients);
                launcher.setVelocity(currentTargetVelocity);
            }
            if (gamepad1.y) {
                adjustISpeed(1);
            }
//            if (gamepad1.a) {
//                adjustISpeed(0);
//            }
            //"Opens" the hopper
            if (gamepad2.left_bumper) {
                hopper.setPosition(LEFT);
            }
            //"Closes" the  hopper
            if (gamepad2.right_bumper) {
                hopper.setPosition(RIGHT);
            }
            if (gamepad2.dpad_up) {
                adjustLSpeed(lSpeed + 0.05);
                sleep(100);
            }
            if (gamepad2.dpad_down) {
                adjustLSpeed(lSpeed - 0.05);
                sleep(100);
            }
            //
            if (gamepad2.right_trigger >= 0.5) {
                hopper.setPosition(LEFT);
                sleep(300);
                hopper.setPosition(RIGHT);
            }
            if (gamepad2.left_trigger >= 0.5) {
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);
                sleep(3000);
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);
                sleep(3000);
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);
            }
            //button that should fire all artifacts in (not-so) rapid succession
            //Speed should be used for the closer range
            if (gamepad1.a) {

                //sleep(4000);
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);

                sleep(1200);
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);

                sleep(1200);
                hopper.setPosition(LEFT);
                sleep(600);
                hopper.setPosition(RIGHT);


            }
            //button that should fire all artifacts in (not-so) rapid succession
            //Speed should be used for the farther range
            if (gamepad1.b) {

                //sleep(4000);
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
            }
            leftIntake.setPower(iSpeed);
            PIDFCoefficients newPidfCoefficients = new PIDFCoefficients(P, 0,0, F);
            launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, newPidfCoefficients);
            launcher.setVelocity(currentTargetVelocity);

            double curVelocity = launcher.getVelocity();
            double error = currentTargetVelocity - curVelocity;
            if (currentTargetVelocity == FAR_RANGE_VELOCITY) {
                telemetry.addData("Current Target Velocity:", FAR_RANGE_VELOCITY);
            } else if (currentTargetVelocity == MEDIUM_RANGE_VELOCITY) {
                telemetry.addData("Current Target Velocity:", MEDIUM_RANGE_VELOCITY);
            } else if (currentTargetVelocity == CLOSE_RANGE_VELOCITY) {
                telemetry.addData("Current Target Velocity:", CLOSE_RANGE_VELOCITY);
            }
            telemetry.addData("Target velocity: ", currentTargetVelocity);
            telemetry.addData("Current velocity: ", "%.2f", curVelocity);
            telemetry.addData("Error: ", "%.2f", error);
            telemetry.addData("launcher speed: ", lSpeed);
            telemetry.addLine("----------------------------");
            telemetry.update();
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
     *
     * @param newSpeed  should be a decimal between -1 and 1, and not 0.
     */
    public void adjustCSpeed(double newSpeed) {
        cSpeed = newSpeed;
    }

    /**
     * Changes the speed the launcher is going to the new value
     *
     * @param newSpeed should be any value between -1 and 1
     */
    @Deprecated
    public void adjustLSpeed(double newSpeed) {
        lSpeed = newSpeed;
    }

    /**
     * Changes the speed the intake is going to the new value
     * @param newSpeed should be any value between -1 and 1
     */
    public void adjustISpeed(double newSpeed) {
        iSpeed = newSpeed;
    }


}
