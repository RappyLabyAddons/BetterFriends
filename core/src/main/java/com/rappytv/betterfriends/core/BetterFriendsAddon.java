package com.rappytv.betterfriends.core;

import com.rappytv.betterfriends.api.generated.ReferenceStorage;
import com.rappytv.betterfriends.core.command.BetterFriendsCommand;
import com.rappytv.betterfriends.core.config.BetterFriendsConfig;
import com.rappytv.betterfriends.core.interactions.FriendNoteEditorBullet;
import com.rappytv.betterfriends.core.interactions.FriendTogglePinBullet;
import com.rappytv.betterfriends.core.listeners.ChatReceiveListener;
import com.rappytv.betterfriends.core.listeners.ConfigVersionListener;
import com.rappytv.betterfriends.core.listeners.FriendListener;
import com.rappytv.betterfriends.core.listeners.FriendRequestListener;
import com.rappytv.betterfriends.core.listeners.LabyChatReceiveListener;
import com.rappytv.betterfriends.core.listeners.TemporaryPinListener;
import com.rappytv.betterfriends.core.ui.badge.FriendPinBadge;
import com.rappytv.betterfriends.core.ui.hud.FriendCountHudWidget;
import com.rappytv.betterfriends.core.ui.hud.IncomingFriendRequestCountHudWidget;
import com.rappytv.betterfriends.core.ui.hud.OnlineFriendCountHudWidget;
import com.rappytv.betterfriends.core.ui.hud.UnreadChatCountWidget;
import com.rappytv.betterfriends.core.ui.tags.FriendNoteNameTag;
import com.rappytv.betterfriends.core.ui.tags.FriendPinIconTag;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class BetterFriendsAddon extends LabyAddon<BetterFriendsConfig> {

  private static BetterFriendsAddon INSTANCE;

  @Override
  protected void preConfigurationLoad() {
    this.registerListener(new ConfigVersionListener());
    Laby.references().revisionRegistry().register(new SimpleRevision(
        "betterfriends",
        new SemanticVersion(1, 0, 1),
        "2025-12-10"
    ));
    Laby.references().revisionRegistry().register(new SimpleRevision(
        "betterfriends",
        new SemanticVersion(1, 1, 0),
        "2026-04-30"
    ));
  }

  @Override
  protected void enable() {
    INSTANCE = this;
    this.registerSettingCategory();

    this.registerCommand(new BetterFriendsCommand());

    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new FriendListener(this));
    this.registerListener(new FriendRequestListener(this));
    this.registerListener(new LabyChatReceiveListener(this));
    this.registerListener(new TemporaryPinListener(this));

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
        new FriendPinIconTag()
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

  public static ReferenceStorage references() {
    return INSTANCE.referenceStorageAccessor();
  }

  public static Component getPrefix() {
    return INSTANCE.configuration().prefixCustomizationConfig().buildPrefix();
  }
}
