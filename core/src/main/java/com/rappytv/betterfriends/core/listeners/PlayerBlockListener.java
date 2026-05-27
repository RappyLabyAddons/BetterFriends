package com.rappytv.betterfriends.core.listeners;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.api.blocklist.event.BlockPlayerEvent;
import com.rappytv.betterfriends.api.blocklist.event.UnblockPlayerEvent;
import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.config.subconfig.BlocklistConfig;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.labymod.addons.voicechat.core.VoiceChatAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class PlayerBlockListener {

  private final BlocklistConfig config;

  public PlayerBlockListener(BetterFriendsAddon addon) {
    this.config = addon.configuration().blocklist();
  }

  @Subscribe
  public void onBlock(BlockPlayerEvent event) {
    this.updateConfig();
    BetterFriendsAddon.displayNotifications(
        "betterfriends.blocklist.title",
        Component.translatable(
            "betterfriends.blocklist.blockedPlayer",
            NamedTextColor.GRAY,
            Component.text(event.player().username(), NamedTextColor.AQUA)
        )
    );
    Friend friend = BetterFriendsAddon.references().sessionHelper()
        .getFriend(event.player().uuid());
    if (this.config.unfriendBlockedPlayers().get() && friend != null) {
      friend.remove();
    }
    if (this.config.muteInVoiceChat().get() && BetterFriendsAddon.isVoiceChatEnabled()) {
      this.setMuted(event.player().uuid(), true);
    }
  }

  @Subscribe
  public void onUnblock(UnblockPlayerEvent event) {
    this.updateConfig();
    BetterFriendsAddon.displayNotifications(
        "betterfriends.blocklist.title",
        Component.translatable(
            "betterfriends.blocklist.unblockedPlayer",
            NamedTextColor.GRAY,
            Component.text(event.player().username(), NamedTextColor.AQUA)
        )
    );
    if (this.config.muteInVoiceChat().get() && BetterFriendsAddon.isVoiceChatEnabled()) {
      this.setMuted(event.player().uuid(), false);
    }
  }

  private void setMuted(UUID uuid, boolean value) {
    Map<UUID, Float> volumes = VoiceChatAddon.INSTANCE
        .configuration()
        .playerVolumes()
        .get();

    boolean alreadyMuted = volumes.getOrDefault(uuid, 1f) == 0;
    if (value && !alreadyMuted) {
      volumes.put(uuid, 0f);
    } else {
      if (alreadyMuted) {
        volumes.put(uuid, 1f);
      }
    }
  }

  private void updateConfig() {
    List<BlockedPlayer> blockedPlayers = this.config.blockedPlayers();
    blockedPlayers.clear();
    blockedPlayers.addAll(BetterFriendsAddon.references().blocklistManager().getBlockedPlayers());
  }
}
