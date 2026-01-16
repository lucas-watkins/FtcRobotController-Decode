package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.LedStrip

class AimingGuide {
    var strip: LedStrip
    var localization: Localization

    fun update() : Unit {
        val angle = localization.angleToGoal
        if (angle == null) {
            strip.setColorRed()
            return
        }

        if (angle >= -5 && angle <= 5) {
            strip.setColorGreen()
            return
        }

        if (angle < 5) {
            strip.setColorHalfGreenHalfRed()
        }

        if (angle > -5) {
            strip.setColorHalfRedHalfGreen()
        }
    }

    constructor(driver: GoBildaPrismDriver, localization: Localization) {
        this.localization = localization
        strip = LedStrip(driver)
    }
}