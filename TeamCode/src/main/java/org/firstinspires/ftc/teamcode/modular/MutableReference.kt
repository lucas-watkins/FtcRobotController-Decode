package org.firstinspires.ftc.teamcode.modular

data class MutableReference<T>(var data: T) {
    operator fun invoke() : T {
        return data
    }

    operator fun invoke(newValue: T) {
        data = newValue
    }
}
