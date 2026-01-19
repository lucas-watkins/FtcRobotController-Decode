package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.Alliance
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous
import org.firstinspires.ftc.teamcode.modular.MutableReference

@Autonomous(name = "BlueCenterAutonomous", group = "BlueTeam")
class BlueCenterAutonomous : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.BLU)
    override fun getPlan() = AutoStageExecutor(
        Stage(
            { pose.y < 3.375 },
            {
                directionVector.x = 0.0
                directionVector.y = 3.375
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        ),

        Stage(
            { pose.angle < 0.65 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.y = 0.75
                turnPower.x = 0.0
            }
        ),

        *autoHelper.launchBallStage,

        Stage(
            { pose.angle > 0.2 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.x = 0.75
                turnPower.y = 0.0
            }
        ),

        Stage(
            { pose.y > 2.25},
            {
                directionVector.x = 0.0
                directionVector.y = -2.25
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        )
    )
}
