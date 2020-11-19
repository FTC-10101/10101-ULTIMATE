import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class motorEncoders extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    ringDetermination vision = new ringDetermination();
    OpenCvCamera Webcam1;
    int sleepConstant = 1700;
    double powerConstant = .6;

    @Override
    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        Webcam1 = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        Webcam1.setPipeline(vision);

        ULTIMATE.init(hardwareMap);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.

        Webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                Webcam1.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
        });

        while(!isStarted()) {
            telemetry.addData("test: ", vision.getAnalysis());
            telemetry.addData("test2: ", vision.position);
            telemetry.update();
        }

        waitForStart();
        Webcam1.stopStreaming();

        switch (vision.position) {
            case NONE:
                telemetry.addLine("none");

                //initialize latch to hold wobble goal
                ULTIMATE.latch.setPosition(1);
                sleep(sleepConstant);

                // drive straight until in front of A box
                drive(powerConstant, 900);
                sleep(sleepConstant);

                // move arm out to drop wobble goal
                moveExtensionArm(1,1500);
                sleep(sleepConstant);

                // lower wobble goal holding arm
                ULTIMATE.armSwing.setPosition(1);
                sleep(sleepConstant);

                // unlatch wobble goal
                ULTIMATE.latch.setPosition(0);
                sleep(200);

                // retract extension arm
                moveExtensionArm(-1,1500);
                sleep(sleepConstant);

                // move holding arm back up
                ULTIMATE.armSwing.setPosition(0);
                sleep(sleepConstant);

                // move in front of tower goal
                strafe(.6, 1150);
                sleep(sleepConstant);

                // attempt to move to our shooting spot
                drive(powerConstant, 200);
                sleep(sleepConstant);




                // finish program
                rest();
                break;

            case ONE:
                telemetry.addLine("one");

                //initialize latch to hold wobble goal
                ULTIMATE.latch.setPosition(1);
                sleep(sleepConstant);

                // drive straight until in front of A box
                drive(powerConstant, 1100);
                sleep(sleepConstant);

                // strafe right to get in front of B box
                strafe(.6,1150);
                sleep(sleepConstant);

                // move forward closer to B box
                drive(powerConstant,300);
                sleep(sleepConstant);

                // move arm out to drop wobble goal
                moveExtensionArm(1,1500);
                sleep(sleepConstant);

                // lower wobble goal holding arm
                ULTIMATE.armSwing.setPosition(1);
                sleep(sleepConstant);

                // unlatch wobble goal
                ULTIMATE.latch.setPosition(0);
                sleep(500);

                // retract extension arm
                moveExtensionArm(-1,1500);
                sleep(sleepConstant);

                // move holding arm back up
                ULTIMATE.armSwing.setPosition(0);
                sleep(sleepConstant);

                // move back to get to our shooting spot
                drive(-powerConstant,225);
                sleep(sleepConstant);

                // finish program
                rest();
                break;

            case FOUR:
                telemetry.addLine("four");

                //initialize latch to hold wobble goal
                ULTIMATE.latch.setPosition(1);
                sleep(sleepConstant);

                // drive straight until in front of C box
                drive(powerConstant, 2100);
                sleep(sleepConstant);

                // move arm out to drop wobble goal
                moveExtensionArm(1,1500);
                sleep(sleepConstant);

                // lower wobble goal holding arm
                ULTIMATE.armSwing.setPosition(1);
                sleep(sleepConstant);

                // unlatch wobble goal
                ULTIMATE.latch.setPosition(0);
                sleep(200);

                // retract extension arm
                moveExtensionArm(-1, 1500);
                sleep(sleepConstant);

                // move holding arm back up
                ULTIMATE.armSwing.setPosition(0);
                sleep(sleepConstant);

                // move back slightly as insurance that we'll clear wobble goal
                drive(-powerConstant,100);
                sleep(sleepConstant);

                // move in front of tower goal
                strafe(.6, 950);
                sleep(sleepConstant);

                // attempt to move to our shooting spot
                drive(-powerConstant, 600);
                sleep(sleepConstant);

                // finish program
                rest();

            default:
                telemetry.addLine("Problem with vision. Defaulting to A box");

                //initialize latch to hold wobble goal
                ULTIMATE.latch.setPosition(1);
                sleep(sleepConstant);

                // drive straight until in front of A box
                drive(powerConstant, 1100);
                sleep(sleepConstant);

                // move arm out to drop wobble goal
                moveExtensionArm(1,1500);
                sleep(sleepConstant);

                // lower wobble goal holding arm
                ULTIMATE.armSwing.setPosition(1);
                sleep(sleepConstant);

                // unlatch wobble goal
                ULTIMATE.latch.setPosition(0);
                sleep(200);

                // retract extension arm
                moveExtensionArm(-1,1500);
                sleep(sleepConstant);

                // move holding arm back up
                ULTIMATE.armSwing.setPosition(0);
                sleep(sleepConstant);

                // move back slightly as insurance that we'll clear wobble goal
                drive(-powerConstant,100);
                sleep(sleepConstant);

                // move in front of tower goal
                strafe(.6, 900);
                sleep(sleepConstant);

                // attempt to move to our shooting spot
                drive(powerConstant, 275);
                sleep(sleepConstant);
        }

        // finish program
        rest();
    }

    private void drive (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
        ULTIMATE.leftF.setPower(0);
        ULTIMATE.leftB.setPower(0);
        ULTIMATE.rightF.setPower(0);
        ULTIMATE.rightB.setPower(0);
    }

    private void strafe (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
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
        ULTIMATE.shoot1.setPower(.6);
        ULTIMATE.shoot2.setPower(.6);

        // move catchplate into shooting position
        ULTIMATE.catchPlate.setPosition(.85);
        sleep(sleepConstant);

        // move the trigger to shoot the preloaded rings three times
        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(.0);
        sleep(1000);
        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(.0);
        sleep(1000);
        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(.0);
        sleep(100);

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


