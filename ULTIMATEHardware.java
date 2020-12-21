import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
@TeleOp
public class ULTIMATEHardware {

    // Hardware Map. Android Studio says this line is unnecessary, but it is in every hardware class
    // we have made before. Eventually I will test it to see what's up, but for now, I don't
    // understand this line. Need to ask Jacob or Zade

    // DC motors
    public DcMotor leftF, leftB, rightF, rightB, // drive motors
            shoot1, shoot2, intake, // intake motor
            extensionArm; // arm for wobble goal



    // Servos
    public Servo catchPlate, trigger, deflector, armSwing, latch, lapBar, rearLatch;

    // Inertial Measuring Unit
    public BNO055IMU imu;

    // External Webcam
    OpenCvCamera Webcam1;

    double globalAngle = 0;

    public void init(HardwareMap ulthw, boolean encoders, boolean camera) {

        leftB = ulthw.dcMotor.get("leftB");
        rightB = ulthw.dcMotor.get("rightB");
        rightF = ulthw.dcMotor.get("rightF");
        leftF = ulthw.dcMotor.get("leftF");
        shoot1 = ulthw.dcMotor.get("shoot1");
        shoot2 = ulthw.dcMotor.get("shoot2");
        intake = ulthw.dcMotor.get("intake");
        extensionArm = ulthw.dcMotor.get("extensionArm");

        trigger = ulthw.servo.get("trigger");
        catchPlate = ulthw.servo.get("catchPlate");
        deflector = ulthw.servo.get("deflector");
        armSwing = ulthw.servo.get("armSwing");
        latch = ulthw.servo.get("latch");
        lapBar = ulthw.servo.get("lapBar");
        rearLatch = ulthw.servo.get("rearLatch");

        imu = ulthw.get(BNO055IMU.class, "imu");

        // Don't create camera object if not using in a particular instance
        if(camera) {
            int cameraMonitorViewId = ulthw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", ulthw.appContext.getPackageName());
            Webcam1 = OpenCvCameraFactory.getInstance().createWebcam(ulthw.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        }
        // This is the best way to account for motors being mounted in the opposite direction.
        // Instead of having to use a negative sign for the motor power value all the time, I can
        // just write these lines are forget about it. It also makes our programs more readable
        shoot1.setDirection(DcMotor.Direction.REVERSE);
        shoot2.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        leftF.setDirection(DcMotor.Direction.REVERSE);
        leftB.setDirection(DcMotor.Direction.REVERSE);

        // Don't use encoders if not using in a particular instance
        if (encoders) {
            rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            rightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // Use encoders on flywheel to access built in PID
        shoot1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }
}
