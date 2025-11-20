package org.firstinspires.ftc.teamcode.modular

data class Vector2<T>(var x: T, var y: T) {
    override fun toString(): String {
        return "($x, $y)"
    }
}
