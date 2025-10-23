package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx

/*
* Initializes drivetrain other hardware TBD
*/
abstract class BaseOpMode : OpMode() {
    protected lateinit var leftFrontMotor: DcMotorEx
    protected lateinit var rightFrontMotor: DcMotorEx
    protected lateinit var leftRearMotor: DcMotorEx
    protected lateinit var rightRearMotor: DcMotorEx
    protected val driveTrain = arrayOf(leftFrontMotor, rightFrontMotor, leftRearMotor, rightRearMotor)

    protected lateinit var leftLauncherMotor: DcMotorEx
    protected lateinit var rightLauncherMotor: DcMotorEx

    // Don't override this function, override initialize instead
    override fun init() {
        try {
            leftFrontMotor = hardwareMap.get(DcMotorEx::class.java ,"leftFrontMotor")
            rightFrontMotor = hardwareMap.get(DcMotorEx::class.java ,"rightFrontMotor")
            leftRearMotor = hardwareMap.get(DcMotorEx::class.java ,"leftRearMotor")
            rightRearMotor = hardwareMap.get(DcMotorEx::class.java ,"rightRearMotor")
            leftLauncherMotor = hardwareMap.get(DcMotorEx::class.java ,"leftLauncherMotor")
            rightLauncherMotor = hardwareMap.get(DcMotorEx::class.java ,"rightLauncherMotor")

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
