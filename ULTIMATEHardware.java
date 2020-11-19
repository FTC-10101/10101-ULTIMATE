import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ULTIMATEHardware {

    // Hardware Map. Android Studio says this line is unnecessary, but it is in every hardware class
    // we have made before. Eventually I will test it to see what's up, but for now, I don't
    // understand this line. Need to ask Jacob or Zade
    HardwareMap ulthw = null;

    // DC motors
    public DcMotor leftF, leftB, rightF, rightB, // drive motors
            shoot1, shoot2, intake, // intake and shooter motors
            extensionArm; // arm for wobble goal

    // Servos
    public Servo catchPlate, trigger, deflector, armSwing, latch, lapBar;

    public ULTIMATEHardware() { } // default constructor. Another line that seems unnecessary to me
    // but we have have used it in years past

    public void init(HardwareMap ulthw) {
        leftB = ulthw.dcMotor.get("leftB");
        rightB = ulthw.dcMotor.get("rightB");
        rightF = ulthw.dcMotor.get("rightF");
        leftF = ulthw.dcMotor.get("leftF");
        shoot1 = ulthw.dcMotor.get("shoot1");
        shoot2 = ulthw.dcMotor.get("shoot2");
        intake = ulthw.dcMotor.get("intake");
        extensionArm = ulthw.dcMotor.get("extensionArm");
        trigger = ulthw.servo.get("trigger");
        catchPlate = ulthw.servo.get("catchPlate");
        deflector = ulthw.servo.get("deflector");
        armSwing = ulthw.servo.get("armSwing");
        latch = ulthw.servo.get("latch");
        lapBar = ulthw.servo.get("lapBar");

        // This is the best way to account for motors being mounted in the opposite direction.
        // Instead of having to use a negative sign for the motor power value all the time, I can
        // just write these lines are forget about it. It also makes our programs more readable
        shoot1.setDirection(DcMotor.Direction.REVERSE);
        shoot2.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        leftF.setDirection(DcMotor.Direction.REVERSE);
        rightF.setDirection(DcMotor.Direction.REVERSE);
        rightB.setDirection(DcMotor.Direction.REVERSE);
        leftB.setDirection(DcMotor.Direction.REVERSE);

    }
}
