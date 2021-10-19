/*Class for all our hardware devices. For more detail, see the programming section of our engineering notebook */
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.ArrayList;

public class ULTIMATEHardware {

    // All our DC motors
    public DcMotor leftF, leftB, rightF, rightB; // drive motors

    public DcMotorEx shoot1, shoot2, intake1, intake2; // flywheel and intake motors. Intake 2 has
    // external encoder for our two CR servos

    // All our servos
    public Servo catchPlate, trigger, deflector, armSwing, latch, lapBar, sideBar;

    public CRServo extensionArm1, extensionArm2,
            tapeMeasure; // CR servos for our linear slides arm for wobble
    // goal

    // External Webcam
    OpenCvCamera Webcam1;

    public RevColorSensorV3 ring1, ring2, ring3;
    public ColorRangeSensor exit;
    public ModernRoboticsI2cRangeSensor leftDistance, rightDistance;
    public RevColorSensorV3[] ringSensors;

    public final int ring1Threshold = 300;
    public final int ring2Threshold = 200;
    public final int ring3Threshold = 150;
    public int[] thresholds;

    // Positions
    // catchplate
    public final double CATCHPLATE_shootPosition = .455;
    public final double CATCHPLATE_dumpPosition = 0.0;
    public final double CATCHPLATE_intakePosition = 1.0;

    // armSwing
    public final double ARMSWING_downPosition = 1.0;
    public final double ARMSWING_upPosition = 0;

    // latch
    public final double LATCH_closedPosition = 1.0;
    public final double LATCH_openPosition = 0.4;

    // trigger
    public final double TRIGGER_shootPosition = .625;
    public final double TRIGGER_restPosition = 0.0;

    // deflector
    public final double DEFLECTOR_powerShotPosition = 1;
    public final double DEFLECTOR_normalPosition = 0;

    // lapBar
    public final double LAPBAR_grabPosition = 1.0;
    public final double LAPBAR_releasePosition = 0.0;

    // sideArm
    public final double SIDEBAR_inPosition = 1.0;
    public final double SIDEBAR_outPosition = .46;

    // other robot constants
    public int shootTPS = 1400;
    public final int shootSubtractorVal = 150;

    public HardwareMap hwMap;

    public ULTIMATEHardware (HardwareMap hwMap){

        // Initializes our hardware map including everything we have connected to our hubs
        this.hwMap = hwMap;
        extensionArm1 = hwMap.crservo.get("extensionArm1");
        extensionArm2 = hwMap.crservo.get("extensionArm2");
        tapeMeasure = hwMap.crservo.get("tapeMeasure");
        trigger = hwMap.servo.get("trigger");
        catchPlate = hwMap.servo.get("catchPlate");
        deflector = hwMap.servo.get("deflector");
        armSwing = hwMap.servo.get("armSwing");
        latch = hwMap.servo.get("latch");
        lapBar = hwMap.servo.get("lapBar");
        sideBar = hwMap.servo.get("sideBar");
        leftB = hwMap.dcMotor.get("leftB");
        rightB = hwMap.dcMotor.get("rightB");
        rightF = hwMap.dcMotor.get("rightF");
        leftF = hwMap.dcMotor.get("leftF");
        DcMotor[] drivetrain = {leftF, leftB, rightF, rightB};

        shoot1 = hwMap.get(DcMotorEx.class, "shoot1");
        shoot2 = hwMap.get(DcMotorEx.class, "shoot2");
        intake1 = hwMap.get(DcMotorEx.class, "intake1");
        intake2 = hwMap.get(DcMotorEx.class, "intake2");
        ring1 = hwMap.get(RevColorSensorV3.class, "ring1");
        ring2 = hwMap.get(RevColorSensorV3.class, "ring2");
        ring3 = hwMap.get(RevColorSensorV3.class, "ring3");
        exit = hwMap.get(ColorRangeSensor.class, "exit");
        ringSensors = new RevColorSensorV3[]{ring1, ring2, ring3};
        thresholds = new int[]{ring1Threshold, ring2Threshold, ring3Threshold};
        rightDistance = hwMap.get(ModernRoboticsI2cRangeSensor.class, "rightDistance");
        leftDistance = hwMap.get(ModernRoboticsI2cRangeSensor.class, "leftDistance");

        /* Accounts for motors that are mounted in the opposite direction, so positive power is
        always forward and negative power backwards */
        shoot1.setDirection(DcMotor.Direction.REVERSE);
        shoot2.setDirection(DcMotor.Direction.REVERSE);
        intake1.setDirection(DcMotor.Direction.REVERSE);
        intake2.setDirection(DcMotor.Direction.REVERSE);
        leftF.setDirection(DcMotor.Direction.REVERSE);
        leftB.setDirection(DcMotor.Direction.REVERSE);
        extensionArm1.setDirection(DcMotor.Direction.REVERSE);
        extensionArm2.setDirection(DcMotor.Direction.REVERSE);

        // motors will resist further movement when power is set to 0 for more precise movements
        for(DcMotor d : drivetrain){
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Makes sure motors aren't using encoders when they don't need to
        leftF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Always use encoders on flywheel to access default PID control
        shoot1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public boolean hasRing(int rings){
        if(rings == 0) {
            return ring1.blue() < ring1Threshold;
        }
        else {
            return ringSensors[rings - 1].blue() < thresholds[rings - 1];
        }
    }

}