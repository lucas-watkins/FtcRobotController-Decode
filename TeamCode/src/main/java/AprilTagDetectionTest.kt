import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.jvm.javaClass

@TeleOp
class AprilTagDetectionTest : LinearOpMode() {
    lateinit var aprilTagDetector: AprilTagDetector

    override fun runOpMode() {
        aprilTagDetector = AprilTagDetector()
        waitForStart()

        while (opModeIsActive()) {
            val positions = aprilTagDetector.getRelativePositions()

            positions.forEach {
                telemetry.addLine()
                telemetry.addData("type: ", it.type)
                telemetry.addData("age: ", it.staleness)
                telemetry.addData("distance x: ", it.distanceX)
                telemetry.addData("distance y: ", it.distanceY)
                telemetry.addData("distance z: ", it.distanceZ)
                telemetry.addData("angle x: ", it.angleX)
                telemetry.addData("angle y: ", it.angleY)
                telemetry.update()
            }
        }
    }
}