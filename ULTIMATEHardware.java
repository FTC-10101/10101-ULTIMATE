import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ULTIMATEHardware {

    // Hardware Map
    HardwareMap ultimatehw = null;

    // DC motors
    public DcMotor leftF, leftB, rightF, rightB, // drive motors
            shoot1, shoot2, intake; // intake and shooter motors

    // Servos
    public Servo intakeServo, feedServo;

    public ULTIMATEHardware() { } // default constructor

    public void init(HardwareMap ulthw) {
        leftB = ulthw.dcMotor.get("leftB");
        rightB = ulthw.dcMotor.get("rightB");
        rightF = ulthw.dcMotor.get("rightF");
        leftF = ulthw.dcMotor.get("leftF");
        shoot1 = ulthw.dcMotor.get("shoot1");
        shoot2 = ulthw.dcMotor.get("shoot2");
        intake = ulthw.dcMotor.get("intake");
        feedServo = ulthw.servo.get("feedServo");
        intakeServo = ulthw.servo.get("intakeServo");
        shoot1.setDirection(DcMotor.Direction.REVERSE);
        shoot2.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
    }
}
