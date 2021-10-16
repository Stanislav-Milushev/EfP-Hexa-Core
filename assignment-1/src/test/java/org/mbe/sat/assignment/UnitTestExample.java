package org.mbe.sat.assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitTestExample {

    private static final Logger LOG = LoggerFactory.getLogger(UnitTestExample.class);

    @BeforeEach
    public void setup() {
        LOG.debug("Setting up test");
        // Test setup that is run before each test
    }

    @Test
    public void sampleTest() {
        LOG.info("Executing sample test");
        Assertions.assertTrue(true);
    }
}
