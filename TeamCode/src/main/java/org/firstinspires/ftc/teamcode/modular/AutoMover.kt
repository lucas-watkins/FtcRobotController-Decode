package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.DcMotorEx

class AutoMover(private val driveTrain: Array<DcMotorEx>) {

    var direction = Direction.NONE

    enum class Direction {
        NONE,
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        DIAGONAL_LEFT_FORWARD,
        DIAGONAL_LEFT_BACKWARD,
        DIAGONAL_RIGHT_FORWARD,
        DIAGONAL_RIGHT_BACKWARD,
    }

    init {
        assert(driveTrain.size == 4) { "the driveTrain must have 4 motors" }
    }

    fun goForward(power: Double) {
        direction = if (power < 0) Direction.BACKWARD else Direction.FORWARD
        driveTrain.forEach { m -> m.power = 0.0 }
        driveTrain.forEach { m -> m.power = power }
    }

    fun goBackward(power: Double) {
       goForward(-power)
    }

    fun goLeft(power: Double) {
        direction = if (power < 0) Direction.RIGHT else Direction.LEFT
        driveTrain.forEach { m -> m.power = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.power = if ( i % 3 == 0 ) -power else power }
    }

    fun goRight(power: Double) {
        goLeft(-power)
    }

    fun goDiagonalLeftForward(power: Double) {
        direction = if (power < 0) Direction.DIAGONAL_LEFT_BACKWARD else Direction.DIAGONAL_LEFT_FORWARD
        driveTrain.forEach { m -> m.power = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.power = if ( i == 1 || i == 2) power else 0.0 }
    }

    fun goDiagonalLeftBackward(power: Double) {
        goDiagonalLeftForward(-power)
    }

    fun goDiagonalRightForward(power: Double) {
        direction = if (power < 0) Direction.DIAGONAL_RIGHT_BACKWARD else Direction.DIAGONAL_RIGHT_FORWARD
        driveTrain.forEach { m -> m.power = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.power = if ( i == 0 || i == 3 ) power else 0.0 }
    }

    fun goDiagonalRightBackward(power: Double) {
        goDiagonalRightForward(-power)
    }

    fun isGoingForward(): Boolean {
        return driveTrain.all { m -> m.power > 0.0 }
    }

    fun isGoingBackward(): Boolean {
        return driveTrain.all { m -> m.power < 0.0 }
    }

    fun isStopped(): Boolean {
        return driveTrain.all { m -> m.power == 0.0 }
    }

    fun isGoingLeft(): Boolean {
        return driveTrain[0].power < 0.0 && driveTrain[3].power < 0.0 &&
                driveTrain[1].power > 0.0 && driveTrain[2].power > 0.0
    }

    fun isGoingRight(): Boolean {
        return driveTrain[0].power > 0.0 && driveTrain[3].power > 0.0 &&
                driveTrain[1].power < 0.0 && driveTrain[2].power < 0.0
    }

    fun isGoingDiagonalLeft(): Boolean {
        return driveTrain[1].power > 0.0 && driveTrain[2].power > 0.0 && driveTrain[0].power == 0.0
                && driveTrain[3].power == 0.0
    }

    fun isGoingDiagonalRight(): Boolean {
        return driveTrain[1].power == 0.0 && driveTrain[2].power == 0.0 && driveTrain[0].power > 0.0
                && driveTrain[3].power > 0.0
    }

    fun isGoingDiagonalLeftBackward(): Boolean {
        return driveTrain[1].power < 0.0 && driveTrain[2].power < 0.0 && driveTrain[0].power == 0.0
                && driveTrain[3].power == 0.0
    }

    fun isGoingDiagonalRightBackward(): Boolean {
        return driveTrain[1].power == 0.0 && driveTrain[2].power == 0.0 && driveTrain[0].power < 0.0
                && driveTrain[3].power < 0.0
    }
}
