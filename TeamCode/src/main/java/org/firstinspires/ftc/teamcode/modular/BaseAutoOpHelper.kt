package org.firstinspires.ftc.teamcode.modular

import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor.Stage
import java.lang.Thread.sleep
import java.util.*

class BaseAutoOpHelper(
    private val helper: BaseOpHelper,
    private val directionVector: Vector2<Double>,
    private val turnPower: Vector2<Double>,
    private val localization: Localization,
    private val motif: MutableReference<Optional<AprilTagType>>,
) {
    val launchBallStage: Array<Stage>
        get() {


            // might need to be a MutableReference
            var ballsLaunched = 0
            var launchTheGreenBall = false
            var greenBallsLaunched = 0

            return arrayOf(
                Stage(
                    { helper.launchMotorsVelocity - localization.estimatedTicks !in -50.0..50.0 },
                    {
                        directionVector.x = 0.0
                        directionVector.y = 0.0
                        turnPower.x = 0.0
                        turnPower.y = 0.0

                        helper.launchMotorsVelocity = localization.estimatedTicks
                    }
                ),

                Stage(
                    { ballsLaunched < 3 },
                    {
                        when {
                            motif().isEmpty -> {
                                launchTheGreenBall = greenBallsLaunched == 0
                            }

                            else -> {
                                when (motif().get()) {
                                    AprilTagType.GPP -> {
                                        if (ballsLaunched == 0) {
                                            launchTheGreenBall = true
                                        }
                                    }
                                    AprilTagType.PGP -> {
                                        if (ballsLaunched == 1) {
                                            launchTheGreenBall = true
                                        }
                                    }
                                    AprilTagType.PPG -> {
                                        if (ballsLaunched == 2) {
                                            launchTheGreenBall = true
                                        }
                                    }

                                    else -> { /* REALLY BAD */ }
                                }
                            }
                        }

                        // Maybe swap the servo function calls if it doesn't work out
                        if (launchTheGreenBall) {
                            helper.leftGateServoCycle()
                            greenBallsLaunched++
                            launchTheGreenBall = false
                        } else {
                            helper.rightGateServoCycle()
                        }

                        sleep(750)

                        helper.launchBall()
                        ballsLaunched++

                        helper.launchMotorsVelocity = localization.estimatedTicks
                    }
                ),

                Stage(
                    { helper.launchMotorsVelocity > 25 },
                    {
                        helper.launchMotorsVelocity = 0.0
                    }
                )
            )
        }
}
