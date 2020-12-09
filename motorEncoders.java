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

                // drive straight until in front of A box
                auto.driveEncoders(.65, 2250);
                sleep(auto.sleepConstant);

                // strafe right to get in front of B box
                auto.strafeEncoders(powerConstant,1100);
                sleep(auto.sleepConstant);

                // move forward closer to B box
                auto.driveEncoders(powerConstant,900);
                sleep(200);

                // place wobble goal
                dropWobbleGoal();

                // move back to get to our shooting spot
                auto.driveEncoders(.45,-850);
                sleep(auto.sleepConstant);

                // finish OpMode
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.45,450);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1200);
                sleep(300);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(0);
                sleep(800);


                // move forwards to B box
                auto.driveEncoders(powerConstant,2600);
                sleep(200);

                auto.turn(-.5,1200);
                sleep(200);

                auto.driveEncoders(.3,-300);
                sleep(200);

                auto.lapBar.setPosition(1);
                sleep(500);

                auto.driveEncoders(1,750);
                sleep(200);

                auto.strafeEncoders(1,1000);
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
                auto.strafeEncoders(.5, 1100);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveEncoders(.5, -1600);
                sleep(auto.sleepConstant);

                // finish OpMode
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.5,450);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1300);
                sleep(200);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(0);
                sleep(1000);

                // drive to C box
                auto.driveEncoders(1,3950);
                sleep(200);

                // turn to move wobble goal in
                auto.turn(-1,500);
                sleep(200);

                // move back into C box
                auto.driveEncoders(1,-850);
                sleep(200);

                // let go of wobble goal
                ULTIMATE.lapBar.setPosition(1);
                sleep(500);

                // pull forward to make sure we let go of wobble goal
                auto.driveEncoders(1,400);
                sleep(200);

                // park
                auto.strafeEncoders(1,2000);
                break;

            default:
                telemetry.addLine("none");
                telemetry.update();

                // drive straight until in front of A box
                auto.driveEncoders(powerConstant, 2250);
                sleep(auto.sleepConstant);

                // place wobble goal
                dropWobbleGoal();

                // move in front of tower goal
                auto.strafeEncoders(.5, 1050);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveEncoders(powerConstant, 200);
                sleep(auto.sleepConstant);

                // shoot rings into goal
                shoot();

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.5,400);

                // move back to second wobble goal
                auto.driveEncoders(powerConstant,-1350);
                sleep(1000);

                // move wobble goal holder down
                ULTIMATE.lapBar.setPosition(0);
                sleep(1000);

                // drive to A box
                auto.driveEncoders(powerConstant,2225);
                sleep(200);

                // turn to move wobble goal in
                auto.turn(-.3,1400);
                sleep(200);

                // move back into A box
                auto.driveEncoders(.6,-1200);
                sleep(200);

                // let go of wobble goal
                ULTIMATE.lapBar.setPosition(1);
                sleep(500);

                // pull forward to make sure we let go of wobble goal
                auto.driveEncoders(1,600);

        }
        stop();
        telemetry.clearAll();
        telemetry.addLine("done");
        telemetry.update();
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
        ULTIMATE.shoot1.setPower(.67);
        ULTIMATE.shoot2.setPower(.67);

    }

    private void shoot(){

        // move the trigger to shoot the preloaded rings three times
        for (int i = 0; i < 3; i++) {
            ULTIMATE.trigger.setPosition(.8);
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


