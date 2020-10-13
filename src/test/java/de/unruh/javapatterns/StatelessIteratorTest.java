package de.unruh.javapatterns;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatelessIteratorTest {

    @Test
    void multiaccess() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        final StatelessIterator<Integer> statelessIterator = StatelessIterator.from(iterator);
        assertEquals(1, statelessIterator.getHead());
        assertEquals(2, statelessIterator.getTail().getHead());
        assertEquals(3, statelessIterator.getTail().getTail().getHead());
        assertFalse(statelessIterator.getTail().getTail().getTail().nonEmpty());
    }
}