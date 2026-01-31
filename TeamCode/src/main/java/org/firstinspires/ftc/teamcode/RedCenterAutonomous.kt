package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.*
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage

@Autonomous(name = "RedCenterAutonomous", group = "RedTeam")
class RedCenterAutonomous : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.RED)
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
            { pose.angle > -0.70 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.x = 0.75
                turnPower.y = 0.0
            }
        ),

        *autoHelper.launchBallStage,

        Stage(
            { pose.angle < -0.2 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.x = 0.0
                turnPower.y = 0.75
            }
        ),

        Stage(
            { pose.y > 2.25 },
            {
                directionVector.x = 0.0
                directionVector.y = -2.25
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        )
    )
}
