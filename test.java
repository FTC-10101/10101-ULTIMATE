import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    autonomousSuper auto = new autonomousSuper();

    public void runOpMode() throws InterruptedException{

        ULTIMATE.init(hardwareMap);

        ULTIMATE.leftB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ULTIMATE.rightB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ULTIMATE.leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ULTIMATE.rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();


        auto.driveEncoders(.3, 100);
        sleep(500);
        stop();

    }
}
