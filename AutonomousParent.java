import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Hardware;

public class AutonomousParent extends ULTIMATEHardware {


private LinearOpMode lOpMode;
int sleepConstant = 1400;




public void init(){
    init(lOpMode.hardwareMap, true,false);
    leftB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
}

public AutonomousParent (LinearOpMode lom){
    this.lOpMode = lom;
}

    public void drive (double power){
        leftF.setPower(power);
        leftB.setPower(power);
        rightF.setPower(power);
        rightB.setPower(power);
    }
    public void driveTime (double power, int time){
        drive(power);
        lOpMode.sleep(time);
        halt();
    }

    public void strafe (double power, int time){
        leftF.setPower(power);
        leftB.setPower(-power);
        rightF.setPower(-power);
        rightB.setPower(power);
        lOpMode.sleep(time);
        halt();
    }

    public void halt(){
        leftF.setPower(0);
        leftB.setPower(0);
        rightF.setPower(0);
        rightB.setPower(0);
    }

    public void driveEncoders(double power, int distance){
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(power);

        while (rightB.isBusy() && leftB.isBusy()){
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
        }

        halt();

        rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveExtensionArm (double power, int time){
        extensionArm.setPower(power);
        lOpMode.sleep(time);
        extensionArm.setPower(0);
    }
    public void dropWobbleGoal(){

        // move arm out to drop wobble goal
        moveExtensionArm(1,1500);
        lOpMode.sleep(sleepConstant);

        // lower wobble goal holding arm
        armSwing.setPosition(1);
        lOpMode.sleep(sleepConstant);

        // unlatch wobble goal
        latch.setPosition(0);
        lOpMode.sleep(200);

        // retract extension arm halfway
        moveExtensionArm(-1,750);
        lOpMode.sleep(sleepConstant);

        // move holding arm back up
        armSwing.setPosition(0);
        lOpMode.sleep(sleepConstant);

        // retract extension arm fully
        moveExtensionArm(-1,750);
        lOpMode.sleep(sleepConstant);

    }

    public void initIMU(){
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.mode = BNO055IMU.SensorMode.IMU;
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = false;
        imu.initialize(imuParameters);
    }


}
