package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor

/*
* Initializes drivetrain other hardware TBD
*/
abstract class BaseOpMode : OpMode() {
    protected lateinit var leftFrontMotor: DcMotor
    protected lateinit var rightFrontMotor: DcMotor
    protected lateinit var leftRearMotor: DcMotor
    protected lateinit var rightRearMotor: DcMotor
    protected lateinit var leftLauncherMotor: DcMotor
    protected lateinit var rightLauncherMotor: DcMotor

    // Don't override this function, override initialize instead
    override fun init() {
        try {
            leftFrontMotor = hardwareMap.dcMotor["leftFrontMotor"]
            rightFrontMotor = hardwareMap.dcMotor["rightFrontMotor"]
            leftRearMotor = hardwareMap.dcMotor["leftRearMotor"]
            rightRearMotor = hardwareMap.dcMotor["rightRearMotor"]
            leftLauncherMotor = hardwareMap.dcMotor["leftLauncherMotor"]
            rightLauncherMotor = hardwareMap.dcMotor["rightLauncherMotor"]

            // Custom initialization block
            initialize()
        } catch (e: Exception) {
            if (e is IllegalArgumentException) {
                telemetry.addLine(e.message ?: "A hardware device could not be found")
            } else {
                telemetry.addLine("Failed to initialize hardware!!!")
            }
            telemetry.update()
        }
    }

    // This function must be overridden to initialize hardware related to the derived opmode
    abstract fun initialize()

}
