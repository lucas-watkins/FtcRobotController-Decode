package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.modular.*
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage


@Autonomous(name = "RedGoalAutonomous", group = "RedTeam")
class RedGoalAutonomous : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.RED)
    override val goalLocation = Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0)
    override fun getPlan() = AutoStageExecutor(
        Stage(
            { pose().y < 1.977 },
            {
                directionVector.x = 0.0
                directionVector.y = 1.977
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        ),

        Stage(
            { localization.angleToGoal == null || (localization.angleToGoal ?: -10.0) < -0.5 },
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
            { pose().x > -0.75 },
            {
                directionVector.x = -0.75
                directionVector.y = 0.0
                turnPower.x = 0.0
                turnPower.y = 0.0
            }
        )
    )
}
