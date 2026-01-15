package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous

@Autonomous(name = "BlueGoalAutonomous", group = "BlueTeam")
class BlueGoalAutonomous : BaseAutonomous() {
    override val plan = AutoStageExecutor(
        Stage(
            { pose.y < 1.977 },
            {
                directionVector.x = 0.0
                directionVector.y = 1.977
                turnPower = 0.0
            }
        ),

        Stage(
            { pose.angle < 2.9 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = 0.75
            }
        ),

        *launchThreeBalls,

        Stage(
            { pose.x < 0.75 },
            {
                directionVector.x = 0.75
                directionVector.y = 0.0
                turnPower = 0.0
            }
        )
    )
}
