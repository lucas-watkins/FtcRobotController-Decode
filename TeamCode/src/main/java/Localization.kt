import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import kotlin.math.*

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

    private val validIDs = arrayOf(21, 24) // apriltag goal markers (we do NOT use the motif apriltag as the position can change every match and is unreliable)
    private val goalsX: Double = -1.4827 // this is the same for both goals
    private val bluGoalY: Double = -1.4133
    private val redGoalY: Double = 1.4133

    fun getFieldPosition() : FieldPos {
        val result: LLResult = limelight.latestResult
        return FieldPos(
            staleness = result.staleness,
            rawResult = result,
            pose = result.botpose,
            poseMT2 = result.botpose_MT2,
        )
    }

    var distanceFromGoal: Double = 0d
    get() {
        val result: LLResult = limelight.latestResult
        val robotYaw = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ,
            AngleUnit.DEGREES).firstAngle.toDouble() // THE DOCUMENTATION SAID THIS WOULD BE A DOUBLE
        limelight.updateRobotOrientation(robotYaw)
        val botPose: Pose3D= result.botpose_MT2
        var distance: Double
        if (alliance == Alliance.BLU) {
            distance = sqrt(exp(goalsX - botPose.position.x) + exp(bluGoalY - botPose.position.y))
        } else {
            distance = sqrt(exp(goalsX - botPose.position.x) + exp(redGoalY - botPose.position.y))
        }

        return distance
    }

    fun getRelativePositions() : ArrayList<RelativePos> {
        val result: LLResult = limelight.latestResult
        val positions = ArrayList<RelativePos>()

        if (result.isValid) {
            val fiducials : List<LLResultTypes.FiducialResult> = result.fiducialResults
            val age = result.staleness

            fiducials.forEach {
                positions.add(RelativePos(
                    type = when (it.fiducialId) {
                        21 -> AprilTagType.GPP
                        22 -> AprilTagType.PGP
                        23 -> AprilTagType.PPG
                        20 -> AprilTagType.BlueGoal
                        24 -> AprilTagType.RedGoal
                        else -> return@forEach
                    },
                    staleness = age,
                    rawResult = result,
                    rawFiducialResult = it,

                    // robotPoseTargetSpace: AprilTag pose in the camera's coordinate system. "not very useful", according to the docs (liars)
                    distanceX = it.robotPoseTargetSpace.position.x,
                    distanceY = it.robotPoseTargetSpace.position.y,
                    distanceZ = it.robotPoseTargetSpace.position.z, // this is the main value we want

                    angleX = it.targetXDegrees,
                    angleY = it.targetYDegrees,
                ))
            }
        }

        return positions
    }

    constructor(ll: Limelight3A, currentAlliance: Alliance, imu: IMU) {
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