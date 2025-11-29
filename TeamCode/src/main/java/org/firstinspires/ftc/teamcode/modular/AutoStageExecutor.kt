package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver

class AutoStageExecutor(val plan: Array<Stage>) {
    data class Stage(val condition: () -> Boolean, val exec: () -> Unit)

    private var currentStage = 0

    fun getStageNumber(): Int {
        return currentStage
    }

    fun update(): Boolean {
        val stage = plan[currentStage]

        if (stage.condition()) {
            stage.exec()
            return true
        } else {
            if (currentStage + 1 <= plan.lastIndex) {
                currentStage++
                return true
            }

            return false
        }
    }
}
