import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Hardware;

// *****This program needs a lot of testing****
@TeleOp
//@Disabled
public class Tele_2 extends OpMode {

    // Inherits hardware class
    private ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();


    boolean bToggle = false;
    boolean aWasPressed = false;
    boolean rBumperWasPressed = false;
    boolean gPad2_AToggle = false;
    boolean gPad2_BToggle = false;
    boolean gPad2_rBumperWasPressed = false;
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
        rotate = gamepad1.right_stick_x;
        double frontLeftPower = (drive) + strafe + (rotate);
        double backLeftPower = (drive) - strafe - (rotate);
        double frontRightPower = (drive) - strafe + (rotate);
        double backRightPower = (drive) + strafe - (rotate);
        ULTIMATE.leftF.setPower(frontLeftPower);
        ULTIMATE.leftB.setPower(backLeftPower);
        ULTIMATE.rightF.setPower(frontRightPower);
        ULTIMATE.rightB.setPower(backRightPower);
        telemetry.addData("Left Front Power", (float) frontLeftPower);
        telemetry.addData("Left Rear Power", (float) backLeftPower);
        telemetry.addData("Right Front Power", (float) frontRightPower);
        telemetry.addData("Right Rear Power", (float) backRightPower);

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
            ULTIMATE.leftF.setPower(-.8);
            ULTIMATE.leftB.setPower(.8);
            ULTIMATE.rightF.setPower(.8);
            ULTIMATE.rightB.setPower(-.8);
        }
        if (gamepad1.dpad_left) {
            ULTIMATE.leftF.setPower(.8);
            ULTIMATE.leftB.setPower(-.8);
            ULTIMATE.rightF.setPower(-.8);
            ULTIMATE.rightB.setPower(.8);
        }

        if (gamepad1.b) {
            bToggle = !bToggle;
            sleep(sleepConstant);
        }

        if (bToggle && gamepad1.left_trigger == 0) {
            ULTIMATE.intake.setPower(1);
        }
        else {
            ULTIMATE.intake.setPower(0);
        }

        if (gamepad1.a) {
            if(!aWasPressed){
                ULTIMATE.feedServo.setPosition(.8);
                aWasPressed = true;
                sleep(sleepConstant);
            }
            else{
                ULTIMATE.feedServo.setPosition(0);
                aWasPressed = false;
                sleep(sleepConstant);
            }
        }


        // Accessory driver's controls

        // I have never worked with buttons this way before, so it needs to be tested and may need
        // to be changed. The idea is that when the driver presses a or b, instead of just flipping
        // a motor on, it will turn flip the value of a boolean variable which will be
        // responsible for the state of the motor. The sleeps are necessary because this program
        // is run in a loop. The computer is "checking" many times a second what buttons are being
        // pressed. The sleep is intended to be a delay between those "checks." Without them,
         //the hubs think you are pressing the button thousands of times and won't do anything

        // If they are slowing down the program, the sleep constant value needs to lowered.
        // The part of the conditional that checks if the triggers are zero is for other code
        // That I have written below and is commented out currently. I am just going to leave it
        // because we might use that later.

        if (gamepad2.a) {
            gPad2_AToggle= !gPad2_AToggle;
            sleep(sleepConstant);
        }

        if (gPad2_AToggle && gamepad2.right_trigger == 0) {
            ULTIMATE.shoot1.setPower(1);
            ULTIMATE.shoot2.setPower(1);
        } else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot2.setPower(0);
        }

        if (gamepad2.b) {
            gPad2_BToggle = !gPad2_BToggle;
            sleep(sleepConstant);
        }

        if (gPad2_BToggle && gamepad2.right_trigger == 0) {
            ULTIMATE.shoot1.setPower(.8);
            ULTIMATE.shoot2.setPower(.8);
        } else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot2.setPower(0);
        }

        // These lines are supposed to give the acc. driver the ability to reverse the intake and
        // shoot motors, but are commented because they may interfere with the a and b buttons
        //ULTIMATE.shoot1.setPower(-gamepad2.right_trigger);
        //ULTIMATE.shoot2.setPower(-gamepad2.right_trigger);
        //ULTIMATE.intake.setPower(-gamepad2.left_trigger);

        // alternate way of moving intake that we may switch to based off of driver preference
        //ULTIMATE.intake.setPower(gamepad2.left_stick_x);

        // This is basically the same way I put the front plate grabber servos on the same button
        // last year, so this should work


        if (gamepad2.right_bumper) {
            if(!gPad2_rBumperWasPressed) {
                ULTIMATE.intakeServo.setPosition(1);
                gPad2_rBumperWasPressed = true;
                sleep(sleepConstant);
            }

            else{
                ULTIMATE.intakeServo.setPosition(.85);
                gPad2_rBumperWasPressed = false;
                sleep(sleepConstant);
            }


    }

}
    // Makes sure the program stops completely
    public void stop () {
    }
}
