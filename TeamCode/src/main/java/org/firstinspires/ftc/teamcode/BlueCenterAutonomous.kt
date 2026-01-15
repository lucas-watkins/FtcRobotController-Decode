package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous

@Autonomous(name = "BlueCenterAutonomous", group = "BlueTeam")
class BlueCenterAutonomous : BaseAutonomous() {
    override val plan = AutoStageExecutor(
        Stage(
            { pose.y < 3.375 },// magic nubers, put units
            {
                directionVector.x = 0.0 // units
                directionVector.y = 3.375
                turnPower = 0.0
            }
        ),

        Stage(
            { pose.angle < 0.65 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = 0.75
            }
        ),

        *launchThreeBalls,

        Stage(
            { pose.angle > 0.2 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = -0.75
            }
        ),

        Stage(
            { pose.y > 2.25},
            {
                directionVector.x = 0.0
                directionVector.y = -2.25
                turnPower = 0.0
            }
        )
    )
}
