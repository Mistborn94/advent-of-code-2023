package helper

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CompositeRangeTest {

    @Test
    fun `Overlap - fully contained`() {
        val a = 0..10
        val b = 5..7
        val expected = CompositeRange.newIntRange(5..7)
        assertEquals(expected, CompositeRange.newIntRange(a).overlap(b), "Outer First")
        assertEquals(expected, CompositeRange.newIntRange(b).overlap(a), "Inner First")
    }

    @Test
    fun `Add - fully contained`() {
        val a = 0..10
        val b = 5..7
        val expected = CompositeRange.newIntRange(0..10)
        assertEquals(expected, CompositeRange.newIntRange(a) + b, "Outer First")
        assertEquals(expected, CompositeRange.newIntRange(b) + a, "Inner First")
    }

    @Test
    fun `Overlap - identical`() {
        val a = 0..10
        val expected = CompositeRange.newIntRange(a)
        assertEquals(expected, CompositeRange.newIntRange(a).overlap(a))
    }

    @Test
    fun `Add - identical`() {
        val a = 0..10
        val expected = CompositeRange.newIntRange(a)
        assertEquals(expected, CompositeRange.newIntRange(a) + a)
    }

    @Test
    fun `Overlap - no overlap`() {
        val a = 0..5
        val b = 6..10
        assertEquals(CompositeRange.emptyIntRange, CompositeRange.newIntRange(a).overlap(b), "Lower First")
        assertEquals(CompositeRange.emptyIntRange, CompositeRange.newIntRange(b).overlap(a), "Higher First")
    }

    @Test
    fun `Add - no overlap`() {
        val a = 0..5
        val b = 7..10
        val expected = CompositeRange.newIntRange(listOf(a, b))
        assertEquals(expected, CompositeRange.newIntRange(a) + b, "Lower First")
        assertEquals(expected, CompositeRange.newIntRange(b) + a, "Higher First")
    }

    @Test
    fun `Add - adjacent`() {
        val a = 0..5
        val b = 6..10
        val expected = CompositeRange.newIntRange(0..10)
        assertEquals(expected, CompositeRange.newIntRange(a) + b, "Lower First")
        assertEquals(expected, CompositeRange.newIntRange(b) + a, "Higher First")
    }

    @Test
    fun `Overlap - partial overlap`() {
        val a = 0..6
        val b = 5..10
        val expected = CompositeRange.newIntRange(5..6)
        assertEquals(expected, CompositeRange.newIntRange(a).overlap(b), "Lower First")
        assertEquals(expected, CompositeRange.newIntRange(b).overlap(a), "Higher First")
    }
}