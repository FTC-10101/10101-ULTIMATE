import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class hitPowerShots extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();

    public void runOpMode() throws InterruptedException{



        ULTIMATE.init(hardwareMap);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        ULTIMATE.shoot1.setPower(1);
        ULTIMATE.shoot2.setPower(1);



        moveForward(.2, 300);
        sleep(200);
        strafeRight(.3,1500);
        sleep(200);
        moveForward(.5,1000);
        sleep(200);

        ULTIMATE.catchPlate.setPosition(1);
        sleep(1000);

        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(0);
        strafeRight(.2,300);
        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(0);
        strafeRight(.2,300);
        ULTIMATE.trigger.setPosition(.8);
        sleep(1000);
        ULTIMATE.trigger.setPosition(0);
        strafeRight(.2,300);











    }
    private void moveForward (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
    }

    // this is flipped because teleop is messed up. fix later
    private void strafeRight (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(-power);
        ULTIMATE.rightF.setPower(-power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
    }
}