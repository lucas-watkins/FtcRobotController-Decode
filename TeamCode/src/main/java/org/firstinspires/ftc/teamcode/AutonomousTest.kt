package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.AutoFlightPlan
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.AutoMover
import org.firstinspires.ftc.teamcode.modular.AutoMover.Direction
import org.firstinspires.ftc.teamcode.modular.Units
import org.firstinspires.ftc.teamcode.modular.AutoFlightPlan.Stage

@Autonomous(name = "AutonomousTest", group = "OpMode")
class AutonomousTest : BaseOpMode() {
    private lateinit var odometry: GoBildaPinpointDriver
    private lateinit var mover: AutoMover
    private lateinit var plan: AutoFlightPlan

    val x: Double
        get() {
            odometry.update()
            return odometry.getPosX(DistanceUnit.CM)
        }

    val y: Double
        get() {
            odometry.update()
            return odometry.getPosY(DistanceUnit.CM)
        }

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(0.0, 20.0, DistanceUnit.CM)
        odometry.setEncoderResolution(37.25135125, DistanceUnit.MM)
        odometry.setEncoderDirections(EncoderDirection.FORWARD, EncoderDirection.REVERSED)
        odometry.resetPosAndIMU()

        mover = AutoMover(driveTrain)

        plan = AutoFlightPlan(arrayOf(
            Stage(
                { y < Units.convert(1, Units.TILE, Units.CM) },
                { if (mover.direction != Direction.FORWARD) mover.goForward(0.333) }
            ),

            Stage(
                { x < Units.convert(1, Units.TILE, Units.CM) },
                { if (mover.direction != Direction.RIGHT) mover.goRight(0.333) }
            ),

            Stage(
                { y > 0 },
                { if (mover.direction != Direction.BACKWARD) mover.goBackward(0.333) }
            ),

            Stage(
                { x > 0 },
                { if (mover.direction != Direction.LEFT) mover.goLeft(0.333) }
            )
        ))
    }

    override fun loop() {
        telemetry.addLine("Direction: ${mover.direction.name}")
        telemetry.addLine("Stage Number: ${plan.getStageNumber()}")
        telemetry.addLine("Odometry (tile): \n" +
                "x -> ${Units.convert(x, Units.CM, Units.TILE)}\n" +
                "y -> ${Units.convert(y, Units.CM, Units.TILE)}"
        )
        telemetry.update()

        if (!plan.update()) {
            driveTrain.forEach { m -> m.power = 0.0 }
        }
    }
}
