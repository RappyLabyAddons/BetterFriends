package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import com.rappytv.betterfriends.config.subconfig.PinIconConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@SpriteTexture("settings.png")
public class BetterFriendsConfig extends AddonConfig {

  @SettingSection(value = "general", center = true)
  @SpriteSlot
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SpriteSlot(x = 1)
  @ColorPickerSetting
  private final ConfigProperty<Color> prefixColor = new ConfigProperty<>(Color.ofRGB(255, 102, 0));
  @SpriteSlot(size = 8, x = 4)
  private final PinIconConfig pinIconConfig = new PinIconConfig();
  @SpriteSlot(x = 3)
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();
  @SpriteSlot(x = 4)
  @TextFieldSetting
  private final ConfigProperty<String> friendPrefix = new ConfigProperty<>("&aⒻ");

  @SettingSection(value = "notifications", center = true)
  @SpriteSlot(x = 5)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRequestNotifications = new ConfigProperty<>(true);
  @SettingRequires("friendRequestNotifications")
  @SpriteSlot(x = 6)
  @DropdownSetting
  private final ConfigProperty<FriendRequestReaction> automaticFriendRequestReaction = new ConfigProperty<>(
      FriendRequestReaction.NONE);
  @SpriteSlot(x = 7)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendServerSwitchNotifications = new ConfigProperty<>(true);
  @SpriteSlot(y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendStatusUpdateNotifications = new ConfigProperty<>(true);
  @SpriteSlot(x = 1, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRemovalNotifications = new ConfigProperty<>(true);
  @SpriteSlot(x = 2, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRequestRemovalNotifications = new ConfigProperty<>(true);
  @SpriteSlot(x = 3, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> labyChatMessageNotifications = new ConfigProperty<>(true);
  @SettingRequires("labyChatMessageNotifications")
  @SpriteSlot(x = 4, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> ownLabyChatMessages = new ConfigProperty<>(true);

  @SettingSection(value = "interactions", center = true)
//  @SpriteSlot(x = 5, y = 1)
//  @SwitchSetting
//  private final ConfigProperty<Boolean> noteEditorBullet = new ConfigProperty<>(true);
  @SpriteSlot(size = 8, x = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> togglePinBullet = new ConfigProperty<>(true);

  @SettingSection(value = "voicechat", center = true)
  @SpriteSlot(x = 6, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> restartWhenMuted = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public ConfigProperty<Color> prefixColor() {
    return this.prefixColor;
  }
  public PinIconConfig pinIconConfig() {
    return this.pinIconConfig;
  }
  public FriendNoteTagConfig friendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }
  public ConfigProperty<String> friendPrefix() {
    return this.friendPrefix;
  }

  public ConfigProperty<Boolean> friendRequestNotifications() {
    return this.friendRequestNotifications;
  }
  public ConfigProperty<FriendRequestReaction> automaticFriendRequestReaction() {
    return this.automaticFriendRequestReaction;
  }
  public ConfigProperty<Boolean> friendServerSwitchNotifications() {
    return this.friendServerSwitchNotifications;
  }
  public ConfigProperty<Boolean> friendStatusUpdateNotifications() {
    return this.friendStatusUpdateNotifications;
  }
  public ConfigProperty<Boolean> friendRemovalNotifications() {
    return this.friendRemovalNotifications;
  }
  public ConfigProperty<Boolean> friendRequestRemovalNotifications() {
    return this.friendRequestRemovalNotifications;
  }
  public ConfigProperty<Boolean> labyChatMessageNotifications() {
    return this.labyChatMessageNotifications;
  }
  public ConfigProperty<Boolean> ownLabyChatMessages() {
    return this.ownLabyChatMessages;
  }

//  public ConfigProperty<Boolean> noteEditorBullet() {
//    return this.noteEditorBullet;
//  }
  public ConfigProperty<Boolean> togglePinBullet() {
    return this.togglePinBullet;
  }
  public ConfigProperty<Boolean> restartWhenMuted() {
    return this.restartWhenMuted;
  }

  public enum FriendRequestReaction {
    NONE, ACCEPT, DECLINE
  }
}
