package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.loader.MinecraftVersions;

public class ChatReceiveListener {

  private final BetterFriendsAddon addon;

  public ChatReceiveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe(125)
  public void onChatReceive(ChatReceiveEvent receiveEvent) {
    if (receiveEvent.isCancelled() || this.addon.configuration().friendPrefix().get().isBlank()) {
      return;
    }

    Component message = receiveEvent.message();

    if (MinecraftVersions.V1_12_2.orOlder()) {
      message = message.copy().colorIfAbsent(NamedTextColor.WHITE);
    }

    UUID uuid = receiveEvent.chatMessage().getSenderUniqueId();
    if (uuid == null) {
      return;
    }

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    Friend friend = session.getFriend(uuid);
    if (friend == null) {
      return;
    }

    receiveEvent.setMessage(
        this.addon.getSerializer()
            .deserialize(this.addon.configuration().friendPrefix().get())
            .append(Component.space())
            .append(message)
    );
  }

}
