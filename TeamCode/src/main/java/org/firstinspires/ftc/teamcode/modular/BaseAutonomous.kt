package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.modular.GoBildaPrismDriver.GoBildaPrismDriver
import java.lang.Thread.sleep
import java.util.*
import kotlin.math.abs

abstract class BaseAutonomous : BaseOpMode() {
    protected abstract fun getPlan(): AutoStageExecutor
    protected abstract val alliance: MutableReference<Alliance>
    protected abstract val goalLocation: Pose2D
    protected var pose = MutableReference(Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.RADIANS, 0.0))
    protected val directionVector = Vector2(0.0, 0.0)
    protected var turnPower = Vector2(0.0, 0.0)
    protected var isOpModeActive = true
    protected var motif = MutableReference<Optional<AprilTagType>>(Optional.empty())
    protected lateinit var autoHelper: BaseAutoOpHelper
    protected lateinit var localization: Localization
    protected lateinit var ledStrip: LedStrip
    private lateinit var plan: AutoStageExecutor
    private lateinit var odometry: GoBildaPinpointDriver
    private lateinit var limelight: Limelight3A
    private lateinit var imu: IMU
    private val velocity = 800.0
    private var hasWaited = false
    private var waitPeriod: UInt = 0U

    @Suppress("kotlin:S6530")
    override fun initialize() {
        odometry = hardwareMap["goBildaPinpoint"] as GoBildaPinpointDriver
        odometry.setOffsets(6.0, -9.5, DistanceUnit.CM)
        odometry.setEncoderResolution(GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        odometry.setEncoderDirections(EncoderDirection.REVERSED, EncoderDirection.FORWARD)
        odometry.resetPosAndIMU()

        imu = hardwareMap["imu"] as IMU
        limelight = hardwareMap["limelight"] as Limelight3A
        localization = Localization(limelight, imu, alliance)

        ledStrip = LedStrip(hardwareMap["goBildaPrism"] as GoBildaPrismDriver)

        autoHelper = BaseAutoOpHelper(baseHelper, directionVector, turnPower, localization, motif, pose, goalLocation)
        plan = getPlan()

        driveTrain.forEach { m -> m.mode = RunMode.RUN_USING_ENCODER }
        driveTrain.forEach { m -> m.zeroPowerBehavior = ZeroPowerBehavior.FLOAT}
        servoLauncher.position = 0.7 // initialization position
    }

    @Suppress("kotlin:S100")
    override fun init_loop() {
        when {
           gamepad1.leftBumperWasPressed() -> waitPeriod--
           gamepad1.rightBumperWasPressed() -> waitPeriod++
        }

        telemetry.addLine("Current Wait Period: $waitPeriod seconds")
        telemetry.addLine("(Left and Right Bumper to Adjust)")
        telemetry.update()
    }

    override fun loop() {
        if (motif().isEmpty) {
            val newMotifs = localization.relativePositions.filter{ it.type.id in 21..23 }

            if (newMotifs.size == 1) {
                motif(Optional.of(newMotifs[0].type))

                when (motif().get()) {
                    AprilTagType.PPG -> ledStrip.setColorMotifPPG()
                    AprilTagType.PGP -> ledStrip.setColorMotifPGP()
                    AprilTagType.GPP -> ledStrip.setColorMotifGPP()
                    else             -> ledStrip.setColorRed()
                }
            }
        }

        if (!hasWaited) {
            sleep(waitPeriod.toLong() * 1000)
            hasWaited = true
        }

        telemetry.addLine("Direction: $directionVector")
        telemetry.addLine("Stage Number: ${plan.getStageNumber()}")
        telemetry.addLine("Angle: ${pose().angle}")
        telemetry.addLine("Odometry (tile): \n" +
                "x -> ${pose().x}\n" +
                "y -> ${pose().y}"
        )
        telemetry.addLine("Servo Pos: ${servoLauncher.position}")
        telemetry.addLine("Motif ${if (motif().isEmpty) "N/A" else motif().get().id}")
        telemetry.update()

        odometry.update()
        pose(odometry.position)


        if (isOpModeActive && !plan.update()) {
            directionVector.x = 0.0
            directionVector.y = 0.0
            turnPower.x = 0.0
            turnPower.y = 0.0
            isOpModeActive = false
        }

        rotateDoubleVector(directionVector, pose().angle)
        updateDriveTrain(directionVector, turnPower)
    }

    private fun updateDriveTrain(v: Vector2<Double>, turnPower: Vector2<Double>) {

        val motorPowers = arrayOf(
            -v.y - v.x + (turnPower.y - turnPower.x),
            -v.y + v.x - (turnPower.y - turnPower.x),
            -v.y + v.x + (turnPower.y - turnPower.x),
            -v.y - v.x - (turnPower.y - turnPower.x),
        )

        val max = motorPowers.maxOf { i -> abs(i) }

        motorPowers.forEachIndexed { i, _ -> motorPowers[i] /= max }
        driveTrain.forEachIndexed { i, m -> m.velocity = motorPowers[i] * -velocity }
    }
}
