package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo

class baseOpHelperImpl(
    val leftGateServo : Servo,
    val rightGateServo: Servo,
    val lauchMotor: DcMotorEx,
    val servoLauncher: Servo,
) {
    fun leftGateServoCycle(){
        leftGateServo.position = 0.45
        Thread.sleep(500)
        leftGateServo.position = 0.6
    }
    // TODO Fix balls getting stuck
    // cycles the left servo position to briefly let a ball go though.
    // uses a delay the robot so uncontrolled for half a second.
    fun rightGateServoCycle(){
        rightGateServo.position = 0.5
        Thread.sleep(500)
        rightGateServo.position = 0.3
    }
    fun launchBall() {
        // fires the ball with a condition for high and low speed launching
        val firePos = 0.8
        val restingPos = 0.7
        if (lauchMotor.velocity > 1950) {
            servoLauncher.position = firePos
            Thread.sleep(500)
            servoLauncher.position = restingPos
        } else {
            // offset to account for wheels being smaller at slow speed
            servoLauncher.position = firePos + 0.1
            Thread.sleep(500)
            servoLauncher.position = restingPos
        }
    }
}