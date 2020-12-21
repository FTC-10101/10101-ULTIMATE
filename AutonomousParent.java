import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AutonomousParent extends ULTIMATEHardware {

    private final LinearOpMode lOpMode;
    int sleepConstant = 400;
    Orientation robotAngle = new Orientation();
    public double globalAngle;

    public void init() {
        init(lOpMode.hardwareMap, true, false);
        leftB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public AutonomousParent(LinearOpMode lom) {
        this.lOpMode = lom;
    }

    public void drive(double power) {
        leftF.setPower(power);
        leftB.setPower(power);
        rightF.setPower(power);
        rightB.setPower(power);
    }

    public void driveTime(double power, int time) {
        drive(power);
        lOpMode.sleep(time);
        halt();
    }

    public void strafe(double power) {
        leftF.setPower(power);
        leftB.setPower(-power);
        rightF.setPower(-power);
        rightB.setPower(power);
    }

    public void strafeTime(double power, int time) {
        leftF.setPower(power);
        leftB.setPower(-power);
        rightF.setPower(-power);
        rightB.setPower(power);
        lOpMode.sleep(time);
        halt();
    }

    public void turn(double power) {
        leftF.setPower(-power);
        leftB.setPower(-power);
        rightF.setPower(power);
        rightB.setPower(power);
    }

    public void turn(double power, int time) {
        leftF.setPower(-power);
        leftB.setPower(-power);
        rightF.setPower(power);
        rightB.setPower(power);
        lOpMode.sleep(time);
        halt();
    }

    public void halt() {
        leftF.setPower(0);
        leftB.setPower(0);
        rightF.setPower(0);
        rightB.setPower(0);
    }

    public void driveEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(distance);
        rightF.setTargetPosition(distance);
        leftF.setTargetPosition(distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(power);

        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
        }


        halt();
        lOpMode.telemetry.clearAll();

        rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(-distance);
        leftF.setTargetPosition(distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        strafe(power);

        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
        }


        halt();
        lOpMode.telemetry.clearAll();

        rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void moveExtensionArm(double power, int time) {
        extensionArm.setPower(power);
        lOpMode.sleep(time);
        extensionArm.setPower(0);
    }


    public void init_IMU() {
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        imu.initialize(imuparameters);
    }


    public void turnEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(distance);
        leftF.setTargetPosition(-distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        strafe(power);

        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
        }

        halt();
        lOpMode.telemetry.clearAll();

        rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

}
