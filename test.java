import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {

    AutonomousParent auto  = new AutonomousParent(this);
    ULTIMATEHardware ULTIMATE_AUTO = auto;
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();



    public void runOpMode(){


        auto.init();
        ULTIMATE_AUTO.init(hardwareMap,true,false);

        //All the required parameters for IMU
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        //Finds the BNO055IMU class and gets its information
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Initializes IMU Parameters
        imu.initialize(imuparameters);

        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.update();








        waitForStart();

        auto.turnEncoders(1,1000);
        sleep(8000);
        auto.turnEncoders(1,1000);
        sleep(8000);
        auto.turnEncoders(1,1000);
        sleep(8000);


        stop();

    }

    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);

        ULTIMATE_AUTO.globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */

    //We experimentally determined the Z axis is the axis we want to use for heading angle.
    // We have to process the angle because the imu works in euler angles so the Z axis is
    // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
    // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

    private double getAngle()
    {

        Orientation angles = imu.getAngularOrientation
                (AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        ULTIMATE_AUTO.globalAngle += deltaAngle;

        lastAngles = angles;

        return -ULTIMATE_AUTO.globalAngle;
    }


    //See if we are moving in a straight line and if not return a power correction value.
    //return power adjustment, + is adjust left - is adjust right.
    private double checkDirection()
    {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction;
        double angle;
        double gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }



    // This rotates left or right a number of degrees. Does not support turning more than 180 degrees.
    //Degrees to turn, + is left - is right
    private void rotate(int degrees, double power)
    {
        double leftPower;
        double rightPower;
        //Restarts IMU for tracking
        resetAngle();
        // getAngle() returns + when turning left and - when turning right
        // clockwise (right).
        if (degrees < 0) {
            // turn right.
            leftPower = -power;
            rightPower = power;
        } else if (degrees > 0){
            // turn left.
            leftPower = power;
            rightPower = -power;
        } else
            return;
        // set power to rotate.
        ULTIMATE_AUTO.leftF.setPower(leftPower);
        ULTIMATE_AUTO.leftB.setPower(leftPower);
        ULTIMATE_AUTO.rightF.setPower(rightPower);
        ULTIMATE_AUTO.rightB.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {
                telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES));
                telemetry.addData("angle",getAngle());
                telemetry.update();
            }
            while (opModeIsActive() && getAngle() > degrees) {
                telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZYX,AngleUnit.DEGREES));
                telemetry.addData("angle",getAngle());
                telemetry.update();
            }
        } else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {
                telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZYX, AngleUnit.DEGREES));
                telemetry.addData("angle",getAngle());
                telemetry.update();
            }
        // turn the motors off.
        auto.halt();
        // wait for rotation to stop.
        sleep(1000);
        // reset angle for new heading
        resetAngle();
    }


}