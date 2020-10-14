import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Hardware;

@TeleOp
//@Disabled
public class Tele extends OpMode {

    // Inherits hardware class
    private ULTIMATEHardware ULTIMATE = new ULTIMATEHardware();

    // This is the same sleep method that is used in autonomous programs. It will eventually be used
    // for delays in the main loop to prevent the program from thinking a button was pressed
    // multiple times
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void init() {
        // Initializes hardware map for the phones
        ULTIMATE.init(hardwareMap);
    }
    public void loop() {

        // Primary driver's controls

        // Driving
        double drive;   // Power for forward and back motion
        double strafe;  // Power for left and right motion
        double rotate;  // Power for rotating the robot
        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;
        double frontLeftPower = (drive) + strafe + (rotate);
        double backLeftPower = (drive) - strafe - (rotate);
        double frontRightPower = (drive) - strafe + (rotate);
        double backRightPower = (drive) + strafe - (rotate);
        ULTIMATE.leftF.setPower(frontLeftPower);
        ULTIMATE.leftB.setPower(backLeftPower);
        ULTIMATE.rightF.setPower(frontRightPower);
        ULTIMATE.rightB.setPower(backRightPower);
        telemetry.addData("Left Front Power", (float) frontLeftPower);
        telemetry.addData("Left Rear Power", (float) backLeftPower);
        telemetry.addData("Right Front Power", (float) frontRightPower);
        telemetry.addData("Right Rear Power", (float) backRightPower);

        // Below is an option for the primary driver to use the dpad to move for slower, more
        // precise movements
        if(gamepad1.dpad_up) {
            ULTIMATE.leftF.setPower(.8);
            ULTIMATE.leftB.setPower(.8);
            ULTIMATE.rightB.setPower(.8);
        }
        if(gamepad1.dpad_down) {
            ULTIMATE.leftF.setPower(-.8);
            ULTIMATE.leftB.setPower(-.8);
            ULTIMATE.rightF.setPower(-.8);
            ULTIMATE.rightB.setPower(-.8);
        }
        if(gamepad1.dpad_right) {
            ULTIMATE.leftF.setPower(-.8);
            ULTIMATE.leftB.setPower(.8);
            ULTIMATE.rightF.setPower(.8);
            ULTIMATE.rightB.setPower(-.8);
        }
        if(gamepad1.dpad_left) {
            ULTIMATE.leftF.setPower(.8);
            ULTIMATE.leftB.setPower(-.8);
            ULTIMATE.rightF.setPower(-.8);
            ULTIMATE.rightB.setPower(.8);
        }

        // Accessory driver's controls

        // The power values below may not be correct because I don't know how the motors are mounted
        // Else statements may be unnecessary, but I have had problems in the past where motors
        // would continue to turn after pressing their respective buttons on the gamepad unless
        // I added an else statement putting them at 0. This can be tested once the shooter is ready
        if(gamepad2.a){
            ULTIMATE.shoot1.setPower(1);
            ULTIMATE.shoot1.setPower(1);
        }
        else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot1.setPower(0);
        }
        if(gamepad2.b){
            ULTIMATE.shoot1.setPower(-1);
            ULTIMATE.shoot1.setPower(-1);
        }
        else {
            ULTIMATE.shoot1.setPower(0);
            ULTIMATE.shoot1.setPower(0);
        }
    }
    // Makes sure the program stops completely
    public void stop () {
    }
}