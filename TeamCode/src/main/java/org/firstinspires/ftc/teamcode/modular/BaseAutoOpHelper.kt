package org.firstinspires.ftc.teamcode.modular

import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage

class BaseAutoOpHelper(
    private val helper: BaseOpHelper,
    private val directionVector: Vector2<Double>,
    private val turnPower: Vector2<Double>
) {
    val launchBallStage: Array<Stage>
        get() {

            val motorLaunchVelocity = 2175.0
            // might need to be a MutableReference
            var ballsLaunched = 0

            return arrayOf(
                Stage(
                    { helper.launchMotorsVelocity < motorLaunchVelocity },
                    {
                        directionVector.x = 0.0
                        directionVector.y = 0.0
                        turnPower.x = 0.0
                        turnPower.y = 0.0

                        helper.launchMotorsVelocity = motorLaunchVelocity
                    }
                ),

                Stage(
                    { ballsLaunched < 3 },
                    {
                        helper.launchBall()
                        ballsLaunched++
                    }
                ),

                Stage(
                    { helper.launchMotorsVelocity > motorLaunchVelocity - 1 },
                    {
                        helper.launchMotorsVelocity = 0.0
                    }
                )
            )
        }
}
