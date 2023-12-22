package day22

import helper.Debug
import helper.addAllIf
import helper.point.Cube
import helper.point.Point3D
import helper.point.base.Point
import helper.point.base.Rectangle
import helper.removeFirst
import kotlin.math.min

val translateDown = Point3D(0, 0, -1)
val translateUp = Point3D(0, 0, 1)

class CubeData(val above: List<Cube>, val below: List<Cube>)

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val bricks = parseInput(text)
    val (finalBrickPositions, _) = makeBricksFall(bricks)
    val brickSupportData = getBrickSupportData(finalBrickPositions)

    debug {
        println(printBricks("xz", finalBrickPositions.map { it.xzView() }))
        println(printBricks("yz", finalBrickPositions.map { it.yzView() }))
    }

    return finalBrickPositions.count { cube ->
        safeToRemove(brickSupportData, cube, debug)
    }
}

private fun getBrickSupportData(finalBrickPositions: List<Cube>) =
    finalBrickPositions.associateWith { cube ->
        val up = cube.translate(translateUp)
        val down = cube.translate(translateDown)
        val supporting = finalBrickPositions.filter { other -> other != cube && other != up && other.overlaps(up) }
        val supportedBy = finalBrickPositions.filter { other -> other != down && other != cube && other.overlaps(down) }
        CubeData(supporting, supportedBy)
    }

private fun safeToRemove(
    brickSupportData: Map<Cube, CubeData>,
    cube: Cube,
    debug: Debug
): Boolean {
    val cubeData = brickSupportData[cube]!!

    debug {
        println("${cube.label}: Is supporting ${cubeData.above.map { it.label }}")
    }

    val supportedHasExtraSupport = cubeData.above.all { supported ->
        val supportedData = brickSupportData[supported]!!

        debug {
            println("${supported.label} is supported by: ${supportedData.below.map { it.label }}")
        }

        supportedData.below.size > 1
    }

    val canBeRemoved = cubeData.above.isEmpty() || supportedHasExtraSupport
    debug {
        println("${cube.label} Can be removed: $canBeRemoved")
    }
    return canBeRemoved
}

private fun makeBricksFall(bricks: List<Cube>): Pair<List<Cube>, Int> {
    var fallCount = 0
    val finalBrickPositions = mutableListOf<Cube>()
    bricks.forEach { brick ->
        var current = brick
        var next = current.translate(translateDown)

        while (finalBrickPositions.none { it.overlaps(next) } && next.zRange.first > 0) {
            current = next
            next = next.translate(translateDown)
        }

        if (current != brick) {
            fallCount++
        }

        finalBrickPositions.add(current)
    }
    return Pair(finalBrickPositions, fallCount)
}

private fun parseInput(text: String): List<Cube> {
    var label = 'A'
    val bricks = text.lines().map {
        val split = it.split(',', '~')

        val pointA = Point3D(split[0].toInt(), split[1].toInt(), split[2].toInt())
        val pointB = Point3D(split[3].toInt(), split[4].toInt(), split[5].toInt())
        pointA to pointB
    }.sortedBy { min(it.first.z, it.second.z) }.map { (a, b) -> Cube.boundingBoxOf(a, b, "${label++}") }
    return bricks
}

private fun printBricks(label: String, rectangles: List<Rectangle>): String {
    val yRange = rectangles.maxOf { it.yRange.last } downTo rectangles.minOf { it.yRange.first }
    val xRange = rectangles.minOf { it.xRange.first }..rectangles.maxOf { it.xRange.last }

    return yRange.joinToString(prefix = "$label\n", postfix = "\n---------\n", separator = "\n") { y ->
        xRange.joinToString(separator = "") { x ->
            val point = Point(x, y)
            val matching = rectangles.filter { it.contains(point) }

            if (matching.isEmpty()) {
                "."
            } else if (matching.size == 1) {
                matching.first().label
            } else {
                "?"
            }
        }
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val bricks = parseInput(text)
    val (finalBrickPositions, _) = makeBricksFall(bricks)
    val brickSupportData = getBrickSupportData(finalBrickPositions)

    return finalBrickPositions.reversed().sumOf {
        fallCount(it, brickSupportData, debug)
    }
}

fun fallCount(cube: Cube, brickSupportData: Map<Cube, CubeData>, debug: Debug): Int {
    if (safeToRemove(brickSupportData, cube, debug)) {
        return 0
    } else {
        val fallen = mutableSetOf<Cube>()
        val toVisit = mutableSetOf(cube)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            val currentSupportData = brickSupportData[current]!!

            val willAlsoFall = currentSupportData.above.filter { supported ->
                val supportedData = brickSupportData[supported]!!
                supportedData.below.all { it == current || it in fallen || it in toVisit }
            }

            fallen.add(current)
            toVisit.addAllIf(willAlsoFall) { it !in fallen }
        }
        return fallen.size - 1
    }
}
