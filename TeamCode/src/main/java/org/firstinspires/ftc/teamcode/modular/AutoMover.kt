package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.DcMotorEx
import kotlin.math.abs

// Abysmal typealias name change later
typealias forEachIndexedPowerType = (i: Int, p: Double) -> Double
@Deprecated("Don't use. Use direction vectors instead")
class AutoMover(val driveTrain: Array<DcMotorEx>) {

    var direction = Direction.NONE

    // offset calculator: https://www.desmos.com/calculator/gqckl1t3cr
    companion object {
        const val LEFT_FRONT_OFFSET = 0.0
        const val RIGHT_FRONT_OFFSET = 0.0
        const val LEFT_REAR_OFFSET = 0.0
        const val RIGHT_REAR_OFFSET = 0.0
        val offsets = arrayOf(LEFT_FRONT_OFFSET, RIGHT_FRONT_OFFSET, LEFT_REAR_OFFSET, RIGHT_REAR_OFFSET)
    }

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
        ROTATE_RIGHT,
        ROTATE_LEFT,
    }

    init {
        assert(driveTrain.size == 4) { "the driveTrain must have 4 motors" }
    }

    fun goForward(velocity: Double) {
        direction = if (velocity < 0) Direction.BACKWARD else Direction.FORWARD
        driveTrain.forEach { m -> m.velocity = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.velocity = velocity * (1.0 + offsets[i]) }
    }

    fun goBackward(velocity: Double) {
       goForward(-velocity)
    }

    fun goLeft(velocity: Double) {
        direction = if (velocity < 0) Direction.RIGHT else Direction.LEFT
        driveTrain.forEach { m -> m.velocity = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.velocity = (if ( i % 3 == 0 ) -velocity else velocity) * (1.0 + offsets[i]) }
    }

    fun goRight(velocity: Double) {
        goLeft(-velocity)
    }

    fun goDiagonalLeftForward(velocity: Double) {
        direction = if (velocity < 0) Direction.DIAGONAL_LEFT_BACKWARD else Direction.DIAGONAL_LEFT_FORWARD
        driveTrain.forEach { m -> m.velocity = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.velocity = if ( i == 1 || i == 2) velocity * (1.0 + offsets[i]) else 0.0 }
    }

    fun goDiagonalLeftBackward(velocity: Double) {
        goDiagonalLeftForward(-velocity)
    }

    fun goDiagonalRightForward(velocity: Double) {
        direction = if (velocity < 0) Direction.DIAGONAL_RIGHT_BACKWARD else Direction.DIAGONAL_RIGHT_FORWARD
        driveTrain.forEach { m -> m.velocity = 0.0 }
        driveTrain.forEachIndexed { i, m -> m.velocity = if ( i == 0 || i == 3 ) velocity * (1.0 + offsets[i]) else 0.0 }
    }

    fun goDiagonalRightBackward(velocity: Double) {
        goDiagonalRightForward(-velocity)
    }

    fun rotateRight(velocity: Double) {
        direction = if (velocity < 0) Direction.ROTATE_LEFT else Direction.ROTATE_RIGHT
        driveTrain.forEachIndexed { i, m -> m.velocity = velocity * (1.0 + offsets[i]) * if ( i % 2 == 0 ) 1.0 else -1.0 }
    }

    fun rotateLeft(velocity: Double) {
        rotateRight(-velocity)
    }

    fun isGoingForward(): Boolean {
        return driveTrain.all { m -> m.velocity > 0.0 }
    }

    fun isGoingBackward(): Boolean {
        return driveTrain.all { m -> m.velocity < 0.0 }
    }

    fun isStopped(): Boolean {
        return driveTrain.all { m -> m.velocity == 0.0 }
    }

    fun isGoingLeft(): Boolean {
        return driveTrain[0].velocity < 0.0 && driveTrain[3].velocity < 0.0 &&
                driveTrain[1].velocity > 0.0 && driveTrain[2].velocity > 0.0
    }

    fun isGoingRight(): Boolean {
        return driveTrain[0].velocity > 0.0 && driveTrain[3].velocity > 0.0 &&
                driveTrain[1].velocity < 0.0 && driveTrain[2].velocity < 0.0
    }

    fun isGoingDiagonalLeft(): Boolean {
        return driveTrain[1].velocity > 0.0 && driveTrain[2].velocity > 0.0 && driveTrain[0].velocity == 0.0
                && driveTrain[3].velocity == 0.0
    }

    fun isGoingDiagonalRight(): Boolean {
        return driveTrain[1].velocity == 0.0 && driveTrain[2].velocity == 0.0 && driveTrain[0].velocity > 0.0
                && driveTrain[3].velocity > 0.0
    }

    fun isGoingDiagonalLeftBackward(): Boolean {
        return driveTrain[1].velocity < 0.0 && driveTrain[2].velocity < 0.0 && driveTrain[0].velocity == 0.0
                && driveTrain[3].velocity == 0.0
    }

    fun isGoingDiagonalRightBackward(): Boolean {
        return driveTrain[1].velocity == 0.0 && driveTrain[2].velocity == 0.0 && driveTrain[0].velocity < 0.0
                && driveTrain[3].velocity < 0.0
    }
}
