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

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(6.0, -9.5, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        driveTrain.forEach { m -> m.mode = RunMode.RUN_USING_ENCODER }
        servoLauncher.position = servoRetract

        plan = AutoStageExecutor {
            val p = ArrayList<Stage>()
            /*Stage(
                {  pose.x > -1 && pose.y < 3.889 },
                {
                    directionVector.x = -1.0
                    directionVector.y = 3.889
                    turnPower = 0.0
                }
            ),

            Stage(
                { pose.angle > -1 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = -0.75
                }
            ),*/

            // shooting stage here

            p += Stage(
                { leftLauncherMotor.velocity > -1600 && rightLauncherMotor.velocity < 1600 },
                {
                    leftLauncherMotor.velocity = -1600.0
                    rightLauncherMotor.velocity = 1600.0
                }
            )

            for (i in 0..2) {
                p += Stage(
                    { servoLauncher.position < servoLaunch },
                    {
                        servoLauncher.position = servoLaunch
                        sleep(500)
                    }
                )

                p += Stage(
                    { servoLauncher.position in servoRetract.nextUp()..<servoLaunch },
                    {}
                )

                p += Stage(
                    { servoLauncher.position > servoRetract + 0.0001 },
                    {
                        servoLauncher.position = servoRetract
                        sleep(500)
                    }
                )
            }


            /*Stage(
                { pose.angle < -0.2 },
                {
                    directionVector.x = 0.0
                    directionVector.y = 0.0
                    turnPower = 0.75
                }
            ),

            Stage(
                { pose.x < -0.5 && pose.y > 1.723 },
                {
                    directionVector.x = 0.5
                    directionVector.y = -2.166
                    turnPower = 0.0
                }
            )*/

            return@AutoStageExecutor p.toTypedArray()
        }
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

private fun isInEpsilon(x: Double, y: Double, epsilon: Double = 0.000001): Boolean {
    return x in y-epsilon..y+epsilon
}
