package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Main class
 */
public class MainTest {
    @Test
    public void testMainMethod() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
