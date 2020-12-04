import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {

    ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();
    AutonomousParent auto;

    public void runOpMode(){

        ULTIMATE.init(hardwareMap,true);

        auto  = new AutonomousParent(this);



        waitForStart();

        auto.driveEncoders(.3, 100);
        sleep(500);
        stop();

    }

}
