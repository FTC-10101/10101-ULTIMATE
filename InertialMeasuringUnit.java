import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class InertialMeasuringUnit extends ULTIMATEHardware {

    Orientation lastAngles = new Orientation();

    private final LinearOpMode lOpMode;

    public double globalAngle;

    public InertialMeasuringUnit (LinearOpMode lom){
        this.lOpMode = lom;
    }

    public void init_IMU(){
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        imu.initialize(imuparameters);
    }

    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);

        globalAngle = 0;
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

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
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
        leftF.setPower(leftPower);
        leftB.setPower(leftPower);
        rightF.setPower(rightPower);
        rightB.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (lOpMode.opModeIsActive() && getAngle() == 0) {
                lOpMode.telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,AngleUnit.DEGREES));
                lOpMode.telemetry.update();
            }
            while (lOpMode.opModeIsActive() && getAngle() > degrees) {
                lOpMode.telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZYX,AngleUnit.DEGREES));
                lOpMode.telemetry.update();
            }
        } else    // left turn.
            while (lOpMode.opModeIsActive() && getAngle() < degrees) {
                lOpMode.telemetry.addData("Angle", imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZYX,AngleUnit.DEGREES));
                lOpMode.telemetry.update();
            }
        // turn the motors off.
        leftF.setPower(0);
        leftB.setPower(0);
        rightF.setPower(0);
        rightB.setPower(0);
        // wait for rotation to stop.
        lOpMode.sleep(1000);
        // reset angle for new heading
        resetAngle();
    }
}
