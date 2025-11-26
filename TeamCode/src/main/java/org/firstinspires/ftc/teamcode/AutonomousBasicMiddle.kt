package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.Units
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.modular.Vector2
import org.firstinspires.ftc.teamcode.modular.rotateDoubleVector
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.nextUp


@Autonomous(name = "AutonomousBasicMiddle", group = "OpMode")
class AutonomousBasicMiddle : BaseOpMode() {
    private lateinit var odometry: GoBildaPinpointDriver
    private lateinit var plan: AutoStageExecutor
    private val velocity = 800.0
    private var pose = Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0)
    private val directionVector = Vector2(0.0, 0.0)
    private var turnPower = 0.0
    private var isOpModeActive = true
    private val servoLaunch = 1.0
    private val servoRetract = 0.7
    private var ballsLaunched = 0

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(6.0, -9.5, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        driveTrain.forEach { m -> m.mode = RunMode.RUN_USING_ENCODER }
        servoLauncher.position = servoRetract

        plan = AutoStageExecutor (
            Stage(
                { pose.y < 3.375 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 3.375
                    turnPower = 0.0
                }
            ),

            Stage(
                { pose.angle < 0.65 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = 0.75
                }
            ),

            Stage(
                { leftLauncherMotor.velocity > -2175 && rightLauncherMotor.velocity < 2175 },
                {
                    leftLauncherMotor.velocity = -2175.0
                    rightLauncherMotor.velocity = 2175.0

                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = 0.0
                }
            ),

            Stage(
                { ballsLaunched < 3 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = 0.0

                    if (servoLauncher.position < servoLaunch) {
                        servoLauncher.position = servoLaunch
                        ballsLaunched++
                        sleep(1000)
                    }

                   if (servoLauncher.position > servoRetract) {
                       servoLauncher.position = servoRetract
                       sleep(1000)
                   }
               }
            ),

            Stage(
                { leftLauncherMotor.velocity < -2174 && rightLauncherMotor.velocity > 2174 },
                {
                    leftLauncherMotor.velocity = 0.0
                    rightLauncherMotor.velocity = 0.0

                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = 0.0
                }
            ),

            Stage(
                { pose.angle > 0.2 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = -0.75
                }
            ),

            Stage(
                { pose.y > 2.25},
                {
                    directionVector.x = 0.0
                    directionVector.y = -2.25
                    turnPower = 0.0
                }
            )
        )
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
}

private val Pose2D.x: Double
    get() {
        return Units.convert(getX(DistanceUnit.CM), Units.CM, Units.TILE)
    }

private val Pose2D.y: Double
    get() {
        return Units.convert(getY(DistanceUnit.CM), Units.CM, Units.TILE)
    }

private val Pose2D.angle: Double
    get() {
        return getHeading(AngleUnit.RADIANS)
    }
