package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver

@TeleOp(name = "org.firstinspires.ftc.teamcode.Localization shenanigans", group = "Test opmodes")
class LocalizationTest : LinearOpMode() {
    lateinit var localizationizer: Localization
    lateinit var aimingGuide: AimingGuide

    override fun runOpMode() {
        val limelight = hardwareMap[Limelight3A::class.java, "limelight"]
        val imu = hardwareMap[IMU::class.java, "imu"]
        val driver = hardwareMap.get(GoBildaPrismDriver::class.java, "goBildaPrism")

        localizationizer = Localization(limelight, imu, Alliance.BLU)
        aimingGuide = AimingGuide(driver, localizationizer)
        waitForStart()

        while (opModeIsActive()) {
            val distance = localizationizer.distanceFromGoal
            val rpm = localizationizer.estimatedTicks
            val angle = localizationizer.angleToGoal
            aimingGuide.update()

            telemetry.addData("Distance from blue goal", distance)
            telemetry.addData("Estimated ticks", rpm)
            telemetry.addData("Angle to blue goal (degrees)", angle)
            telemetry.update()
        }
    }
}
