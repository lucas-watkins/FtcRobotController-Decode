package org.firstinspires.ftc.teamcode.modular

import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver.Artboard

class LedStrip(private val driver: GoBildaPrismDriver) {
    fun setColorHalfGreenHalfRed() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_0)
    }

    fun setColorHalfRedHalfGreen() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_1)
    }

    fun setColorGreen() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_2)
    }

    fun setColorRed() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_3)
    }

    fun setColorMotifPPG() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_4)
    }

    fun setColorMotifPGP() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_5)
    }

    fun setColorMotifGPP() {
        driver.loadAnimationsFromArtboard(Artboard.ARTBOARD_6)
    }
}
