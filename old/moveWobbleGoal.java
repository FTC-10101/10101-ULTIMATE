/*import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class moveWobbleGoal extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    ringDetermination vision = new ringDetermination();
    int sleepConstant = 1400;
    double powerConstant = .6;

        @Override
        public void runOpMode() throws InterruptedException {


            ULTIMATE.init(hardwareMap, false,true,false);
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
            ULTIMATE.deflector.setPosition(0);
            sleep(sleepConstant);

            switch (vision.position) {
                case ONE:
                    telemetry.addLine("one");
                    telemetry.update();



                    // drive straight until in front of A box
                    drive(powerConstant, 1100);
                    sleep(sleepConstant);

                    // strafe right to get in front of B box
                    strafe(powerConstant,1150);
                    sleep(sleepConstant);

                    // move forward closer to B box
                    drive(powerConstant,300);
                    sleep(sleepConstant);

                    // place wobble goal
                    dropWobbleGoal();

                    // move back to get to our shooting spot
                    drive(-powerConstant,225);
                    sleep(sleepConstant);

                    // finish OpMode
                    rest();
                    break;

                case FOUR:
                    telemetry.addLine("four");
                    telemetry.update();

                    // drive straight until in front of C box
                    drive(powerConstant, 2225);
                    sleep(sleepConstant);

                    // place wobble goal
                    dropWobbleGoal();

                    // move in front of tower goal
                    strafe(powerConstant, 950);
                    sleep(sleepConstant);

                    // attempt to move to our shooting spot
                    drive(-powerConstant, 600);
                    sleep(sleepConstant);

                    // finish OpMode
                    rest();
                    break;

                default:
                    telemetry.addLine("none");
                    telemetry.update();


                    // drive straight until in front of A box
                    drive(powerConstant, 1025);
                    sleep(sleepConstant);

                    // place wobble goal
                    dropWobbleGoal();

                    // move in front of tower goal
                    strafe(.6, 1150);
                    sleep(sleepConstant);

                    // attempt to move to our shooting spot
                    drive(powerConstant, 155);
                    sleep(sleepConstant);

                    // finish OpMode
                    rest();
            }
            telemetry.clearAll();
            telemetry.addLine("done");
            telemetry.update();
        }

    private void drive (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
        halt();
    }

    private void strafe (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
        halt();
    }
    private void halt(){
        ULTIMATE.leftF.setPower(0);
        ULTIMATE.leftB.setPower(0);
        ULTIMATE.rightF.setPower(0);
        ULTIMATE.rightB.setPower(0);
    }

    private void moveExtensionArm (double power, int time){
        ULTIMATE.extensionArm.setPower(power);
        sleep(time);
        ULTIMATE.extensionArm.setPower(0);
    }

    private void dropWobbleGoal(){

        // move arm out to drop wobble goal
        moveExtensionArm(1,1500);
        sleep(sleepConstant);

        // lower wobble goal holding arm
        ULTIMATE.armSwing.setPosition(1);
        sleep(sleepConstant);

        // unlatch wobble goal
        ULTIMATE.latch.setPosition(0);
        sleep(200);

        // retract extension arm halfway
        moveExtensionArm(-1,750);
        sleep(sleepConstant);

        // move holding arm back up
        ULTIMATE.armSwing.setPosition(0);
        sleep(sleepConstant);

        // retract extension arm fully
        moveExtensionArm(-1,750);
        sleep(sleepConstant);

    }

    private void rest(){

            // turn flywheel on
        ULTIMATE.shoot1.setPower(.64);
        ULTIMATE.shoot2.setPower(.64);

        // move catchplate into shooting position
        ULTIMATE.catchPlate.setPosition(.85);
        sleep(sleepConstant);

        // move the trigger to shoot the preloaded rings three times
        for (int i = 0; i < 3; i++) {
            ULTIMATE.trigger.setPosition(.8);
            sleep(800);
            ULTIMATE.trigger.setPosition(.0);
            sleep(800);
        }

        // park
        drive(1, 150);
        sleep(sleepConstant);

        // turn motors off and put catchplate back to normal position
        ULTIMATE.shoot1.setPower(0);
        ULTIMATE.shoot2.setPower(0);
        ULTIMATE.catchPlate.setPosition(1);
        sleep(sleepConstant);

        // makes sure the program ends correctly
        stop();
    }
}
*/
