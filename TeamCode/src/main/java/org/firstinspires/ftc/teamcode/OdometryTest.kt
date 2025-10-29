package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.BaseOpMode
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import kotlin.math.abs
import kotlin.math.roundToInt

@TeleOp(name = "OdometryTest", group = "OpMode")
class OdometryTest : BaseOpMode() {
    private lateinit var odometry: GoBildaPinpointDriver

    override fun initialize() {
        odometry = hardwareMap.get(GoBildaPinpointDriver::class.java, "goBildaPinpoint")
        odometry.setOffsets(0.0, 20.0, DistanceUnit.CM)
        odometry.setEncoderResolution(37.25135125, DistanceUnit.MM)
        odometry.setEncoderDirections(EncoderDirection.FORWARD, EncoderDirection.REVERSED)
        odometry.resetPosAndIMU()
    }

    override fun loop() {
        odometry.update()
        uploadOdometryDebug()

        val turnPower = gamepad1.right_trigger - gamepad1.left_trigger

        val motorPowers = arrayOf(
            gamepad1.left_stick_y - gamepad1.left_stick_x - turnPower,
            gamepad1.left_stick_y + gamepad1.left_stick_x + turnPower,
            gamepad1.left_stick_y + gamepad1.left_stick_x - turnPower,
            gamepad1.left_stick_y - gamepad1.left_stick_x + turnPower,
        )

        val max = motorPowers.maxOf { i -> abs(i) }

        motorPowers.forEachIndexed {i, _ -> motorPowers[i] /= max}
        driveTrain.forEachIndexed {i, m -> m.power = motorPowers[i] * 0.33333 }
    }

    private fun uploadOdometryDebug() {
        telemetry.addLine("Odometry (cm):")

        telemetry.addLine("(${odometry.getPosX(DistanceUnit.CM).roundToInt()}, " +
                "${odometry.getPosY(DistanceUnit.CM).roundToInt()})")

        telemetry.update()
    }

}