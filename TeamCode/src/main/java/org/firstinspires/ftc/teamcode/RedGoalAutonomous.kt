package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.Alliance
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous
import org.firstinspires.ftc.teamcode.modular.MutableReference

@Autonomous(name = "RedGoalAutonomous", group = "RedTeam")
class RedGoalAutonomous : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.RED)
    override fun getPlan() = AutoStageExecutor(
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
            { /*pose.angle < 2.9*/ pose.angle > -2.9 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                //turnPower.x = 0.0
                //turnPower.y = 0.75
                turnPower.x = 0.75
                turnPower.y = 0.0
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
