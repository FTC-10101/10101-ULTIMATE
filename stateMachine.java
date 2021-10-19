import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class stateMachine extends LinearOpMode {

    public enum State{
        INITIALIZE,
        DEPLOY_WOBBLE_GOAL_ONE,
        SHOOT_PRELOADED,
        GRAB_WOBBLE_GOAL_TWO,
        INTAKE_ONE_RING,
        SHOOT_ONE_RING,
        DEPLOY_WOBBLE_GOAL_TWO,
        PARK,
        STOP

    }
    public void runOpMode() throws InterruptedException{

    }

}
