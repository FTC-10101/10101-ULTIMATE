/*The class that runs for the tele-op period. For more detail, see our engineering portfolio or the
programming section of our engineering notebook */
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
// LinearOpMode to more cleanly access Thread.sleep, and because it's simpler and saves lines
public class TeleLinear extends LinearOpMode {

    ElapsedTime mainTimer = new ElapsedTime();
    ElapsedTime lapBarTimer = new ElapsedTime();
    ElapsedTime intakeTimer = new ElapsedTime();
    ElapsedTime deflectorTimer = new ElapsedTime();
    ElapsedTime shootTimer = new ElapsedTime();
    ElapsedTime catchplateTimer = new ElapsedTime();
    ElapsedTime armSwingTimer = new ElapsedTime();
    ElapsedTime latchTimer = new ElapsedTime();
    ElapsedTime incrementUpTimer = new ElapsedTime();
    ElapsedTime incrementDownTimer = new ElapsedTime();
    ElapsedTime rsButtonTimer = new ElapsedTime();
    ElapsedTime sideBarTimer = new ElapsedTime();
    ElapsedTime ring3Timer = new ElapsedTime();

    // These are for our "button logic" that turns our buttons into on/off switches
    private boolean bToggle = false;
    private boolean xWasPressed = false;
    private boolean yWasPressed = false;
    private boolean gPad2_AToggle = false;
    private boolean gPad2_BWasPressed = false;
    private boolean gPad2_yWasPressed = false;
    private boolean gPad2_xWasPressed = false;
    private boolean rsButtonWasPressed = false;
    private boolean rightBumperWasPressed = false;
    private boolean ringToggle = false;

    // Some tele-op exclusive variables, explanations commented in their implementations below
    private int reverse = 1;
    private int shootSubtractor = 0;
    private int targetVelocity = 0;
    private int velocityIncrement = 0;
    private double slowMode = 1;
    private int counter = 0;

    @Override
    public void runOpMode(){
        /* Initializes hardware map for the control hub by inheriting our hardware class and running
        its constructor */
        ULTIMATEHardware ULTIMATE = new ULTIMATEHardware(hardwareMap);
        int timerConstant = 350;

        // Initialize some servos
        ULTIMATE.armSwing.setPosition(ULTIMATE.ARMSWING_upPosition);
        ULTIMATE.deflector.setPosition(ULTIMATE.DEFLECTOR_normalPosition);

        waitForStart(); // Above happens in init, below after the start button is pressed
        mainTimer.reset();
        while(opModeIsActive()) {


            // Some telemetry used for debugging and to make sure everything is working as intended

            /*telemetry.addData("slowMode: ", slowMode < 1.0 ? "on" : "off");
            telemetry.addData("avg shoot vel: ", (ULTIMATE.shoot1.getVelocity()
                    + ULTIMATE.shoot2.getVelocity()) / 2);
            telemetry.addData("target vel:  ", targetVelocity);*/
            telemetry.addData("target vel: ", targetVelocity);
            telemetry.addData("time: ", (int) mainTimer.seconds());
            telemetry.update();

            // --------------------------------Primary driver's controls----------------------------


            // Uses the dimensional values of the left and right sticks for our mecanum drive
            double wheelPower = Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y);
            double stickAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);

            stickAngle -= Math.PI / 4;
            double leftFrontPower = wheelPower * Math.cos(stickAngle) + gamepad1.right_stick_x;
            double leftBackPower = wheelPower * Math.sin(stickAngle) + gamepad1.right_stick_x;
            double rightFrontPower = wheelPower * Math.sin(stickAngle) - gamepad1.right_stick_x;
            double rightBackPower = wheelPower * Math.cos(stickAngle) - gamepad1.right_stick_x;
            ULTIMATE.leftF.setPower(leftFrontPower * slowMode);
            ULTIMATE.leftB.setPower(leftBackPower * slowMode);
            ULTIMATE.rightF.setPower(rightFrontPower * slowMode);
            ULTIMATE.rightB.setPower(rightBackPower * slowMode);

            // Automatically sets up for endgame at 100 seconds
            if(((int) mainTimer.seconds() == 100)){
                shootSubtractor = ULTIMATE.shootSubtractorVal;
                ULTIMATE.deflector.setPosition(ULTIMATE.DEFLECTOR_powerShotPosition);
                xWasPressed = true;
            }

            // moves servo that pushes ring from catchplate to shooting wheel
            ULTIMATE.trigger.setPosition(ULTIMATE.TRIGGER_shootPosition * gamepad1.right_trigger);

            // moves the intake DC motors
            if (gamepad1.b && intakeTimer.milliseconds() > timerConstant) {
                bToggle = !bToggle;
                intakeTimer.reset();
            }

            if (bToggle) {
                ULTIMATE.intake1.setPower(reverse);
                ULTIMATE.intake2.setPower(-reverse);
            } else {
                ULTIMATE.intake1.setPower(0);
                ULTIMATE.intake2.setPower(0);
            }

            // moves the deflector
            if (gamepad1.x && deflectorTimer.milliseconds() > timerConstant) {
                if (!xWasPressed) {
                    ULTIMATE.deflector.setPosition(ULTIMATE.DEFLECTOR_powerShotPosition);
                    shootSubtractor = ULTIMATE.shootSubtractorVal;
                    xWasPressed = true;
                } else {
                    ULTIMATE.deflector.setPosition(ULTIMATE.DEFLECTOR_normalPosition);
                    shootSubtractor = 0;
                    xWasPressed = false;
                }
                deflectorTimer.reset();
            }

            // moves the lap bar
            if (gamepad1.y && lapBarTimer.milliseconds() > timerConstant) {
                if (!yWasPressed) {
                    ULTIMATE.lapBar.setPosition(ULTIMATE.LAPBAR_grabPosition);
                    yWasPressed = true;

                } else {
                    ULTIMATE.lapBar.setPosition(ULTIMATE.LAPBAR_releasePosition);
                    yWasPressed = false;
                    sleep(timerConstant);
                }
                lapBarTimer.reset();
            }

            // reverses the intake motors if left bumper is held down and b toggle is true
            if (gamepad1.left_bumper) {
                reverse = -1;
            }
            else{
                reverse = 1;
            }

            // activates slow mode on the drive motors if left trigger is held down
            if(gamepad1.left_trigger > 0){
                slowMode = .55;
            }
            else{
                slowMode = 1;
            }

            if (ULTIMATE.ring3.blue() > ULTIMATE.ring3Threshold && intakeTimer.milliseconds() > 1000
                    && catchplateTimer.milliseconds() > 1000 && shootTimer.milliseconds() > 1000) {
                if(ringToggle){
                    if(ring3Timer.milliseconds() > 120){
                        bToggle = false;
                        gPad2_AToggle = true;
                        ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_shootPosition);
                    }
                }
                else{
                    ring3Timer.reset();
                }
                ringToggle = true;

            }
            else{
                ringToggle = false;
            }

            if(gamepad1.dpad_right){
                ULTIMATE.leftF.setPower(1);
                ULTIMATE.leftB.setPower(-1);
                ULTIMATE.rightF.setPower(-1);
                ULTIMATE.rightB.setPower(1);
            }
            if(gamepad1.dpad_left){
                ULTIMATE.leftF.setPower(-1);
                ULTIMATE.leftB.setPower(1);
                ULTIMATE.rightF.setPower(1);
                ULTIMATE.rightB.setPower(-1);
            }

            if(gamepad1.right_trigger > 0 && mainTimer.seconds() < 100){
                ULTIMATE.sideBar.setPosition(ULTIMATE.SIDEBAR_outPosition);
            }






            //----------------- Accessory driver's controls ----------------------------------------

            // the velocity we set our flywheel motors to
            targetVelocity = ULTIMATE.shootTPS - shootSubtractor + velocityIncrement;

            // toggles on the flywheel motors
            if (gamepad2.a && shootTimer.milliseconds() > timerConstant) {
                gPad2_AToggle = !gPad2_AToggle;
                shootTimer.reset();
            }

            if (gPad2_AToggle) {
                ULTIMATE.shoot1.setVelocity(targetVelocity);
                ULTIMATE.shoot2.setVelocity(targetVelocity);
            } else {
                ULTIMATE.shoot1.setVelocity(0);
                ULTIMATE.shoot2.setVelocity(0);
            }

            // moves the catch plate
            if (gamepad2.b && catchplateTimer.milliseconds() > timerConstant) {
                if (!gPad2_BWasPressed) {
                    ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_intakePosition);
                    gPad2_BWasPressed = true;
                } else {
                    ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_shootPosition);
                    gPad2_BWasPressed = false;
                }
                catchplateTimer.reset();
            }

            // dumps the rings in the catch plate if they load incorrectly
            if (gamepad2.left_bumper) {
                ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_dumpPosition);
            }

            // moves the wobble goal lifter
            if (gamepad2.x && armSwingTimer.milliseconds() > timerConstant) {
                if (!gPad2_xWasPressed) {
                    ULTIMATE.armSwing.setPosition(ULTIMATE.ARMSWING_downPosition);
                    gPad2_xWasPressed = true;
                } else {
                    ULTIMATE.armSwing.setPosition(ULTIMATE.ARMSWING_upPosition);
                    gPad2_xWasPressed = false;
                }
                armSwingTimer.reset();
            }

            // moves wobble goal latch
            if (gamepad2.y && latchTimer.milliseconds() > timerConstant) {
                if (!gPad2_yWasPressed) {
                    ULTIMATE.latch.setPosition(ULTIMATE.LATCH_closedPosition);
                    gPad2_yWasPressed = true;
                } else {
                    ULTIMATE.latch.setPosition(ULTIMATE.LATCH_openPosition);
                    gPad2_yWasPressed = false;
                }
                latchTimer.reset();
            }

            // increases or decreases the velocity of the flywheel by 50 ticks per second each press
            if(gamepad2.dpad_up && incrementUpTimer.milliseconds() > timerConstant){
                velocityIncrement += 50;
                incrementUpTimer.reset();
            }
            if(gamepad2.dpad_down && incrementDownTimer.milliseconds() > timerConstant){
                velocityIncrement -= 50;
                incrementDownTimer.reset();
            }
            // for increased driver efficiency, moves the catch plate AND turns the wheel on or off
            if(gamepad2.right_stick_button && rsButtonTimer.milliseconds() > timerConstant){
                if(!rsButtonWasPressed){
                ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_shootPosition);
                gPad2_BWasPressed = false;
                gPad2_AToggle = true;
                rsButtonWasPressed = true;
                }
                else{
                    ULTIMATE.catchPlate.setPosition(ULTIMATE.CATCHPLATE_intakePosition);
                    gPad2_BWasPressed = true;
                    gPad2_AToggle = false;
                    rsButtonWasPressed = false;
                }
                rsButtonTimer.reset();
            }

            if(gamepad2.right_bumper && sideBarTimer.milliseconds() > timerConstant){
                if(!rightBumperWasPressed){
                    ULTIMATE.sideBar.setPosition(ULTIMATE.SIDEBAR_inPosition);
                    rightBumperWasPressed = true;
                }
                else{
                    ULTIMATE.sideBar.setPosition(ULTIMATE.SIDEBAR_outPosition);
                    rightBumperWasPressed = false;
                }
                sideBarTimer.reset();
            }

            /* Moves the wobble goal arm. We believe that the Vex CR servos we are using for this
            cannot have higher than .5 power which explains the multiplier */
            ULTIMATE.extensionArm1.setPower(gamepad2.left_stick_x*.5);
            ULTIMATE.extensionArm2.setPower(gamepad2.left_stick_x*.5);
            ULTIMATE.tapeMeasure.setPower(gamepad2.right_stick_x);
        }
    }
    }