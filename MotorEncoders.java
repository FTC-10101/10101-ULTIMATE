/*Our autonomous OpMode. See Engineering Notebook*/
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class MotorEncoders extends LinearOpMode {

    AutonomousParent auto;
    RingDetermination vision = new RingDetermination();
    @Override
    public void runOpMode(){

        auto  = new AutonomousParent(this,hardwareMap);

        auto.Webcam1.setPipeline(vision);

        auto.Webcam1.openCameraDeviceAsync(() ->
                auto.Webcam1.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT));

        // Analyze rings in a loop until start button is pressed
        while(!isStarted()) {
            telemetry.addData("Analysis: ", vision.getAnalysis());
            telemetry.addData("# of rings: ", vision.position);
            telemetry.update();
        }
        telemetry.clearAll();

        auto.Webcam1.stopStreaming();

        //initialize some servos
        auto.latch.setPosition(auto.LATCH_closedPosition);
        auto.lapBar.setPosition(auto.LAPBAR_releasePosition);
        auto.deflector.setPosition(auto.DEFLECTOR_normalPosition);
        auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
        auto.sideBar.setPosition(auto.SIDEBAR_inPosition);

        switch (vision.position) {
            case ONE:
                telemetry.addLine("one");
                telemetry.update();

                // drive straight until in front of A box
                auto.driveEncoders(.2,.75, 2555);
                sleep(250);

                // turn wheel on early to make sure it has enough time to get up to speed
                auto.setVelocityFlywheel(auto.shootTPS);

                // move in front of tower goal
                auto.strafeEncoders(.2,.6, 1100);
                sleep(50);

                auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
                sleep(1000);

                // shoot rings into goal
                auto.shoot(4);

                // drive straight until close to B box
                auto.driveEncoders(.2,.75,900);
                sleep(50);

                // move arm out to drop wobble goal
                auto.armEncoders(31000);

                // lower wobble goal holding arm
                auto.armSwing.setPosition(auto.ARMSWING_downPosition);
                sleep(300);

                // unlatch wobble goal and put wobble goal holding arm back up
                auto.latch.setPosition(0);
                sleep(100);
                auto.armSwing.setPosition(auto.ARMSWING_upPosition);

                // strafe to move in front of wobble goal while retracting wobble goal arm
                auto.strafeEncodersWithArm(.2,.3,500,-15000);
                sleep(50);

                // move back to second wobble goal while still retracting wobble goal arm
                auto.driveEncodersWithArm(.2,.65,-2500,-15000);
                sleep(50);

                // square up with wobble goal
                auto.strafeEncoders(.2,-100);
                sleep(50);

                // back up to wobble goal slowly to make sure we don't knock it over
                auto.driveEncoders(.2,-200);
                sleep(50);

                // move wobble goal holder down
                auto.lapBar.setPosition(auto.LAPBAR_grabPosition);
                sleep(200);

                // turn on intake and shoot wheel and strafe in front of vision ring
                auto.setPowerIntake(1);
                auto.setVelocityFlywheel(auto.shootTPS);
                auto.strafeEncoders(.2,.5, -435);
                sleep(50);

                // drive forward to shooting spot and hopefully pick up ring simultaneously
                auto.driveEncoders(.2,1,1350);
                sleep(300);

                // strafe to shooting spot and prepare to shoot
                sleep(1000);
                auto.setPowerIntake(0);
                auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
                sleep(400);

                // shoot ring
                auto.shoot(1);

                // turn motors we're not using off
                auto.setVelocityFlywheel(0);

                // move forward to get in front of B box
                auto.driveEncoders(.2,1,600);
                sleep(50);

                // turn into B box
                auto.turnEncoders(1,1825);
                sleep(50);

                // back into B box
                auto.driveEncoders(.65,-220);
                sleep(50);

                // let go of wobble goal
                auto.lapBar.setPosition(auto.LAPBAR_releasePosition);
                sleep(200);

                // park
                auto.driveEncoders(1,300);
                break;

            case FOUR:
                telemetry.addLine("four");
                telemetry.update();

                // drive to C box while extending wobble goal arm
                auto.driveEncodersWithArm(.2,.75,4200,40000);
                sleep(300);

                // lower wobble goal swing mechanism and unlatch
                auto.armSwing.setPosition(auto.ARMSWING_downPosition);
                sleep(100);
                auto.latch.setPosition(auto.LATCH_openPosition);
                sleep(350);

                // turn wheel on early to be sure it has enough time to get up to speed
                auto.setVelocityFlywheel(auto.shootTPS);

                // strafe to be in line with shooting spot while also retracting wobble goal arm
                auto.armSwing.setPosition(auto.ARMSWING_upPosition);
                auto.strafeEncodersWithArm(.2,.4,1250,-40000);
                auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
                sleep(50);

                // attempt to move to our shooting spot
                auto.driveEncoders(.2,.75, -1600);
                sleep(300);

                // shoot rings
                auto.shoot(4);

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.2,.5,505);
                sleep(50);

                // move back to second wobble goal
                auto.driveEncoders(.2,.8,-1200);


                // move slowly to make sure we aren't knocking it over
                auto.driveEncoders(.2,-325);
                sleep(50);

                // move wobble goal holder down
                auto.lapBar.setPosition(auto.LAPBAR_grabPosition);
                sleep(400);

                // turn wheel on early to be sure it has enough time to get up to speed
                auto.setVelocityFlywheel(auto.shootTPS);

                // strafe to line up with starting stack
                auto.strafeEncoders(.2,.5,-515);
                sleep(100);

                // bump stack to knock it over, making it easier to intake the rings
                auto.setPowerIntake(1);
                sleep(250);
                auto.driveEncoders(1,750);

                // back up a little because sometimes it drives over the rings in previous movement
                auto.driveEncoders(.6,-175);
                sleep(100);

                // drive forward to intake 2-3 rings
                auto.driveEncoders(.35,650);
                auto.driveEncoders(.35,-100);
                auto.driveEncoders(.35,150);

                // long delay to make sure the rings have enough time to move through intake
                sleep(1050);
                auto.setPowerIntake(0);
                sleep(100);

                // prepare to shoot
                auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
                sleep(800); // long delay because the servo is slow

                // shoot
                auto.shoot(3);
                sleep(200);



                // drive to C box
                auto.driveEncoders(.5,1,2370);
                sleep(50);

                // turn right so the wobble goal is in the box
                auto.turnEncoders(1,-850);


                // back up to make sure it's all the way in
                auto.driveEncoders(1,-900);


                // let go of wobble goal
                auto.lapBar.setPosition(auto.LAPBAR_releasePosition);
                sleep(150);


                auto.tapeMeasure.setPower(1);
                auto.sideBar.setPosition(auto.SIDEBAR_outPosition);


                // make sure we aren't touching either wobble goal
                auto.driveEncoders(1,500);




                auto.setPowerIntake(0);
                auto.strafeEncoders(1,1000);


                break;

            default:
                telemetry.addLine("none");
                telemetry.update();

                // drive straight until in front of A box
                auto.driveEncoders(.2,.75, 2200);
                sleep(100);

                // move arm out to drop wobble goal
                auto.armEncoders(30000);
                sleep(50);

                // lower wobble goal holding arm
                auto.armSwing.setPosition(auto.ARMSWING_downPosition);
                sleep(500);

                // unlatch wobble goal
                auto.latch.setPosition(0);
                sleep(200);

                // retract extension arm halfway
                auto.armEncoders(-15000);

                // move holding arm back up
                auto.armSwing.setPosition(auto.ARMSWING_upPosition);
                sleep(500);

                // retract extension arm fully
                auto.armEncoders(-10000);
                sleep(500);

                // turn flywheel on
                auto.setVelocityFlywheel(auto.shootTPS);

                // move in front of tower goal
                auto.strafeEncoders(.2,.6, 1150);
                sleep(100);

                // attempt to move to our shooting spot
                auto.driveEncoders(.2,.5, 200);
                sleep(100);

                auto.catchPlate.setPosition(auto.CATCHPLATE_shootPosition);
                sleep(1100);

                // shoot rings into goal
                auto.shoot(4);

                // turn motors off and put catch plate back to intake position
                auto.setVelocityFlywheel(0);
                auto.catchPlate.setPosition(auto.CATCHPLATE_intakePosition);
                sleep(300);

                // strafe to move in front of wobble goal
                auto.strafeEncoders(.2,.6,470);
                sleep(50);

                // move back to second wobble goal
                auto.driveEncoders(.2,1,-1100);

                // slow down so we don't knock it over
                auto.driveEncoders(.2,-370);

                // move wobble goal holder down
                auto.lapBar.setPosition(auto.LAPBAR_grabPosition);
                sleep(600);

                // drive to A box
                auto.driveEncoders(.2,1,1755);
                sleep(100);

                // turn to move wobble goal in
                auto.turnEncoders(1,-850);
                sleep(100);

                // move back into A box
                auto.driveEncoders(.2, 1,-950);
                sleep(100);

                // let go of wobble goal
                auto.lapBar.setPosition(auto.LAPBAR_releasePosition);
                sleep(300);

                // pull forward to make sure we let go of wobble goal
                auto.driveEncoders(1,650);
        }
        telemetry.clearAll();
        telemetry.addLine("done");
        telemetry.update();
        stop();
    }
}