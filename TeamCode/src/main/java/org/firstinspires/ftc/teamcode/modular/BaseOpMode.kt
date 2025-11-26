package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
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

    protected lateinit var leftLauncherMotor: DcMotorEx
    protected lateinit var rightLauncherMotor: DcMotorEx
    protected lateinit var servoLauncher: Servo

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
            driveTrain = arrayOf(leftFrontMotor, rightFrontMotor, leftRearMotor, rightRearMotor)

            driveTrain.forEachIndexed {i, m ->
                if (i != 1 && i != 3) { m.direction = DcMotorSimple.Direction.REVERSE }
                m.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
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

}
