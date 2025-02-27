package com.rappytv.betterfriends.interactions;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

/**
 * This needs to be fixed soon
 */
public class FriendNoteEditorBullet implements BulletPoint {

  private final BetterFriendsAddon addon;
  private Friend friend = null;

  public FriendNoteEditorBullet(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("betterfriends.interactions.editNote.title");
  }

  @Override
  public Icon getIcon() {
    return SpriteCommon.EDIT;
  }

  @Override
  public void execute(Player player) {
    // TODO: Fix this
    Laby.labyAPI().minecraft().executeNextTick(this.friend::openNoteEditor);
  }

  @Override
  public boolean isVisible(Player player) {
//    if (!this.addon.configuration().enabled().get() || !this.addon.configuration().noteEditorBullet().get())
//      return false;
//
//    LabyConnectSession session = Laby.references().labyConnect().getSession();
//    if (session == null || !session.isAuthenticated()) return false;
//
//    Friend friend = session.getFriend(player.getUniqueId());
//    if (friend == null) return false;
//
//    this.friend = friend;
//    return true;
    return false;
  }
}
