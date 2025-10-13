package org.firstinspires.ftc.teamcode.modular

enum class Units(private val convFactors: Array<Double>) {
    // Conversion factors are in order of TILE, IN, FT, CM, M
    TILE(arrayOf(1.0, 24.0, 2.0, 60.69, 0.6069)),
    IN(arrayOf(0.0416666667, 1.0, 0.0833333333, 2.54, 0.0254)),
    FT(arrayOf(0.5, 12.0, 1.0, 30.48, 0.3048)),
    CM(arrayOf(0.0164771791, 0.3937007874, 0.032808399, 1.0, 100.0)),
    M(arrayOf(1.64771791, 39.37007874, 3.2808399, 100.0, 1.0));

    fun to(n: Number, destUnit: Units): Double {
        return convert(n, this, destUnit)
    }

    companion object {
        fun convert(n: Number, sourceUnit: Units, destUnit: Units): Double {
            return n.toDouble() * sourceUnit.convFactors[destUnit.ordinal]
        }
    }
}
