package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import com.rappytv.betterfriends.config.subconfig.PinIconConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

public class BetterFriendsConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @ColorPickerSetting
  private final ConfigProperty<Color> prefixColor = new ConfigProperty<>(Color.ofRGB(255, 102, 0));
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();
  private final PinIconConfig pinIconConfig = new PinIconConfig();
  @TextFieldSetting
  private final ConfigProperty<String> friendPrefix = new ConfigProperty<>("&aⒻ");

  @SettingSection("notifications")
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRequestNotifications = new ConfigProperty<>(true);
  @SettingRequires("friendRequestNotifications")
  @DropdownSetting
  private final ConfigProperty<FriendRequestReaction> automaticFriendRequestReaction = new ConfigProperty<>(
      FriendRequestReaction.NONE);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendServerSwitchNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendStatusUpdateNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRemovalNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> labyChatMessageNotifications = new ConfigProperty<>(true);
  @SettingRequires("labyChatMessageNotifications")
  @SwitchSetting
  private final ConfigProperty<Boolean> ownLabyChatMessages = new ConfigProperty<>(true);

  @SettingSection("interactions")
  @SwitchSetting
  private final ConfigProperty<Boolean> noteEditorBullet = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> togglePinBullet = new ConfigProperty<>(true);

  @SettingSection("voicechat")
  @SwitchSetting
  private final ConfigProperty<Boolean> restartWhenMuted = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public ConfigProperty<Color> prefixColor() {
    return this.prefixColor;
  }
  public FriendNoteTagConfig friendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }
  public PinIconConfig pinIconConfig() {
    return this.pinIconConfig;
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
  public ConfigProperty<Boolean> labyChatMessageNotifications() {
    return this.labyChatMessageNotifications;
  }
  public ConfigProperty<Boolean> ownLabyChatMessages() {
    return this.ownLabyChatMessages;
  }

  public ConfigProperty<Boolean> noteEditorBullet() {
    return this.noteEditorBullet;
  }
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
