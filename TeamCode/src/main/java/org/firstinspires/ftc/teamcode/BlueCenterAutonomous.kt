package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.modular.*
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage

@Autonomous(name = "BlueCenterAutonomous", group = "BlueTeam")
class BlueCenterAutonomous : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.BLU)
    override val goalLocation = Pose2D(DistanceUnit.CM, -90.0, 330.0, AngleUnit.RADIANS, 0.0)
    override fun getPlan() = AutoStageExecutor(
        Stage(
            { pose().y < 3.375 },
            {
                directionVector.x = 0.0
                directionVector.y = 3.375
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        ),

        Stage(
            { (localization.angleToGoal ?: 10.0) > -20.0 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.y = 0.75
                turnPower.x = 0.0
            }
        ),

        *autoHelper.launchBallStage,

        Stage(
            { pose().angle > 0.2 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower.x = 0.75
                turnPower.y = 0.0
            }
        ),

        Stage(
            { pose().y > 2.25},
            {
                directionVector.x = 0.0
                directionVector.y = -2.25
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        )
    )
}
