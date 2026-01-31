package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

val GoBildaPinpointDriver.velocityX: Double
    get() {
        return Units.convert(getVelX(DistanceUnit.CM), Units.CM, Units.TILE)
    }

val GoBildaPinpointDriver.velocityY: Double
    get() {
        return Units.convert(getVelY(DistanceUnit.CM), Units.CM, Units.TILE)
    }
