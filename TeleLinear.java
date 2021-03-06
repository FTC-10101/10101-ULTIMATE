import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
//@Disabled
public class TeleLinear extends LinearOpMode {

    // Inherits hardware class
    private ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    ElapsedTime timer = new ElapsedTime();

    double driveConstant = .8;
    boolean bToggle = false;
    boolean xWasPressed = false;
    boolean yWasPressed = false;
    boolean rbWasPressed = false;
    boolean lsButtonWasPressed = false;
    boolean gPad2_AToggle = false;
    boolean gPad2_BWasPressed = false;
    boolean gPad2_yWasPressed = false;
    boolean gPad2_xWasPressed = false;
    int reverse;

    int sleepConstant = 200;
    double slowMode = 1;





    @Override
    public void runOpMode() throws InterruptedException {
        // Initializes hardware map for the control hub
        ULTIMATE.init(hardwareMap,false,false);

        // Makes sure servos are in the right position
        ULTIMATE.armSwing.setPosition(.35);
        ULTIMATE.catchPlate.setPosition(1);

        waitForStart();
        timer.reset();
        while(opModeIsActive()) {


            telemetry.addData("Time: ", (int) timer.seconds());
            telemetry.addData("slowMode",slowMode);
            telemetry.addData("catchplate position", ULTIMATE.catchPlate.getPosition());
            telemetry.update();
            // --------------------------------Primary driver's controls--------------------------------
            // Driving
            double drive;   // Power for forward and back motion
            double strafe;  // Power for left and right motion
            double rotate;  // Power for rotating the robot
            drive = -gamepad1.left_stick_y * driveConstant;
            strafe = gamepad1.left_stick_x * driveConstant;
            rotate = -gamepad1.right_stick_x * driveConstant;
            double frontLeftPower = (drive) + strafe - (rotate);
            double backLeftPower = (drive) - strafe - (rotate);
            double frontRightPower = (drive) - strafe + (rotate);
            double backRightPower = (drive) + strafe + (rotate);
            ULTIMATE.leftF.setPower(frontLeftPower * slowMode);
            ULTIMATE.leftB.setPower(backLeftPower * slowMode);
            ULTIMATE.rightF.setPower(frontRightPower * slowMode);
            ULTIMATE.rightB.setPower(backRightPower * slowMode);

            if(((int) timer.seconds() == 5)){
                ULTIMATE.deflector.setPosition(1);
            }

            // Below is an option for the primary driver to use the dpad to move for slower, more
            // precise movements
            if (gamepad1.dpad_up) {
                ULTIMATE.leftF.setPower(.4);
                ULTIMATE.leftB.setPower(.4);
                ULTIMATE.rightF.setPower(.4);
                ULTIMATE.rightB.setPower(.4);
            }
            if (gamepad1.dpad_down) {
                ULTIMATE.leftF.setPower(-.4);
                ULTIMATE.leftB.setPower(-.4);
                ULTIMATE.rightF.setPower(-.4);
                ULTIMATE.rightB.setPower(-.4);
            }
            if (gamepad1.dpad_right) {
                ULTIMATE.leftF.setPower(.4);
                ULTIMATE.leftB.setPower(-.4);
                ULTIMATE.rightF.setPower(-.4);
                ULTIMATE.rightB.setPower(.4);
            }
            if (gamepad1.dpad_left) {
                ULTIMATE.leftF.setPower(-.4);
                ULTIMATE.leftB.setPower(.4);
                ULTIMATE.rightF.setPower(.4);
                ULTIMATE.rightB.setPower(-.4);
            }

            // move the intake dc motor
            if (gamepad1.b) {
                bToggle = !bToggle;
                sleep(sleepConstant);
            }

            if (bToggle) {
                ULTIMATE.intake.setPower(reverse);
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
                    ULTIMATE.deflector.setPosition(0);
                    xWasPressed = true;
                    sleep(sleepConstant);
                } else {
                    ULTIMATE.deflector.setPosition(1);
                    xWasPressed = false;
                    sleep(sleepConstant);
                }
            }
            if (gamepad1.y) {
                if (!yWasPressed) {
                    ULTIMATE.lapBar.setPosition(0);
                    yWasPressed = true;
                    sleep(sleepConstant);
                } else {
                    ULTIMATE.lapBar.setPosition(1);
                    yWasPressed = false;
                    sleep(sleepConstant);
                }
            }
            if (gamepad1.right_bumper) {
                if (!rbWasPressed) {
                    ULTIMATE.lapBar.setPosition(.1);
                    yWasPressed = true;
                    sleep(sleepConstant);
                } else {
                    ULTIMATE.lapBar.setPosition(.5);
                    rbWasPressed = false;
                    sleep(sleepConstant);
                }
            }

            if (gamepad1.left_bumper) {
                reverse = -1;
            } else {
                reverse = 1;
            }
            if(gamepad1.left_stick_button){
                if(!lsButtonWasPressed){
                    slowMode = .35;
                    lsButtonWasPressed = true;
                }
                else{
                    slowMode = 1;
                    lsButtonWasPressed = false;
                }
            }


            //----------------- Accessory driver's controls --------------------------------------------

            // move the shoot wheel motors
            if (gamepad2.a) {
                gPad2_AToggle = !gPad2_AToggle;
                sleep(sleepConstant);
            }

            if (gPad2_AToggle) {
                ULTIMATE.shoot1.setVelocity(1500);
                ULTIMATE.shoot2.setVelocity(1500);
            } else {
                ULTIMATE.shoot1.setVelocity(0);
                ULTIMATE.shoot2.setVelocity(0);
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

            // dump
            if (gamepad2.left_bumper) {
                ULTIMATE.catchPlate.setPosition(0);
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

            //moves the swing servo all the way up so we can get the wobble goal above the wall
            if (gamepad2.right_bumper) {
                ULTIMATE.armSwing.setPosition(0);
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
            if (gamepad2.dpad_right) {
                ULTIMATE.rearLatch.setPosition(1);
            }
            if (gamepad2.dpad_left) {
                ULTIMATE.rearLatch.setPosition(0);
            }



            // move the wobble goal arm
            ULTIMATE.extensionArm.setPower(gamepad2.left_stick_x);
        }
    }
    }





