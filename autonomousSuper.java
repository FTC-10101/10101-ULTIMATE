import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;



public class autonomousSuper extends LinearOpMode {

ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();

    public autonomousSuper(){}

    public void runOpMode(){}
    public void drive (double power){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
    }
    public void driveTime (double power, int time){
        drive(power);
        sleep(time);
        driveHalt();
    }

    public void strafe (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
        driveHalt();

    }
    public void driveHalt(){
        ULTIMATE.leftF.setPower(0);
        ULTIMATE.leftB.setPower(0);
        ULTIMATE.rightF.setPower(0);
        ULTIMATE.rightB.setPower(0);
    }
    public void driveEncoders(double power, int distance){
        ULTIMATE.leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ULTIMATE.leftB.setTargetPosition(distance);
        ULTIMATE.rightB.setTargetPosition(distance);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(power);

        while( ULTIMATE.leftB.isBusy() && ULTIMATE.rightB.isBusy()){
            telemetry.addLine("busy");
            telemetry.update();
        }

        driveHalt();

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveExtensionArm (double power, int time){
        ULTIMATE.extensionArm.setPower(power);
        sleep(time);
        ULTIMATE.extensionArm.setPower(0);
    }


}
