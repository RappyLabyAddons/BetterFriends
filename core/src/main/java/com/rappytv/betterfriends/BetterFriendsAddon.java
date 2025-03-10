package com.rappytv.betterfriends;

import com.rappytv.betterfriends.command.BetterFriendsCommand;
import com.rappytv.betterfriends.config.BetterFriendsConfig;
import com.rappytv.betterfriends.interactions.FriendNoteEditorBullet;
import com.rappytv.betterfriends.interactions.FriendTogglePinBullet;
import com.rappytv.betterfriends.listeners.ChatReceiveListener;
import com.rappytv.betterfriends.listeners.FriendAddListener;
import com.rappytv.betterfriends.listeners.FriendRemoveListener;
import com.rappytv.betterfriends.listeners.FriendRequestReceiveListener;
import com.rappytv.betterfriends.listeners.FriendRequestRemoveListener;
import com.rappytv.betterfriends.listeners.FriendServerStateListener;
import com.rappytv.betterfriends.listeners.FriendStatusUpdateListener;
import com.rappytv.betterfriends.listeners.LabyChatReceiveListener;
import com.rappytv.betterfriends.ui.badge.FriendPinBadge;
import com.rappytv.betterfriends.ui.hud.FriendCountHudWidget;
import com.rappytv.betterfriends.ui.hud.IncomingFriendRequestCountHudWidget;
import com.rappytv.betterfriends.ui.hud.OnlineFriendCountHudWidget;
import com.rappytv.betterfriends.ui.hud.UnreadChatCountWidget;
import com.rappytv.betterfriends.ui.tags.FriendNoteNameTag;
import com.rappytv.betterfriends.ui.tags.FriendPinIconTag;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class BetterFriendsAddon extends LabyAddon<BetterFriendsConfig> {

  private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
  private static BetterFriendsAddon instance;

  @Override
  protected void enable() {
    instance = this;
    this.registerSettingCategory();

    this.registerCommand(new BetterFriendsCommand());

    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new FriendAddListener(this));
    this.registerListener(new FriendRemoveListener(this));
    this.registerListener(new FriendRequestReceiveListener(this));
    this.registerListener(new FriendRequestRemoveListener(this));
    this.registerListener(new FriendServerStateListener(this));
    this.registerListener(new FriendStatusUpdateListener(this));
    this.registerListener(new LabyChatReceiveListener(this));

    this.labyAPI().interactionMenuRegistry().register(new FriendNoteEditorBullet(this));
    this.labyAPI().interactionMenuRegistry().register(new FriendTogglePinBullet(this));

    HudWidgetCategory category = new HudWidgetCategory(this, "widgets");
    this.labyAPI().hudWidgetRegistry().categoryRegistry().register(category);
    this.labyAPI().hudWidgetRegistry().register(new FriendCountHudWidget(category));
    this.labyAPI().hudWidgetRegistry().register(new IncomingFriendRequestCountHudWidget(category));
    this.labyAPI().hudWidgetRegistry().register(new OnlineFriendCountHudWidget(category));
    this.labyAPI().hudWidgetRegistry().register(new UnreadChatCountWidget(category));

    for (PositionType position : PositionType.values()) {
      this.labyAPI().tagRegistry().registerBefore(
          "labymod_role",
          "betterfriends_friend_note",
          position,
          new FriendNoteNameTag(this, position)
      );
    }
    this.labyAPI().tagRegistry().registerBefore(
        "VoiceTag",
        "betterfriends_pin_icon",
        PositionType.RIGHT_TO_NAME,
        new FriendPinIconTag(this)
    );
    Laby.references().badgeRegistry().registerBefore(
        "VoiceBadge",
        "betterfriends_pin_badge",
        net.labymod.api.client.entity.player.badge.PositionType.RIGHT_TO_NAME,
        new FriendPinBadge(this)
    );
  }

  @Override
  protected Class<? extends BetterFriendsConfig> configurationClass() {
    return BetterFriendsConfig.class;
  }

  public static TextComponent getPrefix() {
    return Component.empty()
        .append(Component.text(
            "BF",
            TextColor.color(instance.configuration().prefixColor().get().get())
        ).decorate(TextDecoration.BOLD))
        .append(Component.space())
        .append(Component.text("»", NamedTextColor.DARK_GRAY))
        .append(Component.space());
  }

  public LegacyComponentSerializer getSerializer() {
    return this.serializer;
  }
}
