package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous
import java.lang.Thread.sleep

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

        Stage(
            { leftLauncherMotor.velocity > -2175 && rightLauncherMotor.velocity < 2175 },
            {
                leftLauncherMotor.velocity = -2175.0
                rightLauncherMotor.velocity = 2175.0

                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = 0.0
            }
        ),

        Stage(
            { ballsLaunched < 3 },
            {
                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = 0.0

                if (servoLauncher.position < servoLaunch) {
                    servoLauncher.position = servoLaunch
                    ballsLaunched++
                    sleep(2000)
                }

                if (servoLauncher.position > servoRetract) {
                    servoLauncher.position = servoRetract
                    sleep(2000)
                }
            }
        ),

        Stage(
            { leftLauncherMotor.velocity < -2174 && rightLauncherMotor.velocity > 2174 },
            {
                leftLauncherMotor.velocity = 0.0
                rightLauncherMotor.velocity = 0.0

                directionVector.x = 0.0
                directionVector.y = 0.0
                turnPower = 0.0
            }
        ),

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