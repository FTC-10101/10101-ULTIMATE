import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class motorEncoders extends LinearOpMode {

    AutonomousParent auto  = new AutonomousParent(this);
    ULTIMATEHardware ULTIMATE = auto;
    ringDetermination vision = new ringDetermination();
    double powerConstant = .75;

    @Override
    public void runOpMode() throws InterruptedException {


        auto.init();
        ULTIMATE.init(hardwareMap,true,true);
        ULTIMATE.Webcam1.setPipeline(vision);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.

        ULTIMATE.Webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                ULTIMATE.Webcam1.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
        });

        while(!isStarted()) {
            telemetry.addData("Analysis: ", vision.getAnalysis());
            telemetry.addData("Position: ", vision.position);
            telemetry.update();
        }
        telemetry.clearAll();

        ULTIMATE.Webcam1.stopStreaming();

        //initialize latch to hold wobble goal and deflector to shoot
        ULTIMATE.latch.setPosition(1);
        ULTIMATE.catchPlate.setPosition(.855);
        ULTIMATE.deflector.setPosition(0);
        sleep(auto.sleepConstant);

        switch (vision.position) {
            case ONE:
                telemetry.addLine("one");
                telemetry.update();

                /*
                // drive straight until close to B box
                auto.driveEncoders(.65, 3300);
                sleep(auto.sleepConstant);

                // strafe right to get in front of B box
                auto.strafeEncoders(.5,900);
                sleep(auto.sleepConstant);


                // place wobble goal
                dropWobbleGoal();

                // move back to get to our shooting spot
                auto.driveEncoders(powerConstant,-600);
                sleep(auto.sleepConstant);

                // strafe to line up with shooting spot
                auto.strafeEncoders(.5,175);

                // shoot preloaded rings
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.45,450);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1375);
                sleep(300);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(0);
                sleep(800);


                // move forwards to B box
                auto.driveEncoders(powerConstant,2600);
                sleep(200);

                // turn so wobble goal is in box
                auto.turn(-.5,1200);
                sleep(200);

                // move wobble goal back into B box
                auto.driveEncoders(.3,-150);
                sleep(200);

                // let go of wobble goal
                auto.lapBar.setPosition(1);
                sleep(500);

                // drive forward to get away from wobble goal and park
                auto.driveEncoders(1,750);
                sleep(200);


                 */

                // drive straight until close to B box
                auto.driveEncoders(.65, 3200);
                sleep(auto.sleepConstant);

                // strafe right to get in front of B box
                auto.strafeEncoders(.5,900);
                sleep(auto.sleepConstant);


                // place wobble goal
                dropWobbleGoal();

                // move back to get to our shooting spot
                auto.driveEncoders(powerConstant,-725);
                sleep(auto.sleepConstant);

                // strafe to line up with shooting spot
                auto.strafeEncoders(.5,300);

                // shoot preloaded rings
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.45,325);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1305);
                sleep(300);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(1);
                sleep(800);

                // turn on intake and shoot wheel and strafe in front of vision ring
                ULTIMATE.intake.setPower(1);
                ULTIMATE.shoot1.setPower(.65);
                ULTIMATE.shoot2.setPower(.65);
                auto.strafeEncoders(.5, -500);
                sleep(200);

                // drive forward to shooting spot and hopefully pick up ring
                auto.driveEncoders(.8,1100);
                sleep(200);

                // strafe to shooting spot
                auto.strafeEncoders(.3,300);
                sleep(1000);

                // shoot ring
                ULTIMATE.catchPlate.setPosition(.855);
                sleep(700);
                ULTIMATE.trigger.setPosition(.7);
                sleep(200);
                ULTIMATE.trigger.setPosition(0);

                // turn motors we're not using off
                ULTIMATE.intake.setPower(0);
                ULTIMATE.shoot1.setPower(0);
                ULTIMATE.shoot2.setPower(0);

                // move forward to get in front of B box
                auto.driveEncoders(powerConstant,600);
                sleep(200);

                // turn into B box
                auto.turn(.6,1500);
                sleep(200);

                // back into B box
                auto.driveEncoders(.5,-850);
                sleep(200);

                // let go of wobble goal
                ULTIMATE.lapBar.setPosition(0);
                sleep(400);

                auto.driveEncoders(1,300);

                break;

            case FOUR:
                telemetry.addLine("four");
                telemetry.update();

                // drive straight until in front of C box
                auto.driveEncoders(powerConstant, 4000);
                sleep(auto.sleepConstant);

                // place wobble goal
                dropWobbleGoal();

                // move in front of tower goal
                auto.strafeEncoders(.5, 1200);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveEncoders(.5, -1575);
                sleep(auto.sleepConstant);

                // finish OpMode
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.5,375);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1375);
                sleep(200);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(1);
                sleep(1000);

                // drive to C box
                auto.driveEncoders(1,3950);
                sleep(200);

                // turn to move wobble goal in
                auto.turn(-1,500);
                sleep(200);

                // move back into C box
                auto.driveEncoders(1,-1000);
                sleep(200);

                // let go of wobble goal
                ULTIMATE.lapBar.setPosition(0);
                sleep(500);

                // pull forward to make sure we let go of wobble goal
                auto.driveEncoders(1,550);
                sleep(200);

                // park
                auto.strafeEncoders(1,2000);
                break;

            default:
                telemetry.addLine("none");
                telemetry.update();

                // drive straight until in front of A box
                auto.driveEncoders(.7, 2100);
                sleep(auto.sleepConstant);

                // place wobble goal
                dropWobbleGoal();

                // move in front of tower goal
                auto.strafeEncoders(.5, 1250);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveEncoders(powerConstant, 200);
                sleep(auto.sleepConstant);

                // shoot rings into goal
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.5,315);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1250);
                sleep(1000);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(1);
                sleep(1000);

                // drive to A box
                auto.driveEncoders(powerConstant,1900);
                sleep(200);

                // turn to move wobble goal in
                auto.turn(-.3,1350);
                sleep(200);

                // move back into A box
                auto.driveEncoders(.6,-1200);
                sleep(200);

                // let go of wobble goal
                ULTIMATE.lapBar.setPosition(0);
                sleep(500);

                // pull forward to make sure we let go of wobble goal
                auto.driveEncoders(1,900);

        }
        telemetry.clearAll();
        telemetry.addLine("done");
        telemetry.update();
        stop();
    }

    public void dropWobbleGoal(){

        // move arm out to drop wobble goal
        auto.moveExtensionArm(1,1500);
        sleep(300);

        // lower wobble goal holding arm
        ULTIMATE.armSwing.setPosition(1);
        sleep(500);

        // unlatch wobble goal
        ULTIMATE.latch.setPosition(0);
        sleep(200);

        // retract extension arm halfway
        auto.moveExtensionArm(-1,750);
        sleep(300);

        // move holding arm back up
        ULTIMATE.armSwing.setPosition(0);
        sleep(500);

        // retract extension arm fully
        auto.moveExtensionArm(-1,750);
       sleep(500);

        // turn flywheel on
        ULTIMATE.shoot1.setPower(.65);
        ULTIMATE.shoot2.setPower(.65);

    }

    private void shoot(){

        // move the trigger to shoot the preloaded rings three times
        for (int i = 0; i < 3; i++) {
            ULTIMATE.trigger.setPosition(.7);
            sleep(900);
            ULTIMATE.trigger.setPosition(.0);
            sleep(900);
        }

        // turn motors off and put catchplate back to normal position
        ULTIMATE.shoot1.setPower(0);
        ULTIMATE.shoot2.setPower(0);
        ULTIMATE.catchPlate.setPosition(1);
        sleep(auto.sleepConstant);

    }
}


