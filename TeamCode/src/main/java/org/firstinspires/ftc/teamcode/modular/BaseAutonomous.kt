package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import kotlin.math.abs

abstract class BaseAutonomous : BaseOpMode() {
    protected abstract val plan: AutoStageExecutor

    protected lateinit var odometry: GoBildaPinpointDriver

    protected val velocity = 800.0
    protected var pose = Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0)
    protected val directionVector = Vector2(0.0, 0.0)
    protected var turnPower = 0.0
    protected var isOpModeActive = true
    protected val servoLaunch = 1.0
    protected val servoRetract = 0.7
    protected var ballsLaunched = 0
    protected val rackBallDelay = 2000L
    protected val launchBallDelay = 500L
    protected val motorLaunchVelocity = 2175.0

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(6.0, -9.5, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        driveTrain.forEach { m -> m.mode = RunMode.RUN_USING_ENCODER }
        servoLauncher.position = servoRetract
    }

    override fun loop() {
        telemetry.addLine("Direction: $directionVector")
        telemetry.addLine("Stage Number: ${plan.getStageNumber()}")
        telemetry.addLine("Angle: ${pose.angle}")
        telemetry.addLine("Odometry (tile): \n" +
                "x -> ${pose.x}\n" +
                "y -> ${pose.y}"
        )
        telemetry.addLine("Servo Pos: ${servoLauncher.position}")
        telemetry.addLine("Balls Launched: $ballsLaunched")
        telemetry.update()
        odometry.update()
        pose = odometry.position

        if (isOpModeActive && !plan.update()) {
            directionVector.x = 0.0
            directionVector.y = 0.0
            turnPower = 0.0
            isOpModeActive = false
        }

        rotateDoubleVector(directionVector, pose.angle)
        updateDriveTrain(directionVector, turnPower)
    }

    private fun updateDriveTrain(v: Vector2<Double>, turnPower: Double) {

        val motorPowers = arrayOf(
            -v.y - v.x + turnPower,
            -v.y + v.x - turnPower,
            -v.y + v.x + turnPower,
            -v.y - v.x - turnPower,
        )

        val max = motorPowers.maxOf { i -> abs(i) }

        motorPowers.forEachIndexed { i, _ -> motorPowers[i] /= max }
        driveTrain.forEachIndexed { i, m -> m.velocity = motorPowers[i] * -velocity }
    }

    protected val Pose2D.x: Double
        get() {
            return Units.convert(getX(DistanceUnit.CM), Units.CM, Units.TILE)
        }

    protected val Pose2D.y: Double
        get() {
            return Units.convert(getY(DistanceUnit.CM), Units.CM, Units.TILE)
        }

    protected val Pose2D.angle: Double
        get() {
            return getHeading(AngleUnit.RADIANS)
        }
}
