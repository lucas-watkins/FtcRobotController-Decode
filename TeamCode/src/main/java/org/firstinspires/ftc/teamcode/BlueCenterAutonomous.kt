package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.*
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage

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
            { pose.angle < 0.70 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.y = 0.2
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
