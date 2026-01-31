package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.Alliance
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous
import org.firstinspires.ftc.teamcode.modular.MutableReference

@Autonomous(name = "GoForward")
class GoForward : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.BLU)

    override fun getPlan(): AutoStageExecutor {
        return AutoStageExecutor(
            AutoStageExecutor.Stage(
                {pose.y < 1.5},
                {
                    directionVector.y = 1.0
                    directionVector.x = 0.0
                    turnPower.y = 0.0
                    turnPower.x = 0.0
                }
            )
        )
    }
}