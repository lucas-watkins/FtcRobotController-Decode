package org.firstinspires.ftc.teamcode.modular

import kotlin.test.Test
import kotlin.test.assertTrue

class UnitsTest {
    private fun isInEpsilon(x: Double, y: Double, epsilon: Double = 0.000001): Boolean {
        return x in (y - epsilon)..(y + epsilon)
    }

    @Test
    fun `inches to feet`() {
        assertTrue(isInEpsilon(144.0, Units.convert(12, Units.FT, Units.IN)))
    }

    @Test
    fun `inches to tile`() {
        assertTrue(isInEpsilon(1.0, Units.convert(24, Units.IN, Units.TILE)))
    }

    @Test
    fun `tile to feet`() {
        assertTrue(isInEpsilon(2.0, Units.convert(1.0, Units.TILE, Units.FT)))
    }

}
