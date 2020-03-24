package io.chocorean.authmod.core;

import net.minecraft.entity.player.EntityPlayerMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerDescriptorTest {


  private EntityPlayerMP player;
  private PlayerDescriptor descriptor;
  private PlayerPos position;

  @BeforeEach
  public void init() {
    this.position = new PlayerPos( null, 4, 4);
    this.player =  mock(EntityPlayerMP.class);
    this.descriptor = new PlayerDescriptor(this.player, this.position);
  }
  
  @Test
  public void testGetPlayer() {
    assertEquals(descriptor.getPlayer(), player);
  }

  @Test
  public void testGetPosition() {
    assertEquals(descriptor.getPosition(), position);
  }

}
