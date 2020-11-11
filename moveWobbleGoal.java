import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class moveWobbleGoal extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    SkystoneDeterminationPipeline vision = new SkystoneDeterminationPipeline();
    OpenCvCamera Webcam1;
    int sleepConstant = 2000;



        @Override
        public void runOpMode() throws InterruptedException {


            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            Webcam1 = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
            vision = new SkystoneDeterminationPipeline();
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

                sleep(100);
            }

            waitForStart();

            Webcam1.stopStreaming();
            switch (vision.position) {
                case NONE:
                    telemetry.addLine("none");
                    ULTIMATE.latch.setPosition(1);
                    sleep(sleepConstant);
                    moveForward(.3, 3250);
                    sleep(sleepConstant);

                    ULTIMATE.extensionArm.setPower(1);
                    sleep(1500);
                    ULTIMATE.extensionArm.setPower(0);
                    sleep(sleepConstant);
                    ULTIMATE.armSwing.setPosition(1);
                    sleep(sleepConstant);
                    ULTIMATE.latch.setPosition(0);
                    sleep(200);

                    break;
                case ONE:
                    telemetry.addLine("one");

                    ULTIMATE.latch.setPosition(1);
                    sleep(sleepConstant);
                    moveForward(.3, 3250);
                    sleep(sleepConstant);
                    strafeRight(.5,1000);
                    sleep(sleepConstant);
                    moveForward(.3,1000);
                    sleep(sleepConstant);
                    ULTIMATE.extensionArm.setPower(1);
                    sleep(1500);
                    ULTIMATE.extensionArm.setPower(0);
                    sleep(sleepConstant);
                    ULTIMATE.armSwing.setPosition(1);
                    sleep(sleepConstant);
                    ULTIMATE.latch.setPosition(0);
                    sleep(500);
                    moveBackward(.3,800);
                    sleep(sleepConstant);

                    ULTIMATE.latch.setPosition(1);
                    break;
                case FOUR:
                    telemetry.addLine("four");
                    ULTIMATE.latch.setPosition(1);
                    sleep(sleepConstant);
                    moveForward(.3, 6500);
                    sleep(sleepConstant);

                    ULTIMATE.extensionArm.setPower(1);
                    sleep(1500);
                    ULTIMATE.extensionArm.setPower(0);
                    sleep(sleepConstant);
                    ULTIMATE.armSwing.setPosition(1);
                    sleep(sleepConstant);
                    ULTIMATE.latch.setPosition(0);
                    sleep(200);
                    break;
                default:
                    telemetry.addLine("Problem with vision");

            }

        }

    private void moveForward (double power, int time){
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

    private void moveBackward (double power, int time){
        ULTIMATE.leftF.setPower(-power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(-power);
        sleep(time);
        ULTIMATE.leftF.setPower(0);
        ULTIMATE.leftB.setPower(0);
        ULTIMATE.rightF.setPower(0);
        ULTIMATE.rightB.setPower(0);
    }


    private void strafeRight (double power, int time){
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
}