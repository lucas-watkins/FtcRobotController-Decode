package org.firstinspires.ftc.teamcode

import Localization
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import kotlin.math.*


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
@TeleOp(name = "war teli-op", group = "Iterative OpMode")
class WarTeliOp : BaseOpMode() {
    var runtime : ElapsedTime = ElapsedTime()
    private var forwardMotion = 0.0 // Note: pushing stick forward gives negative value
    private var lateralMotion = 0.0
    private var yawMotion = 0.0
    private var launchSpeed = 0.0
    private val powerSettings = arrayOf(0.25, 0.5, 0.66)
    private var powerSettingIndex = 0
    private var drivePower = powerSettings[powerSettingIndex]

    // in this could be faster but their is no reason to make it faster
    // in the configuration of the robot as of nov 4 2700 is if anything too powerful
    private var maxLaunchSpeed = 2700.0 // ticks

    private var avgLaunchVelocity = 0.0 // ticks

    private var maxDriveMotorPower = 0.0

    private lateinit var limelight: Limelight3A
    private lateinit var imu: IMU
    private lateinit var localization: Localization


    /*
     * Code to run ONCE when the driver hits INIT
     */

    override fun initialize() {
        limelight = hardwareMap[Limelight3A::class.java, "limelight"]
        imu = hardwareMap[IMU::class.java, "imu"]
        localization = Localization(limelight, imu, Alliance.BLU) // TODO: handle alliance input

        telemetry.addData("Status", "Initialized")
        telemetry.update()
        runtime.reset()

    }

    override fun start() {
        runtime.reset()
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    fun setLaunchSpeed(){
        launchSpeed = localization.estimatedTicks

        launchSpeed = launchSpeed.coerceIn(0.0, maxLaunchSpeed)

        for(m in launcherMotors){
            m.velocity = launchSpeed
        }
    }

    fun getYawOverride(): Double {
        val yawOverride = 0.5
        if(gamepad2.left_bumper || gamepad2.dpad_left){
            return yawOverride
        }
        if(gamepad2.right_bumper || gamepad2.dpad_right){
             return -yawOverride
        }
        return 0.0 //if nothing is pressed
    }
    fun setLaunchSpeedFromDpad(){
        if (gamepad1.right_bumper) {
            powerSettingIndex++
            Thread.sleep(250L)
        }

        if (gamepad1.left_bumper) {
            powerSettingIndex--
            Thread.sleep(250L)
        }

        powerSettingIndex %= powerSettings.size // keep power setting at 3
        powerSettingIndex = abs(powerSettingIndex)

        drivePower = powerSettings[powerSettingIndex]
    }
    override fun loop() {
        forwardMotion = -gamepad1.left_stick_y.toDouble() // Note: pushing stick forward gives negative value
        lateralMotion = gamepad1.left_stick_x.toDouble()
        yawMotion = gamepad1.right_stick_x.toDouble()

        yawMotion += getYawOverride()

        val motorPowers = arrayOf(
            -gamepad1.left_stick_y + gamepad1.left_stick_x + yawMotion,
            -gamepad1.left_stick_y - gamepad1.left_stick_x - yawMotion,
            -gamepad1.left_stick_y - gamepad1.left_stick_x + yawMotion,
            -gamepad1.left_stick_y + gamepad1.left_stick_x - yawMotion,
        )

        // Normalize the values so no wheel power exceeds 100%
        for(p in motorPowers){
            maxDriveMotorPower = min(abs(p), maxDriveMotorPower)
        }
        if(maxDriveMotorPower < 1){
            // if the drive motor power is less then one the motors will be set faster then 1.0
            maxDriveMotorPower = 1.0
        }
        //motorPowers.forEachIndexed {i ,m-> motorPowers[i] /= abs(maxDriveMotorPower)}//
        for(i in motorPowers.indices){motorPowers[i] /= abs(maxDriveMotorPower)}

        driveTrain.forEachIndexed {i, m -> m.power = motorPowers[i] * drivePower}

        if(gamepad2.xWasPressed()){
            ballLaunch.launchBall()
        }
        if(gamepad1.yWasPressed()){
            ballLaunch.rightGateServoCycle()
        }
        if(gamepad1.aWasPressed()){
            ballLaunch.leftGateServoCycle()
        }

        setLaunchSpeed()
//        setLaunchSpeedFromDpad()

        val result = localization.limelight.latestResult
        telemetry.addData("ll result valid", result.isValid)
        telemetry.addData("ll staleness", result.staleness)
        telemetry.addData("ll botpose x", result.botpose.position.x)
        telemetry.addData("ll botpose y", result.botpose.position.y)
        telemetry.addData("ll tag count", result.fiducialResults.size)

//        telemetry.addData("left launch speed tick: ", leftLauncherMotor.velocity)
//        telemetry.addData("right launch speed tick: ", rightLauncherMotor.velocity)
//        telemetry.addData("avg speed: ", avgLaunchVelocity)
//        telemetry.addData("power setting: ", drivePower)
//        telemetry.addData("distance: ", localization.distanceFromGoal)
//        telemetry.addData("tick from localization: ", localization.estimatedTicks)

        telemetry.update()
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    override fun stop() {
        //stop behavior if needed
    }
}
