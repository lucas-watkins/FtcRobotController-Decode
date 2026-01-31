package org.firstinspires.ftc.teamcode.modular

class AutoStageExecutor(vararg val plan: Stage) {
    data class Stage(val condition: () -> Boolean, val exec: () -> Unit)

    private var currentStage = 0

    fun getStageNumber(): Int {
        return currentStage
    }

    fun update(): Boolean {
        if (plan.isEmpty()) {
            return false
        }

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
