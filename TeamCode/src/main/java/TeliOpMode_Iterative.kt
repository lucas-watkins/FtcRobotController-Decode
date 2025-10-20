import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs
import kotlin.math.max

/*
* This file contains an example of an iterative (Non-Linear) "OpMode".
* An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
* The names of OpModes appear on the menu of the FTC Driver Station.
* When a selection is made from the menu, the corresponding OpMode
* class is instantiated on the Robot Controller and executed.
*
* This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
* It includes all the skeletal structure that all iterative OpModes contain.
*
* Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
* Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
*/
@TeleOp(name = "Basic: Iterative OpMode", group = "Iterative OpMode")
@Disabled
class TeliOpMode_Iterative : OpMode() {
    // Declare OpMode members.

    private var frontLeftDrive: DcMotor? = null
    private var backLeftDrive: DcMotor? = null
    private var frontRightDrive: DcMotor? = null
    private var backRightDrive: DcMotor? = null

    private var launchRight: DcMotor? = null

    private var launchLeft: DcMotor? = null

    private var axialMotion = 0.0 // Note: pushing stick forward gives negative value
    private var lateralMotion = 0.0
    private var yawMotion = 0.0

    /*
     * Code to run ONCE when the driver hits INIT
     */
    override fun init() {
        telemetry.addData("Status", "Initialized")

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        frontLeftDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "front_left_drive")
        backLeftDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "back_left_drive")
        frontRightDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "front_right_drive")
        backRightDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "back_right_drive")

        launchLeft = hardwareMap.get<DcMotor?>(DcMotor::class.java, "left_launch")
        launchRight = hardwareMap.get<DcMotor?>(DcMotor::class.java, "right_launch")

        frontLeftDrive!!.setDirection(DcMotor.Direction.REVERSE)
        backLeftDrive!!.setDirection(DcMotor.Direction.REVERSE)
        frontRightDrive!!.setDirection(DcMotor.Direction.REVERSE)
        backRightDrive!!.setDirection(DcMotor.Direction.REVERSE)

        launchLeft!!.setDirection(DcMotorSimple.Direction.REVERSE)
        launchRight!!.setDirection(DcMotorSimple.Direction.REVERSE)

        frontRightDrive!!.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        backLeftDrive!!.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        frontLeftDrive!!.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        backRightDrive!!.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        launchLeft!!.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        launchRight!!.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        launchLeft!!.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        launchRight!!.setMode(DcMotor.RunMode.RUN_USING_ENCODER)

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized")
        telemetry.update()


        runtime.reset()


    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    override fun init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    override fun start() {
        runtime.reset()
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    override fun loop() {


        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.

        // TODO get controls
        axialMotion =
            -gamepad1.left_stick_y.toDouble() // Note: pushing stick forward gives negative value
        lateralMotion = gamepad1.left_stick_x.toDouble()
        yawMotion = gamepad1.right_stick_x.toDouble()


        if (gamepad1.aWasPressed()) {
            launchSpeed += 0.25
        } else if (gamepad1.bWasPressed()) {
            launchSpeed -= 0.25
        }

        if (launchSpeed > 1) {
            launchSpeed = 1.0
        }
        if (launchSpeed < 0) {
            launchSpeed = 0.0
        }


        /*
        if(launchSpeed == 0){
            launchLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            launchRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        */

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        val frontLeftPower = axialMotion + lateralMotion + yawMotion
        val frontRightPower = axialMotion - lateralMotion - yawMotion
        val backLeftPower = axialMotion - lateralMotion + yawMotion
        val backRightPower = axialMotion + lateralMotion - yawMotion


        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = max(abs(frontLeftPower), abs(frontRightPower))
        max = max(max, abs(backLeftPower))
        max = max(max, abs(backRightPower))


        // power greater then 1 going to motors is bad; this stops it
        if (max > 1.0) {
            frontLeftPower /= max
            frontRightPower /= max
            backLeftPower /= max
            backRightPower /= max
        }

        frontLeftDrive!!.setPower(frontLeftPower)
        frontRightDrive!!.setPower(frontRightPower)
        backLeftDrive!!.setPower(backLeftPower)
        backRightDrive!!.setPower(backRightPower)
        launchRight!!.setPower(launchSpeed)
        launchLeft!!.setPower(launchSpeed)


        /*
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
        */
        telemetry.addData("left encoder value: ", launchLeft!!.getCurrentPosition())
        telemetry.addData("right encoder value: ", launchRight!!.getCurrentPosition())
        telemetry.addData("launchSpeed: ", launchSpeed.toString())
        telemetry.update()
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    override fun stop() {
    }
}
