import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A

enum class AprilTagType(val id: Int) {
    GPP(21), // green purple purple
    PGP(22), // purple green purple
    PPG(23), // purple purple green
    BlueGoal(20),
    RedGoal(24)
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

class AprilTagDetector {
    var limelight: Limelight3A = Limelight3A(null, "limelight", null)

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
                    distanceZ = it.robotPoseTargetSpace.position.z,

                    angleX = it.targetXDegrees,
                    angleY = it.targetYDegrees,
                ))
            }
        }

        return positions
    }

    constructor() {
        limelight.setPollRateHz(100)
        limelight.start()

        // the pipeline should be set to zero (the only thing we're doing with the limelight is detecting apriltags) but this is just to make sure
        // do let me know if the pipeline changes
        limelight.pipelineSwitch(0)
    }
}