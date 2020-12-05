import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {

    AutonomousParent auto  = new AutonomousParent(this);
    ULTIMATEHardware ULTIMATE_AUTO = auto;



    public void runOpMode(){


        auto.init();
        ULTIMATE_AUTO.init(hardwareMap,true,true);






        waitForStart();

        auto.driveEncoders(.3,3000);
        telemetry.addLine("should have ran motor");
        telemetry.update();
        stop();

    }

}
