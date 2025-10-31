package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
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
@TeleOp(name = "Teliop kotlin", group = "Iterative OpMode")
//@Disabled
class TeliOpMode_Iterative : BaseOpMode() {
    // Declare OpMode members.

    // Declare OpMode members.
    // TODO stop using getter and setter methods
    // use

    private var runtime = ElapsedTime()

    private var axialMotion = 0.0 // Note: pushing stick forward gives negative value
    private var lateralMotion = 0.0
    private var yawMotion = 0.0
    private var launchSpeed = 0.0



    /*
     * Code to run ONCE when the driver hits INIT
     */


     override fun initialize() {
        telemetry.addData("Status", "Initialized")

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        // TODO rename to base opmode.


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
        axialMotion = -gamepad1.left_stick_y.toDouble() // Note: pushing stick forward gives negative value
        lateralMotion = gamepad1.left_stick_x.toDouble()
        yawMotion = gamepad1.right_stick_x.toDouble()


        if (gamepad1.aWasPressed()) {
            launchSpeed += 0.25
        } else if (gamepad1.bWasPressed()) {
            launchSpeed -= 0.25
        }
/*
        if (launchSpeed > 1) {
            launchSpeed = 1.0
        }
        if (launchSpeed < 0) {
            launchSpeed = 0.0
        }

 */


        /*
        if(launchSpeed == 0){
            leftLauncherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightLauncherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        */

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        val motorPowers = arrayOf(
            -gamepad1.left_stick_y + gamepad1.left_stick_x + yawMotion,
            -gamepad1.left_stick_y - gamepad1.left_stick_x - yawMotion,
            -gamepad1.left_stick_y - gamepad1.left_stick_x + yawMotion,
            -gamepad1.left_stick_y + gamepad1.left_stick_x - yawMotion,
        )

        val max = motorPowers.maxOf { i -> abs(i) }

        motorPowers.forEachIndexed {i, _ -> motorPowers[i] /= max}
        driveTrain.forEachIndexed {i, m -> m.power = motorPowers[i] * 0.33333 }

        leftLauncherMotor.power = launchSpeed
        rightLauncherMotor.power = launchSpeed


        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.

        



        /*
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontMotor , frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
        */
        telemetry.addData("left encoder value: ", leftLauncherMotor.currentPosition)
        telemetry.addData("right encoder value: ", rightLauncherMotor.currentPosition)
        telemetry.addData("launchSpeed: ", launchSpeed.toString())
        telemetry.update()
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    override fun stop() {
    }
}
