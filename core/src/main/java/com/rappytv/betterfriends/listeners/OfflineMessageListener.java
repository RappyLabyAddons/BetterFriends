package com.rappytv.betterfriends.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.LabyConnectEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageReadEvent;
import net.labymod.api.labyconnect.LabyConnect;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class OfflineMessageListener {

  private final List<UUID> temporaryPined;

  public OfflineMessageListener() {
    this.temporaryPined = new ArrayList<>();
  }

  @Subscribe
  public void onLabyConnect(LabyConnectEvent connectEvent) {
    LabyConnect connect = connectEvent.labyConnect();
    if (!connect.isAuthenticated()) {
      return;
    }

    LabyConnectSession session = connect.getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    for (Friend friend : session.getFriends()) {
      for (ChatMessage message : friend.chat().getMessages()) {
        if (message.isRead() || friend.isPinned()) {
          continue;
        }

        this.temporaryPined.add(friend.getUniqueId());
        friend.pin();
      }
    }
  }

  @Subscribe
  public void onLabyConnectChatMessage(LabyConnectChatMessageEvent chatMessageEvent) {
    LabyConnectSession session = chatMessageEvent.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    UUID uniqueId = chatMessageEvent.message().sender().getUniqueId();
    Friend friend = session.getFriend(uniqueId);
    if (friend == null || friend.isPinned()) {
      return;
    }

    this.temporaryPined.add(uniqueId);
    friend.pin();
  }

  @Subscribe
  public void onLabyConnectChatMessageRead(LabyConnectChatMessageReadEvent readEvent) {
    if (this.temporaryPined.isEmpty()) {
      return;
    }

    UUID uniqueId = readEvent.message().sender().getUniqueId();
    if (!this.temporaryPined.contains(uniqueId)) {
      return;
    }

    LabyConnectSession session = readEvent.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    Friend friend = session.getFriend(uniqueId);
    if (friend == null || !friend.isPinned()) {
      return;
    }

    this.temporaryPined.remove(uniqueId);
    friend.unpin();
  }
}
