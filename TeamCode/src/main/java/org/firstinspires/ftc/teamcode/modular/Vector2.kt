package org.firstinspires.ftc.teamcode.modular

import kotlin.math.cos
import kotlin.math.sin

data class Vector2<T>(var x: T, var y: T) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

fun rotateDoubleVector(v: Vector2<Double>, theta: Double) {
    v.x = (v.x * cos(theta) - v.y * sin(theta))
    v.y = (v.x * sin(theta) + v.y * cos(theta))
}
