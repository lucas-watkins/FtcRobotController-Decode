package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.LedStrip

@TeleOp(name = "LedTest", group = "OpMode")
class LedTest : BaseOpMode() {
    private lateinit var ledStrip: LedStrip
    private lateinit var driver: GoBildaPrismDriver

    @Suppress("kotlin:S6530")
    override fun initialize() {
        driver = hardwareMap["goBildaPrism"] as GoBildaPrismDriver
        ledStrip = LedStrip(driver)
    }

    override fun loop() {

        when {
            gamepad1.aWasPressed() -> {
                ledStrip.setColorGreen()
                telemetry.addLine("Color is green")
            }

            gamepad1.bWasPressed() -> {
                ledStrip.setColorRed()
                telemetry.addLine("Color is red")
            }

            gamepad1.xWasPressed() -> {
                ledStrip.setColorHalfGreenHalfRed()
                telemetry.addLine("Color is half green half red")
            }

            gamepad1.yWasPressed() -> {
                ledStrip.setColorHalfRedHalfGreen()
                telemetry.addLine("Color is half red half green")
            }

            gamepad1.dpadLeftWasPressed() -> {
                ledStrip.setColorMotifGPP()
                telemetry.addLine("Color is motif gpp")
            }

            gamepad1.dpadUpWasPressed() -> {
                ledStrip.setColorMotifPGP()
                telemetry.addLine("Color is motif pgp")
            }

            gamepad1.dpadRightWasPressed() -> {
                ledStrip.setColorMotifPPG()
                telemetry.addLine("Color is motif ppg")
            }
        }

        telemetry.update()
    }
}
