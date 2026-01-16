package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import java.lang.Thread.sleep
import kotlin.math.abs

abstract class BaseAutonomous : BaseOpMode() {
    protected abstract val plan: AutoStageExecutor
    protected var pose = Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0)
    protected val directionVector = Vector2(0.0, 0.0)
    protected var turnPower = Vector2(0.0, 0.0)
    protected var isOpModeActive = true
    protected lateinit var autoHelper: BaseAutoOpHelper
    private lateinit var odometry: GoBildaPinpointDriver
    private val velocity = 800.0
    private var hasWaited = false
    private var waitPeriod: UInt = 0U


    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(6.0, -9.5, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        autoHelper = BaseAutoOpHelper(baseHelper, directionVector, turnPower)

        driveTrain.forEach { m -> m.mode = RunMode.RUN_USING_ENCODER }
        servoLauncher.position = 0.7 // initialization position
    }

    override fun init_loop() {
        when {
           gamepad1.leftBumperWasPressed() -> waitPeriod--
           gamepad1.rightBumperWasPressed() -> waitPeriod++
        }

        telemetry.addLine("Current Wait Period: $waitPeriod seconds")
        telemetry.addLine("(Left and Right Bumper to Adjust)")
        telemetry.update()
    }

    override fun loop() {
        if (!hasWaited) {
            sleep(waitPeriod.toLong() * 1000)
            hasWaited = true
        }

        telemetry.addLine("Direction: $directionVector")
        telemetry.addLine("Stage Number: ${plan.getStageNumber()}")
        telemetry.addLine("Angle: ${pose.angle}")
        telemetry.addLine("Odometry (tile): \n" +
                "x -> ${pose.x}\n" +
                "y -> ${pose.y}"
        )
        telemetry.addLine("Servo Pos: ${servoLauncher.position}")
        telemetry.update()

        odometry.update()
        pose = odometry.position

        if (isOpModeActive && !plan.update()) {
            directionVector.x = 0.0
            directionVector.y = 0.0
            turnPower.x = 0.0
            turnPower.y = 0.0
            isOpModeActive = false
        }

        rotateDoubleVector(directionVector, pose.angle)
        updateDriveTrain(directionVector, turnPower)
    }

    private fun updateDriveTrain(v: Vector2<Double>, turnPower: Vector2<Double>) {

        val motorPowers = arrayOf(
            -v.y - v.x + (turnPower.y - turnPower.x),
            -v.y + v.x - (turnPower.y - turnPower.x),
            -v.y + v.x + (turnPower.y - turnPower.x),
            -v.y - v.x - (turnPower.y - turnPower.x),
        )

        val max = motorPowers.maxOf { i -> abs(i) }

        motorPowers.forEachIndexed { i, _ -> motorPowers[i] /= max }
        driveTrain.forEachIndexed { i, m -> m.velocity = motorPowers[i] * -velocity }
    }
}
