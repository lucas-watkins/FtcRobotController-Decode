package org.firstinspires.ftc.teamcode.modular

import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver

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

    override fun toString() : String {
        val angle = localization.angleToGoal ?: return "no tag"

        if (angle >= -5 && angle <= 5) {
            return "perfectly aimed"
        }

        return if (angle < 5) "turn left" else "turn right"
    }

    constructor(driver: GoBildaPrismDriver, localization: Localization) {
        this.localization = localization
        strip = LedStrip(driver)
    }
}