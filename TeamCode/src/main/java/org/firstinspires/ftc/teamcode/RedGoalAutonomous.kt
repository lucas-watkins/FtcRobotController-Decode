package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.*
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage

@Autonomous(name = "RedGoalAutonomous", group = "RedTeam")
class RedGoalAutonomous : BaseAutonomous() {
    override val plan = AutoStageExecutor(
        Stage(
            { pose.y < 1.977 },
            {
                directionVector.x = 0.0
                directionVector.y = 1.977
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        ),

        Stage(
            { pose.angle < 2.9 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.x = 0.0
                turnPower.y = 0.75
            }
        ),

        *autoHelper.launchBallStage,

        Stage(
            { pose.x > -0.75 },
            {
                directionVector.x = -0.75
                directionVector.y = 0.0
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        )
    )
}
