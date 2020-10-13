package de.unruh.javapatterns;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloneableIteratorTest {

    @SafeVarargs
    public static <T> void assertIteratorContains(Iterator<T> iterator, T... elements) {
        for (T element : elements) {
            assertTrue(iterator.hasNext());
            assertEquals(element, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void cloned() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        final CloneableIterator<Integer> cloneableIterator = CloneableIterator.from(iterator);
        assertEquals(1, cloneableIterator.next());
        CloneableIterator<Integer> cloned = cloneableIterator.clone();
        assertIteratorContains(cloneableIterator, 2, 3);
        assertIteratorContains(cloned, 2, 3);
    }

    @Test
    void noncloned() {
        Iterator<Integer> iterator = List.of(1, 2, 3).iterator();
        final CloneableIterator<Integer> cloneableIterator = CloneableIterator.from(iterator);
        assertIteratorContains(cloneableIterator, 1, 2, 3);
    }
}