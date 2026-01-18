package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import org.firstinspires.ftc.teamcode.modular.LedStrip
import org.firstinspires.ftc.teamcode.modular.Localization
import kotlin.time.DurationUnit
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class AimingGuide {
    var strip: LedStrip
    var localization: Localization
    var rateLimit = true
    private val timeSource = TimeSource.Monotonic
    private var lastMark: TimeMark

    fun update() {
        val angle = localization.angleToGoal

        if (lastMark.elapsedNow().toLong(DurationUnit.MILLISECONDS) > 500 || !rateLimit) {
            lastMark = timeSource.markNow()
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
                return
            }

            if (angle > -5) {
                strip.setColorHalfRedHalfGreen()
                return
            }

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
        lastMark = timeSource.markNow()
    }
}