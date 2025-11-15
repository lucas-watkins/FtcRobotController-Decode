package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.AutoMover
import org.firstinspires.ftc.teamcode.modular.AutoMover.Direction
import org.firstinspires.ftc.teamcode.modular.Units
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

@Autonomous(name = "AutonomousTest", group = "OpMode")
class AutonomousTest : BaseOpMode() {
    private lateinit var odometry: GoBildaPinpointDriver
    private lateinit var mover: AutoMover
    private lateinit var plan: AutoStageExecutor
    val power = 0.333
    var pose = Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0)
    var isOpModeActive = true

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(0.0, 20.0, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        mover = AutoMover(driveTrain)

        plan = AutoStageExecutor(arrayOf(
            Stage(
                {  pose.y < 1 },
                { if (mover.direction != Direction.FORWARD) mover.goForward(power) }
            ),

            Stage(
                { pose.x < 1 },
                { if (mover.direction != Direction.RIGHT) mover.goRight(power) }
            ),

            Stage(
                { pose.y > -0.9 },
                { if (mover.direction != Direction.BACKWARD) mover.goBackward(power) }
            ),

            Stage(
                { pose.x > -1 },
                { if (mover.direction != Direction.LEFT) mover.goLeft(power) }
            )
        ), odometry)
    }

    override fun loop() {
        telemetry.addLine("Direction: ${mover.direction.name}")
        telemetry.addLine("Stage Number: ${plan.getStageNumber()}")
        telemetry.addLine("Angle: ${pose.angle}")
        telemetry.addLine("Odometry (tile): \n" +
                "x -> ${pose.x}\n" +
                "y -> ${pose.y}"
        )
        telemetry.update()
        odometry.update()
        pose = odometry.position

        if (!plan.update()) {
            driveTrain.forEach { m -> m.power = 0.0 }
            isOpModeActive = false
        }

        // Decay angle adjustment power after 100 ticks
        driveTrain.forEachIndexed { i, m ->
            if (m.power > power * (1.0 + AutoMover.offsets[i])) m.power -= ((m.power - power) / 100.0)
        }

        if (pose.angle < -0.1 && isOpModeActive) {
            driveTrain[1].power += 0.0005
            driveTrain[3].power += 0.0008
        }

        if (pose.angle > 0.1 && isOpModeActive) {
            driveTrain[0].power += 0.0005
            driveTrain[2].power += 0.0005
        }
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
