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
        drive = gamepad1.left_stick_y;
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

       

        if (gamepad2.a) {
            gPad2_AToggle= !gPad2_AToggle;
            sleep(sleepConstant);
        }

        if (gPad2_AToggle) {
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

        if (gPad2_BToggle) {
            ULTIMATE.shoot1.setPower(.4);
            ULTIMATE.shoot2.setPower(.4);
        } else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot2.setPower(0);
        }
        telemetry.addData("shoot power: ", ULTIMATE.shoot1.getPower());

        //ULTIMATE.shoot1.setPower(-gamepad2.right_trigger);
        //ULTIMATE.shoot2.setPower(-gamepad2.right_trigger);
        //ULTIMATE.intake.setPower(-gamepad2.left_trigger);




        /*if (gamepad2.right_bumper) {
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
    }*/
        if(gamepad2.right_bumper){
            ULTIMATE.intakeServo.setPosition(1);
        }
        else{
            ULTIMATE.intakeServo.setPosition(.85);
        }

}
    // Makes sure the program stops completely
    public void stop () {
    }
}
