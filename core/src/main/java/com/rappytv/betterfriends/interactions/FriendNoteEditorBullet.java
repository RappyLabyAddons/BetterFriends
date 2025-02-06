package com.rappytv.betterfriends.interactions;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.util.concurrent.task.Task;

import java.util.concurrent.TimeUnit;

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
    return null;
  }

  @Override
  public void execute(Player player) {
    // TODO: Fix this
    Task.builder(this.friend::openNoteEditor).delay(5, TimeUnit.SECONDS).build().execute();
  }

  @Override
  public boolean isVisible(Player player) {
    if (!this.addon.configuration().noteEditorBullet().get()) return false;

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) return false;

    Friend friend = session.getFriend(player.getUniqueId());
    if (friend == null) return false;

    this.friend = friend;
    return true;
  }
}
