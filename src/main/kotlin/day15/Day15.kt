package day15

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    return text.split(",").sumOf { hash(it) }
}

fun hash(string: String): Int = string.fold(0) { acc, c ->
    (acc + c.code) * 17 % 256
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val steps = text.split(",")
    val boxes = mutableMapOf<Int, LinkedHashMap<String, Int>>()
    steps.forEach {
        if (it.contains("=")) {
            val (lensLabel, lensSize) = it.split("=")
            val boxId = hash(lensLabel)

            val boxContents = boxes.getOrPut(boxId) { LinkedHashMap() }
            boxContents[lensLabel] = lensSize.toInt()

        } else if (it.contains("-")) {
            val lensLabel = it.substringBefore("-")
            val boxId = hash(lensLabel)
            val boxContents = boxes.getOrPut(boxId) { LinkedHashMap() }
            boxContents.remove(lensLabel)
        }
    }

    debug {
        println(boxes.filterValues { it.isNotEmpty() })
    }

    return boxes.entries.sumOf { (box, lenses) ->
        lenses.entries.mapIndexed { index, (_, lens) ->
            (box + 1) * (index + 1) * lens
        }.sum()
    }
}
