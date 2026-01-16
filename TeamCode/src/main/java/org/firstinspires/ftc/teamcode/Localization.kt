package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import kotlin.math.sqrt

enum class AprilTagType(val id: Int) {
    GPP(21), // green purple purple
    PGP(22), // purple green purple
    PPG(23), // purple purple green
    BlueGoal(20),
    RedGoal(24)
}

enum class Alliance() {
    BLU,
    RED
}

// relative distance from a tag. this is NOT the actual position of the tag
data class RelativePos(
    val type: AprilTagType, // tag id in enum format because magic numbers bad
    val staleness: Long, // the age (in milliseconds) of the result. you probably want to discard results older then 100ms

    // escape hatch
    val rawResult: LLResult,
    val rawFiducialResult: LLResultTypes.FiducialResult,

    val distanceX: Double,
    val distanceY: Double,
    val distanceZ: Double, // probably not needed for aiming the goal

    val angleX: Double,
    val angleY: Double,
    // no angleZ thing
)

// the position in the game field
data class FieldPos(
    val staleness: Long,
    val rawResult: LLResult,
    val pose: Pose3D,
    val poseMT2: Pose3D
)

class Localization {
    val limelight: Limelight3A
    var alliance: Alliance
    val imu: IMU

    private val bluGoalX = -1.4827
    private val bluGoalY = -1.4133

    private val redGoalX = -1.4827
    private val redGoalY = 1.4133

    private var _fieldPosition: FieldPos? = null
    val fieldPosition: FieldPos?
        get() {
            val robotYaw = imu.robotYawPitchRollAngles.yaw
            limelight.updateRobotOrientation(robotYaw)
            val result: LLResult = limelight.latestResult

            if (!result.isValid) {
                return _fieldPosition
            }

            _fieldPosition = FieldPos(
                staleness = result.staleness,
                rawResult = result,
                pose = result.botpose,
                poseMT2 = result.botpose_MT2
            )

            return _fieldPosition
        }

    val distanceFromGoal: Double
        get() {
            val fieldPos = this.fieldPosition ?: return 0.0 // sentinal value for test

            val (goalX, goalY) = if (alliance == Alliance.BLU) {
                Pair(bluGoalX, bluGoalY)
            } else {
                Pair(redGoalX, redGoalY)
            }

            val robotX = fieldPos.pose.position.x
            val robotY = fieldPos.pose.position.y

            val dx = goalX - robotX
            val dy = goalY - robotY

            return sqrt(dx * dx + dy * dy) * 100 // it gets converted into cm
        }

    private var _estimatedTicks: Double = 0.0
    val estimatedTicks: Double
        get() {
            _estimatedTicks = (1.5102 * this.distanceFromGoal) + 1837.16327
            return _estimatedTicks
        }

    constructor(ll: Limelight3A, imu: IMU, currentAlliance: Alliance) {
        limelight = ll
        limelight.setPollRateHz(100)
        limelight.start()

        // the pipeline should be set to zero (the only thing we're doing with the limelight is detecting apriltags) but this is just to make sure
        // do let me know if the pipeline changes
        limelight.pipelineSwitch(0)

        alliance = currentAlliance
        this.imu = imu
    }
}