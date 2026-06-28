package com.rappytv.betterfriends.core.listeners;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.loader.MinecraftVersions;

public class ChatReceiveListener {

  private final BetterFriendsAddon addon;

  public ChatReceiveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe(125)
  public void onChatReceive(ChatReceiveEvent event) {
    if (event.isCancelled()) {
      return;
    }

    if (this.checkIfBlocked(event.chatMessage().getSenderUniqueId())) {
      event.setCancelled(true);
      return;
    }
    this.addFriendPrefix(event);
  }

  private boolean checkIfBlocked(UUID sender) {
    boolean enabled = this.addon.configuration().blocklist().hideChatMessages().get();
    if (sender == null || !enabled) {
      return false;
    }

    return BetterFriendsAddon.references().blocklistManager().isBlocked(sender);
  }

  private void addFriendPrefix(ChatReceiveEvent event) {
    if (!this.addon.configuration().friendPrefix().get().isBlank()) {
      return;
    }
    Component message = event.message();

    if (MinecraftVersions.V1_12_2.orOlder()) {
      message = message.copy().colorIfAbsent(NamedTextColor.WHITE);
    }

    UUID sender = event.chatMessage().getSenderUniqueId();
    if (sender == null) {
      return;
    }

    Friend friend = BetterFriendsAddon.references().sessionHelper().getFriend(sender);
    if (friend == null) {
      return;
    }

    event.setMessage(
        Component.empty()
            .append(LegacyComponentSerializer.legacyAmpersand().deserialize(
                this.addon.configuration().friendPrefix().get()
            ))
            .append(Component.space())
            .append(message)
    );
  }

}
