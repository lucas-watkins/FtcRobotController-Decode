package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

/*
* Initializes drivetrain other hardware TBD
*/
abstract class BaseOpMode : OpMode() {
    protected lateinit var leftFrontMotor: DcMotorEx
    protected lateinit var rightFrontMotor: DcMotorEx
    protected lateinit var leftRearMotor: DcMotorEx
    protected lateinit var rightRearMotor: DcMotorEx
    protected lateinit var driveTrain: Array<DcMotorEx>
    protected  lateinit var launcherMotors: Array<DcMotorEx>
    protected lateinit var leftLauncherMotor: DcMotorEx
    protected lateinit var rightLauncherMotor: DcMotorEx
    protected lateinit var servoLauncher: Servo



     lateinit var leftGateServo: Servo
    
     lateinit var rightGateServo: Servo



    // Don't override this function, override initialize instead
    override fun init() {
        try {
            leftFrontMotor = hardwareMap["leftFrontMotor"] as DcMotorEx
            rightFrontMotor = hardwareMap["rightFrontMotor"] as DcMotorEx
            leftRearMotor = hardwareMap["leftRearMotor"] as DcMotorEx
            rightRearMotor = hardwareMap["rightRearMotor"] as DcMotorEx

            leftLauncherMotor = hardwareMap["leftLauncherMotor"] as DcMotorEx
            rightLauncherMotor = hardwareMap["rightLauncherMotor"] as DcMotorEx
            servoLauncher = hardwareMap["servoLauncher"] as Servo
            leftGateServo = hardwareMap["rightGateServo"] as Servo
            rightGateServo = hardwareMap["leftGateServo"] as Servo
            driveTrain = arrayOf(leftFrontMotor, rightFrontMotor, leftRearMotor, rightRearMotor)
            launcherMotors = arrayOf(rightLauncherMotor, leftLauncherMotor)

            //stops and resets then set to run at the start of every op mode
            // this is needed as otherwise the encoder values will be false

            leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            leftRearMotor.direction = DcMotorSimple.Direction.FORWARD
            rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            rightRearMotor.direction = DcMotorSimple.Direction.FORWARD


            rightLauncherMotor.direction = DcMotorSimple.Direction.FORWARD
            leftLauncherMotor.direction = DcMotorSimple.Direction.REVERSE




            for( m in launcherMotors){
                m.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                m.mode = DcMotor.RunMode.RUN_USING_ENCODER
            }

            driveTrain.forEach {m ->
                m.mode = RunMode.RUN_USING_ENCODER
            }

            // Custom initialization block
            initialize()
        } catch (e: Exception) {
            if (e is IllegalArgumentException) {
                telemetry.addLine(e.message ?: "A hardware device could not be found")
            } else {
                telemetry.addLine("Failed to initialize hardware!")
            }
            telemetry.update()
        }
    }


    // This function must be overridden to initialize hardware related to the derived opmode
    abstract fun initialize()

    // TODO Fix balls getting stuck
    fun leftGateServoCycle(){
        leftGateServo.position = 0.45
        Thread.sleep(500)
        leftGateServo.position = 0.66
    }
    fun rightGateServoCycle(){
        rightGateServo.position = 0.5
        Thread.sleep(500)
        rightGateServo.position = 0.3
    }



}
