package com.rappytv.betterfriends;

import com.rappytv.betterfriends.command.BetterFriendsCommand;
import com.rappytv.betterfriends.config.BetterFriendsConfig;
import com.rappytv.betterfriends.listeners.ChatReceiveListener;
import com.rappytv.betterfriends.listeners.FriendRemoveListener;
import com.rappytv.betterfriends.listeners.FriendServerStateListener;
import com.rappytv.betterfriends.listeners.FriendStatusUpdateListener;
import com.rappytv.betterfriends.nametags.FriendNoteNameTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class BetterFriendsAddon extends LabyAddon<BetterFriendsConfig> {

  private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
  public static final TextComponent prefix = Component.empty()
      .append(Component.text(
          "BF",
          TextColor.color(255, 102, 0)
      ).decorate(TextDecoration.BOLD))
      .append(Component.space())
      .append(Component.text("»", NamedTextColor.DARK_GRAY))
      .append(Component.space());

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerCommand(new BetterFriendsCommand());

    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new FriendRemoveListener(this));
    this.registerListener(new FriendServerStateListener(this));
    this.registerListener(new FriendStatusUpdateListener(this));

    for (PositionType position : PositionType.values()) {
      this.labyAPI().tagRegistry().register(
          "betterfriends_friendnote",
          position,
          new FriendNoteNameTag(this, position)
      );
    }
  }

  @Override
  protected Class<? extends BetterFriendsConfig> configurationClass() {
    return BetterFriendsConfig.class;
  }

  public LegacyComponentSerializer getSerializer() {
    return this.serializer;
  }
}
