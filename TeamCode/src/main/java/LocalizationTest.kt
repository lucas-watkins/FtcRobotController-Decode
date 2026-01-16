import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU

@TeleOp(name = "Localization shenanigans", group = "Test opmodes")
class LocalizationTest : LinearOpMode() {
    lateinit var localizationizer: Localization

    override fun runOpMode() {
        val limelight = hardwareMap[Limelight3A::class.java, "limelight"]
        val imu = hardwareMap[IMU::class.java, "imu"]
        localizationizer = Localization(limelight, imu, Alliance.BLU)
        waitForStart()

        while (opModeIsActive()) {
            val distance = localizationizer.distanceFromGoal
            val rpm = localizationizer.estimatedTicks

            telemetry.addData("Distance from blue goal", distance)
            telemetry.addData("Estimated ticks", rpm)
            telemetry.update()
        }
    }
}
