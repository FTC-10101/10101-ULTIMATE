import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class Park extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();

    public void runOpMode() throws InterruptedException{

        ULTIMATE.init(hardwareMap);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        moveForward(1,1000);
    }
    private void moveForward (double power, int time){
        ULTIMATE.leftF.setPower(power);
        ULTIMATE.leftB.setPower(power);
        ULTIMATE.rightF.setPower(power);
        ULTIMATE.rightB.setPower(power);
        sleep(time);
    }
}
