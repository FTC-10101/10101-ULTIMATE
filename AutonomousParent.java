/* Houses all our commonly used functions that are used exclusively in autonomous. See engineering
notebook for details */
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCameraFactory;

// 800 encoder ticks = 90 degrees
public class AutonomousParent extends ULTIMATEHardware {

    private final LinearOpMode lOpMode; // reference to LinearOMode to use classes like telemetry

    public AutonomousParent(LinearOpMode lom, HardwareMap hwMap) {
        // chained constructor
        super(hwMap);
        super.hwMap = hwMap;

        // Create camera objects since only used in autonomous
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier
                ("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        Webcam1 = OpenCvCameraFactory.getInstance().createWebcam
                (hwMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // initializes reference to linear op mode to the class AutonomousParent is instantiated to
        this.lOpMode = lom;

    }

    /* A set of mostly simple void functions that can be useful for all
    autonomous opmodes. It mostly consists of encoder functions
     */
    public void drive(double power) {
        leftF.setPower(power);
        leftB.setPower(power);
        rightF.setPower(power);
        rightB.setPower(power);
    }
    public void drive(double lBPower, double lFPower, double rBPower, double rFPower) {
        leftF.setPower(lFPower);
        leftB.setPower(lBPower);
        rightF.setPower(rBPower);
        rightB.setPower(rFPower);
    }
    public void strafe(double power) {
        leftF.setPower(power);
        leftB.setPower(-power);
        rightF.setPower(-power);
        rightB.setPower(power);
    }
    public void halt() {
        leftF.setPower(0);
        leftB.setPower(0);
        rightF.setPower(0);
        rightB.setPower(0);
    }
    public void driveEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(distance);
        rightF.setTargetPosition(distance);
        leftF.setTargetPosition(distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(power);
        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {
            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.update();
        }
        halt();
        lOpMode.telemetry.clearAll();
    }
    public void driveEncoders(double startPower, double endPower, int distance) {

        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftF.setTargetPosition(distance);
        leftB.setTargetPosition(distance);
        rightF.setTargetPosition(distance);
        rightB.setTargetPosition(distance);

        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(startPower);

        double tickMultiplier_LF, tickMultiplier_LB, tickMultiplier_RB, tickMultiplier_RF;
        double power_LF, power_LB, power_RF, power_RB;

        while (leftB.isBusy() && leftF.isBusy() && rightF.isBusy() && rightB.isBusy()) {

            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.update();

            tickMultiplier_LF = 1 - Math.pow((double) (leftF.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftF.getTargetPosition(), 3);
            tickMultiplier_LB = 1 - Math.pow((double) (leftB.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftB.getTargetPosition(), 3);
            tickMultiplier_RB = 1 - Math.pow((double) (rightB.getTargetPosition() -
                    leftF.getCurrentPosition()) / rightB.getTargetPosition(), 3);
            tickMultiplier_RF = 1 - Math.pow((double) (rightF.getTargetPosition() -
                    leftF.getCurrentPosition()) / rightF.getTargetPosition(), 3);

            power_LF = tickMultiplier_LF * (endPower - startPower) + startPower;
            power_LB = tickMultiplier_LB * (endPower - startPower) + startPower;
            power_RB = tickMultiplier_RB * (endPower - startPower) + startPower;
            power_RF = tickMultiplier_RF * (endPower - startPower) + startPower;

            drive(power_LB, power_LF, power_RB, power_RF);

        }

        halt();
    }
    public void driveEncodersWithArm(double startPower, double endPower, int distance, int armTicks) {
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftF.setTargetPosition(distance);
        leftB.setTargetPosition(distance);
        rightF.setTargetPosition(distance);
        rightB.setTargetPosition(distance);

        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double tickMultiplier_LF, tickMultiplier_LB, tickMultiplier_RB, tickMultiplier_RF;
        double power_LF, power_LB, power_RF, power_RB;

        if(armTicks > 0) {
            setPowerExtensionArm(.5);
        }
        else{
            setPowerExtensionArm(-.5);
        }

        drive(startPower);
        while (leftB.isBusy() && leftF.isBusy() && rightF.isBusy() && rightB.isBusy()) {

            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.update();

            if(armTicks > 0) {
                if(intake2.getCurrentPosition() >= armTicks){
                    setPowerExtensionArm(0);
                }
            }
            else {
                if(intake2.getCurrentPosition() <= armTicks){
                    setPowerExtensionArm(0);
                }
            }

            tickMultiplier_LF = 1 - Math.pow((double) (leftF.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftF.getTargetPosition(), 3);
            tickMultiplier_LB = 1 - Math.pow((double) (leftB.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftB.getTargetPosition(), 3);
            tickMultiplier_RB = 1 - Math.pow((double) (rightB.getTargetPosition() -
                    leftF.getCurrentPosition()) / rightB.getTargetPosition(), 3);
            tickMultiplier_RF = 1 - Math.pow((double) (rightF.getTargetPosition() -
                    leftF.getCurrentPosition()) / rightF.getTargetPosition(), 3);

            power_LF = tickMultiplier_LF * (endPower - startPower) + startPower;
            power_LB = tickMultiplier_LB * (endPower - startPower) + startPower;
            power_RB = tickMultiplier_RB * (endPower - startPower) + startPower;
            power_RF = tickMultiplier_RF * (endPower - startPower) + startPower;

            drive(power_LB, power_LF, power_RB, power_RF);

        }
        halt();
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPowerExtensionArm(0);
    }
    public void strafeEncoders(double startPower, double endPower, int distance) {
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftF.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(-distance);
        rightB.setTargetPosition(distance);

        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double tickMultiplier_LF, tickMultiplier_LB, tickMultiplier_RB, tickMultiplier_RF;
        double power_LF, power_LB, power_RF, power_RB;

        drive(startPower);
        while (leftF.isBusy() && leftB.isBusy() && rightF.isBusy() && rightB.isBusy()) {

            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.update();

            tickMultiplier_LF = 1 - Math.pow((double) (leftF.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftF.getTargetPosition(), 3);
            tickMultiplier_LB = 1 - Math.pow((double) (leftB.getTargetPosition() -
                    leftB.getCurrentPosition()) / leftB.getTargetPosition(), 3);
            tickMultiplier_RB = 1 - Math.pow((double) (rightB.getTargetPosition() -
                    rightB.getCurrentPosition()) / rightB.getTargetPosition(), 3);
            tickMultiplier_RF = 1 - Math.pow((double) (rightF.getTargetPosition() -
                    rightF.getCurrentPosition()) / rightF.getTargetPosition(), 3);

            power_LF = tickMultiplier_LF * (endPower - startPower) + startPower;
            power_LB = tickMultiplier_LB * (endPower - startPower) + startPower;
            power_RB = tickMultiplier_RB * (endPower - startPower) + startPower;
            power_RF = tickMultiplier_RF * (endPower - startPower) + startPower;

            drive(power_LB, power_LF, power_RB, power_RF);
        }
        halt();
    }
    public void strafeEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(-distance);
        leftF.setTargetPosition(distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        strafe(power);
        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {
            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
         }
        halt();
        lOpMode.telemetry.clearAll();
    }
    public void strafeEncodersWithArm(double startPower, double endPower, int distance, int armTicks) {
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftF.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(-distance);
        rightB.setTargetPosition(distance);

        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        double tickMultiplier_LF, tickMultiplier_LB, tickMultiplier_RB, tickMultiplier_RF;
        double power_LF, power_LB, power_RF, power_RB;

        if(armTicks > 0) {
            setPowerExtensionArm(.5);
        }
        else{
            setPowerExtensionArm(-.5);
        }

        drive(startPower);
        while (leftF.isBusy() && leftB.isBusy() && rightF.isBusy() && rightB.isBusy()) {

            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.update();

            if(armTicks > 0) {
                if(intake2.getCurrentPosition() >= armTicks){
                    setPowerExtensionArm(0);
                }
            }
            else {
                if(intake2.getCurrentPosition() <= armTicks){
                    setPowerExtensionArm(0);
                }
            }

            tickMultiplier_LF = 1 - Math.pow((double) (leftF.getTargetPosition() -
                    leftF.getCurrentPosition()) / leftF.getTargetPosition(), 3);
            tickMultiplier_LB = 1 - Math.pow((double) (leftB.getTargetPosition() -
                    leftB.getCurrentPosition()) / leftB.getTargetPosition(), 3);
            tickMultiplier_RB = 1 - Math.pow((double) (rightB.getTargetPosition() -
                    rightB.getCurrentPosition()) / rightB.getTargetPosition(), 3);
            tickMultiplier_RF = 1 - Math.pow((double) (rightF.getTargetPosition() -
                    rightF.getCurrentPosition()) / rightF.getTargetPosition(), 3);

            power_LF = tickMultiplier_LF * (endPower - startPower) + startPower;
            power_LB = tickMultiplier_LB * (endPower - startPower) + startPower;
            power_RB = tickMultiplier_RB * (endPower - startPower) + startPower;
            power_RF = tickMultiplier_RF * (endPower - startPower) + startPower;

            drive(power_LB, power_LF, power_RB, power_RF);
        }
        halt();
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPowerExtensionArm(0);
    }
    public void turnEncoders(double power, int distance) {
        rightB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightB.setTargetPosition(distance);
        leftB.setTargetPosition(-distance);
        rightF.setTargetPosition(distance);
        leftF.setTargetPosition(-distance);

        rightB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        strafe(power);

        while (rightB.isBusy() && leftB.isBusy() && rightF.isBusy() && leftF.isBusy()) {

            lOpMode.telemetry.addData("leftF: ", leftF.getCurrentPosition());
            lOpMode.telemetry.addData("leftB: ", leftB.getCurrentPosition());
            lOpMode.telemetry.addData("rightF: ", rightF.getCurrentPosition());
            lOpMode.telemetry.addData("rightB: ", rightB.getCurrentPosition());
            lOpMode.telemetry.addLine("busy");
            lOpMode.telemetry.update();
        }
        halt();
        lOpMode.telemetry.clearAll();
    }
    public void armEncoders(int distance) {
        intake2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if(distance > 0) {
            setPowerExtensionArm(.5);
            while (intake2.getCurrentPosition() < distance) {
                lOpMode.telemetry.addData("target: ", distance);
                lOpMode.telemetry.addData("ticks: ", intake2.getCurrentPosition());
                lOpMode.telemetry.update();
            }
        }
        else{
            setPowerExtensionArm(-.5);
            while (intake2.getCurrentPosition() > distance) {
                lOpMode.telemetry.addData("target: ", distance);
                lOpMode.telemetry.addData("ticks: ", intake2.getCurrentPosition());
                lOpMode.telemetry.update();
            }
        }
        setPowerExtensionArm(0);
        lOpMode.telemetry.clearAll();
    }
    public void setPowerIntake(double power){
        intake1.setPower(power);
        intake2.setPower(-power);
    }
    public void setPowerExtensionArm(double power){
        extensionArm1.setPower(power);
        extensionArm2.setPower(power);
    }
    public void setVelocityFlywheel(double velocity){
        shoot1.setVelocity(velocity);
        shoot2.setVelocity(velocity);
    }
    public void shoot(int rings){

        // move the trigger to shoot the preloaded rings three times
        for (int i = 0; i < rings; i++) {
            trigger.setPosition(TRIGGER_shootPosition);
            lOpMode.sleep(500);
            trigger.setPosition(TRIGGER_restPosition);
            lOpMode.sleep(400);
        }

        // turn motors off and put catchplate back to normal position
        setVelocityFlywheel(0);
        catchPlate.setPosition(CATCHPLATE_intakePosition);
        lOpMode.sleep(300);

    }
    }