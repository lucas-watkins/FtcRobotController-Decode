package org.firstinspires.ftc.teamcode.modular

import kotlin.test.Test
import kotlin.test.assertContentEquals

class PathBuilderTest {

    private val pathBuilder = PathBuilder(10, 10)

    @Test
    fun `test horizontal`() {
        val path = pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 9))

        assertContentEquals(Array(10) {i: Int -> Vector2(0, i)}, path)
    }

    @Test
    fun `test vertical`() {
        val path = pathBuilder.buildPath(Vector2(0, 0), Vector2(9, 0))

        assertContentEquals(Array(10) {i: Int -> Vector2(i, 0)}, path)
    }

}