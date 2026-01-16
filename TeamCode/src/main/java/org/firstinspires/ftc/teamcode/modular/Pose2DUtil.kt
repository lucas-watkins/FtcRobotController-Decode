package org.firstinspires.ftc.teamcode.modular

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

val Pose2D.x: Double
    get() {
        return Units.convert(getX(DistanceUnit.CM), Units.CM, Units.TILE)
    }

val Pose2D.y: Double
    get() {
        return Units.convert(getY(DistanceUnit.CM), Units.CM, Units.TILE)
    }

val Pose2D.angle: Double
    get() {
        return getHeading(AngleUnit.RADIANS)
    }
