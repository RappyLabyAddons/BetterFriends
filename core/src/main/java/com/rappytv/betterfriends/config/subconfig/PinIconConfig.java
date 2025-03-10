package com.rappytv.betterfriends.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class PinIconConfig extends Config {

  @SpriteSlot(x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> pinIcon = new ConfigProperty<>(true);
  @SpriteSlot(y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> pinBadge = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> pinIcon() {
    return this.pinIcon;
  }
  public ConfigProperty<Boolean> pinBadge() {
    return this.pinBadge;
  }

}
