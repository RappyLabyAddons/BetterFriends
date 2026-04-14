package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.ui.activities.FriendlistExpirationActivity;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendAddEvent;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;

public class FriendAddListener {

  private final BetterFriendsAddon addon;

  public FriendAddListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendAdd(LabyConnectFriendAddEvent event) {
    if(this.addon.configuration().expiryConfig().sendNotifications().get()) {
      Notification.builder()
          .title(Component.translatable("betterfriends.notifications.friendshipExpiration.set.title"))
          .text(Component.translatable("betterfriends.notifications.friendshipExpiration.set.description"))
          .icon(Icon.head(event.friend().getUniqueId(), true))
          .addButton(NotificationButton.of(
              Component.translatable("betterfriends.notifications.friendshipExpiration.set.button"),
              () -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(
                  new FriendlistExpirationActivity(event.friend())
              )
          ))
          .duration(20000)
          .buildAndPush();
    }

    if (!this.addon.configuration().restartWhenMuted().get()) {
      return;
    }
    if (!this.addon.getVoiceChatHelper().isEnabled()) {
      return;
    }

    this.addon.getVoiceChatHelper().reconnect();
  }
}
