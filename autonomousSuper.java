import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class autonomousSuper {
ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    private void drive (double power){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
    }
    protected void driveTime (double power, int time){
        drive(power);
        sleep(time);
        driveHalt();
    }

    protected void strafe (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
        driveHalt();

    }
    private void driveHalt(){
        ULTIMATE.leftF.setPower(0);
        ULTIMATE.leftB.setPower(0);
        ULTIMATE.rightF.setPower(0);
        ULTIMATE.rightB.setPower(0);
    }
    protected void driveEncoders(double power, int distance){
        ULTIMATE.leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ULTIMATE.leftB.setTargetPosition(distance);
        ULTIMATE.rightB.setTargetPosition(distance);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(power);

        while( ULTIMATE.leftB.isBusy() && ULTIMATE.rightB.isBusy()){
            telemetry.addLine("busy");
        }

        driveHalt();

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    protected void moveExtensionArm (double power, int time){
        ULTIMATE.extensionArm.setPower(power);
        sleep(time);
        ULTIMATE.extensionArm.setPower(0);
    }
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
