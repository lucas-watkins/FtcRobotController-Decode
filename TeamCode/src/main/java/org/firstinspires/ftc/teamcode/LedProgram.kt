package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.Color
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver.Artboard
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver.LayerHeight
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.PrismAnimations
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.PrismAnimations.AnimationBase

@TeleOp(name = "LedProgram", group = "OpMode")
class LedProgram : BaseOpMode() {
    private lateinit var driver: GoBildaPrismDriver
    private var initialized = false

    override fun initialize() {
        driver = hardwareMap.get(GoBildaPrismDriver::class.java, "goBildaPrism")
    }

    override fun loop() {
        if (!initialized) {
            val greenSolidAnimation = PrismAnimations.Solid(Color.GREEN, 255)
            val redSolidAnimation = PrismAnimations.Solid(Color.RED, 255)
            val greenRightHalfAnimation = PrismAnimations.Snakes(6, 0, 1000, Color.TRANSPARENT, 0.0f, Color.GREEN, Color.RED, Color.RED, Color.GREEN)
            val greenLeftHalfAnimation = PrismAnimations.Snakes(6, 0, 1000, Color.TRANSPARENT, 0.0f, Color.RED, Color.GREEN, Color.GREEN, Color.RED)

            program(greenLeftHalfAnimation, Artboard.ARTBOARD_0)
            program(greenRightHalfAnimation, Artboard.ARTBOARD_1)
            program(greenSolidAnimation, Artboard.ARTBOARD_2)
            program(redSolidAnimation, Artboard.ARTBOARD_3)

            initialized = true
        } else {
            telemetry.addLine("GoBilda Prism successfully programmed!")
            telemetry.update()
        }
    }

    private fun program(animation: AnimationBase, artboard: Artboard) {
        driver.insertAndUpdateAnimation(LayerHeight.LAYER_0, animation)
        driver.saveCurrentAnimationsToArtboard(artboard)
    }
}