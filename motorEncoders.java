import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class motorEncoders extends LinearOpMode {

    AutonomousParent auto  = new AutonomousParent(this);
    ULTIMATEHardware ULTIMATE = auto;
    ringDetermination vision = new ringDetermination();
    double powerConstant = .5;

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
        ULTIMATE.deflector.setPosition(0);
        sleep(auto.sleepConstant);

        switch (vision.position) {
            case ONE:
                telemetry.addLine("one");
                telemetry.update();



                // drive straight until in front of A box
                auto.driveEncoders(powerConstant,2500);
                sleep(auto.sleepConstant);

                // strafe right to get in front of B box
                auto.strafe(powerConstant,1150);
                sleep(auto.sleepConstant);

                // move forward closer to B box
                auto.driveEncoders(powerConstant,200);
                sleep(auto.sleepConstant);

                // place wobble goal
                auto.dropWobbleGoal();

                // move back to get to our shooting spot
                auto.driveEncoders(-powerConstant,200);
                sleep(auto.sleepConstant);

                // finish OpMode
                rest();
                break;

            case FOUR:
                telemetry.addLine("four");
                telemetry.update();

                // drive straight until in front of C box
                auto.driveTime(powerConstant, 2225);
                sleep(auto.sleepConstant);

                // place wobble goal
                auto.dropWobbleGoal();

                // move in front of tower goal
                auto.strafe(powerConstant, 950);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveTime(-powerConstant, 600);
                sleep(auto.sleepConstant);

                // finish OpMode
                rest();
                break;

            default:
                telemetry.addLine("none");
                telemetry.update();


                // drive straight until in front of A box
                auto.driveTime(powerConstant, 1500);
                sleep(auto.sleepConstant);

                // place wobble goal
                auto.dropWobbleGoal();

                // move in front of tower goal
                auto.strafe(.6, 1150);
                sleep(auto.sleepConstant);

                // attempt to move to our shooting spot
                auto.driveTime(powerConstant, 155);
                sleep(auto.sleepConstant);

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


    private void rest(){

        // turn flywheel on
        ULTIMATE.shoot1.setPower(.64);
        ULTIMATE.shoot2.setPower(.64);

        // move catchplate into shooting position
        ULTIMATE.catchPlate.setPosition(.85);
        sleep(auto.sleepConstant);

        // move the trigger to shoot the preloaded rings three times
        for (int i = 0; i < 3; i++) {
            ULTIMATE.trigger.setPosition(.8);
            sleep(800);
            ULTIMATE.trigger.setPosition(.0);
            sleep(800);
        }

        // park
        auto.driveEncoders(1, 150);
        sleep(auto.sleepConstant);

        // turn motors off and put catchplate back to normal position
        ULTIMATE.shoot1.setPower(0);
        ULTIMATE.shoot2.setPower(0);
        ULTIMATE.catchPlate.setPosition(1);
        sleep(auto.sleepConstant);

        // makes sure the program ends correctly
        stop();
    }
}


