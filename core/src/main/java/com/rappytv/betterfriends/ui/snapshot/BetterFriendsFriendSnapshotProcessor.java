package com.rappytv.betterfriends.ui.snapshot;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.render.state.entity.EntitySnapshotProcessor;
import net.labymod.api.client.render.state.entity.EntitySnapshotRegistry;
import net.labymod.api.laby3d.renderer.snapshot.ExtrasWriter;
import net.labymod.api.service.annotation.AutoService;

@AutoService(EntitySnapshotProcessor.class)
public class BetterFriendsFriendSnapshotProcessor extends EntitySnapshotProcessor<Player> {

  public BetterFriendsFriendSnapshotProcessor(EntitySnapshotRegistry registry) {
    super(registry);
  }

  @Override
  public boolean supports(Entity entity) {
    return entity instanceof Player;
  }

  @Override
  public void process(Player player, float partialTicks, ExtrasWriter entityWriter) {
    this.registry().captureSnapshot(entityWriter, BetterFriendsKeys.FRIEND, player);
  }
}
