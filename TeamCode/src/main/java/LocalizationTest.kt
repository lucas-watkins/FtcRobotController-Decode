import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU

@TeleOp(name = "Localization shenaigans", group = "Test opmodes")
class AprilTagDetectionTest : LinearOpMode() {
    lateinit var aprilTagDetector: Localization
    val limelight = hardwareMap[Limelight3A::class.java, "limelight"]
    val imu = hardwareMap[IMU::class.java, "imu"]

    override fun runOpMode() {
        val localizerinator = Localization(limelight, Alliance.BLU, imu)
        waitForStart()

        while (opModeIsActive()) {
            val distance = localizerinator.distanceFromGoal

        }
    }
}
