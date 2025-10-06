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

    @Test
    fun `test diagonal`() {
        val path = pathBuilder.buildPath(Vector2(0, 0), Vector2(9, 9))

        assertContentEquals(Array(10) {i: Int -> Vector2(i, i)}, path)
    }

    @Test
    fun `test blocked`(){
        pathBuilder.grid[0][1].blocked = true
        val path = pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 9))

        assertContentEquals(Array(10) {i: Int -> if (i != 1) Vector2(0, i) else Vector2(1, 1)}, path)
    }

    @Test
    fun `test large diagonal`() {
        val localPathBuilder = PathBuilder(1000, 1000)
        val path = localPathBuilder.buildPath(Vector2(0, 0), Vector2(999, 999))

        assertContentEquals(Array(1000) {i: Int -> Vector2(i, i)}, path)
    }

    @Test(expected = RuntimeException::class)
    fun `test end invalid`() {
        pathBuilder.buildPath(Vector2(0, 0), Vector2(100, 100))
    }

    @Test(expected = RuntimeException::class)
    fun `test start invalid`() {
        pathBuilder.buildPath(Vector2(100, 100), Vector2(0, 0))
    }

    @Test(expected = RuntimeException::class)
    fun `test end blocked`() {
        pathBuilder.grid[0][9].blocked = true
        pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 9))
    }

    @Test(expected = RuntimeException::class)
    fun `test start blocked`() {
        pathBuilder.grid[0][0].blocked = true
        pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 9))
    }

    @Test
    fun `test start is end`() {
        val path = pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 0))
        assertContentEquals(emptyArray(), path)
    }

    @Test(expected = RuntimeException::class)
    fun `test destination not found`() {
        pathBuilder.grid[0][8].blocked = true
        pathBuilder.grid[1][8].blocked = true
        pathBuilder.grid[1][9].blocked = true

        pathBuilder.buildPath(Vector2(0, 0), Vector2(0, 9))
    }

}