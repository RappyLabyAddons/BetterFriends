package com.rappytv.betterfriends.core.interactions;

import com.rappytv.betterfriends.api.blocklist.BlocklistManager;
import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class UnblockPlayerBullet implements BulletPoint {

  private final BetterFriendsAddon addon;
  private final BlocklistManager manager = BetterFriendsAddon.references().blocklistManager();

  public UnblockPlayerBullet(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("betterfriends.interactions.unblock");
  }

  @Override
  public Icon getIcon() {
    return null;
  }

  @Override
  public void execute(Player player) {
    this.manager.unblock(player.getUniqueId());
  }

  @Override
  public boolean isVisible(Player playerInfo) {
    return this.addon.configuration().blocklist().showInteractionBullets().get()
        && this.manager.isBlocked(playerInfo.getUniqueId());
  }
}
