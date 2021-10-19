import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Timer;


// class for testing minor changes
@Autonomous
public class test extends LinearOpMode {

    AutonomousParent auto  = new AutonomousParent(this, hardwareMap);
    ElapsedTime timer = new ElapsedTime();
    ULTIMATEHardware ULTIMATE = auto;


    public void runOpMode(){

        ULTIMATE.leftB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.leftF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.rightB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ULTIMATE.rightF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();
        auto.turnEncoders(1,500);
        while(opModeIsActive() && !isStopRequested()) {
            telemetry.addData("load: ", ULTIMATE.intake1.getCurrent(CurrentUnit.AMPS));
            telemetry.update();
            auto.driveEncoders(1,20000);
        }


        telemetry.update();
        sleep(3000);


        stop();

    }



}
