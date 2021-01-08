import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {


    AutonomousParent auto  = new AutonomousParent(this);
    ULTIMATEHardware ULTIMATE = auto;




    public void runOpMode(){





        ULTIMATE.init(hardwareMap,true,false);

        ULTIMATE.leftB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.leftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.rightB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.rightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        waitForStart();

        auto.driveEncoders(.2,.8,3000);
        sleep(200);



        stop();

    }



}
