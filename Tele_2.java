import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Hardware;


@TeleOp
//@Disabled
public class Tele_2 extends OpMode {

    // Inherits hardware class
    private ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();


    boolean bToggle = false;
    boolean xWasPressed = false;
    boolean yToggle = false;
    boolean gPad2_AToggle = false;
    boolean gPad2_BWasPressed = false;
    boolean gPad2_xWasPressed = false;
    boolean gPad2_yWasPressed = false;
    int sleepConstant = 200;


    // This is the same sleep method that is used in autonomous programs. It is used
    // for delays in the main loop to prevent the program from thinking a button was pressed
    // multiple times
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void init() {
        // Initializes hardware map for the control hub
        ULTIMATE.init(hardwareMap);
    }

    public void loop() {

        // Primary driver's controls

        // Driving
        double drive;   // Power for forward and back motion
        double strafe;  // Power for left and right motion
        double rotate;  // Power for rotating the robot
        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = -gamepad1.right_stick_x;
        double frontLeftPower = (drive) + strafe - (rotate);
        double backLeftPower = (drive) - strafe - (rotate);
        double frontRightPower = (drive) - strafe + (rotate);
        double backRightPower = (drive) + strafe + (rotate);
        ULTIMATE.leftF.setPower(frontLeftPower);
        ULTIMATE.leftB.setPower(backLeftPower);
        ULTIMATE.rightF.setPower(frontRightPower);
        ULTIMATE.rightB.setPower(backRightPower);
        telemetry.addData("Left Front Power", (float) frontLeftPower);
        telemetry.addData("Left Rear Power", (float) backLeftPower);
        telemetry.addData("Right Front Power", (float) frontRightPower);
        telemetry.addData("Right Rear Power", (float) backRightPower);
        telemetry.addData("intake power: ", ULTIMATE.intake.getPower());

        // Below is an option for the primary driver to use the dpad to move for slower, more
        // precise movements
        if (gamepad1.dpad_up) {
            ULTIMATE.leftF.setPower(.8);
            ULTIMATE.leftB.setPower(.8);
            ULTIMATE.rightF.setPower(.8);
            ULTIMATE.rightB.setPower(.8);
        }
        if (gamepad1.dpad_down) {
            ULTIMATE.leftF.setPower(-.8);
            ULTIMATE.leftB.setPower(-.8);
            ULTIMATE.rightF.setPower(-.8);
            ULTIMATE.rightB.setPower(-.8);
        }
        if (gamepad1.dpad_right) {
            ULTIMATE.leftF.setPower(.8);
            ULTIMATE.leftB.setPower(-.8);
            ULTIMATE.rightF.setPower(-.8);
            ULTIMATE.rightB.setPower(.8);
        }
        if (gamepad1.dpad_left) {
            ULTIMATE.leftF.setPower(-.8);
            ULTIMATE.leftB.setPower(.8);
            ULTIMATE.rightF.setPower(.8);
            ULTIMATE.rightB.setPower(-.8);
        }

        // move the intake dc motor forwards and backwards
        if (gamepad1.b) {
            bToggle = !bToggle;
            sleep(sleepConstant);
        }

        if (bToggle) {
            ULTIMATE.intake.setPower(1);
        } else {
            ULTIMATE.intake.setPower(0);
        }



        // move the "trigger"
        if (gamepad1.a) {
            ULTIMATE.trigger.setPosition(.8);
        } else {
            ULTIMATE.trigger.setPosition(0);
        }

        // move the "deflector"
        if (gamepad1.x) {
            if (!xWasPressed) {
                ULTIMATE.deflector.setPosition(.55);
               xWasPressed = true;
                sleep(sleepConstant);
            } else {
                ULTIMATE.deflector.setPosition(1);
                xWasPressed = false;
                sleep(sleepConstant);
            }
        }



        // Accessory driver's controls

        // move the shoot wheel motors
        if (gamepad2.a) {
            gPad2_AToggle = !gPad2_AToggle;
            sleep(sleepConstant);
        }

        if (gPad2_AToggle) {
            ULTIMATE.shoot1.setPower(1);
            ULTIMATE.shoot2.setPower(1);
        } else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot2.setPower(0);
        }

        // move the "catchplate"
        if (gamepad2.b) {
            if (!gPad2_BWasPressed) {
                ULTIMATE.catchPlate.setPosition(1);
                gPad2_BWasPressed = true;
                sleep(sleepConstant);
            } else {
                ULTIMATE.catchPlate.setPosition(.85);
                gPad2_BWasPressed = false;
                sleep(sleepConstant);
            }
        }

        // move the wobble goal lifter
        if (gamepad2.x) {
            if (!gPad2_xWasPressed) {
                ULTIMATE.armSwing.setPosition(1);
                gPad2_xWasPressed = true;
                sleep(sleepConstant);
            } else {
                ULTIMATE.armSwing.setPosition(.35);
                gPad2_xWasPressed = false;
                sleep(sleepConstant);
            }
        }


        if(gamepad2.right_bumper){
            ULTIMATE.armSwing.setPosition(0);
            sleep(sleepConstant);
        }



        // move wobble goal latch
        if (gamepad2.y) {
            if (!gPad2_yWasPressed) {
                ULTIMATE.latch.setPosition(1);
                gPad2_yWasPressed = true;
                sleep(sleepConstant);
            } else {
                ULTIMATE.latch.setPosition(0);
                gPad2_yWasPressed = false;
                sleep(sleepConstant);
            }
        }

        // move the wobble goal arm
        ULTIMATE.extensionArm.setPower(gamepad2.left_stick_x);
    }

    // Makes sure the program stops completely
    public void stop () {
    }
}
