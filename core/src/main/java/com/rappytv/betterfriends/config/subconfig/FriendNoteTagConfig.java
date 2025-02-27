package com.rappytv.betterfriends.config.subconfig;

import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class FriendNoteTagConfig extends Config {

  @ShowSettingInParent
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SliderSetting(min = 5, max = 10)
  private final ConfigProperty<Integer> size = new ConfigProperty<>(10);
  @SwitchSetting
  private final ConfigProperty<Boolean> hideBackground = new ConfigProperty<>(false);
  @DropdownSetting
  private final ConfigProperty<PositionType> position = new ConfigProperty<>(
      PositionType.BELOW_NAME);
  @TextFieldSetting
  private final ConfigProperty<String> defaultTag = new ConfigProperty<>("");

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public ConfigProperty<Integer> size() {
    return this.size;
  }
  public ConfigProperty<Boolean> hideBackground() {
    return this.hideBackground;
  }
  public ConfigProperty<PositionType> position() {
    return this.position;
  }
  public ConfigProperty<String> defaultTag() {
    return this.defaultTag;
  }
}
