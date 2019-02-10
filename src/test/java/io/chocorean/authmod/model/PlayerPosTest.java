package io.chocorean.authmod.model;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerPosTest {
    private PlayerPos playerPos;

    @BeforeEach
    void init() {
        this.playerPos = new PlayerPos(null, 0, 1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.playerPos, "The position should not be null");
    }

    @Test
    public void testGetPosition() {
        assertNull(
            this.playerPos.getPosition(),
            "The position is null in this case"
        );
    }

    @Test
    public void testYaw() {
        assertEquals(0, this.playerPos.getYaw(), "The Yaw should be 0");
    }

    @Test
    public void testPitch() {
        assertEquals(1, this.playerPos.getPitch(), "The pitch should be 1");
    }

}

