package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.DcMotorEx

class AutoMover(private val driveTrain: Array<DcMotorEx>) {

    init {
        assert(driveTrain.size == 4) { "the driveTrain must have 4 motors" }
    }

    fun goForward(power: Double) {
        driveTrain.forEach { m -> m.power = power }
    }

    fun goBackward(power: Double) {
       goForward(-power)
    }

    fun goLeft(power: Double) {
        driveTrain.forEachIndexed { i, m -> m.power = if ( i % 2 == 0 ) -power else power }
    }

    fun goRight(power: Double) {
        goLeft(-power)
    }

    fun goDiagonalLeftForward(power: Double) {
        driveTrain.forEachIndexed { i, m -> m.power = if ( i == 1 || i == 2) power else 0.0 }
    }

    fun goDiagonalLeftBackward(power: Double) {
        goDiagonalLeftForward(-power)
    }

    fun goDiagonalRightForward(power: Double) {
        driveTrain.forEachIndexed { i, m -> m.power = if ( i == 0 || i == 3 ) power else 0.0 }
    }

    fun goDiagonalRightBackward(power: Double) {
        goDiagonalRightForward(-power)
    }

}
