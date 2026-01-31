package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.modular.Alliance
import org.firstinspires.ftc.teamcode.modular.AutoStageExecutor
import org.firstinspires.ftc.teamcode.modular.BaseAutonomous
import org.firstinspires.ftc.teamcode.modular.MutableReference

@Autonomous(name = "BlueShootThreeBalls")
class BlueShootThreeBalls : BaseAutonomous() {
    override val alliance = MutableReference(Alliance.BLU)
    override fun getPlan() = AutoStageExecutor(
        *autoHelper.launchBallStage
    )
}