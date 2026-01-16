package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.LedStrip

@TeleOp(name = "LedTest", group = "OpMode")
class LedTest : BaseOpMode() {
    private lateinit var ledStrip: LedStrip
    private lateinit var driver: GoBildaPrismDriver

    override fun initialize() {
        driver = hardwareMap.get(GoBildaPrismDriver::class.java, "goBildaPrism")
        ledStrip = LedStrip(driver)
    }

    override fun loop() {
        if (gamepad1.aWasPressed()) {
            ledStrip.setColorGreen()
            telemetry.addLine("Color is green")
        }

        if (gamepad1.bWasPressed()) {
            ledStrip.setColorRed()
            telemetry.addLine("Color is red")
        }

        if (gamepad1.xWasPressed()) {
            ledStrip.setColorHalfGreenHalfRed()
            telemetry.addLine("Color is half green half red")
        }

        if (gamepad1.yWasPressed()) {
            ledStrip.setColorHalfRedHalfGreen()
            telemetry.addLine("Color is half red half green")
        }

        telemetry.update()
    }
}
