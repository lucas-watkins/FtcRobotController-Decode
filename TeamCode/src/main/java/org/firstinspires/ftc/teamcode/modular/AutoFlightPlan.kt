package org.firstinspires.ftc.teamcode.modular

class AutoFlightPlan(val plan: Array<Stage>) {
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
            }

            if (currentStage == plan.lastIndex) {
                return false
            }
        }

        return true // never supposed to reach here
    }

}