package com.rappytv.betterfriends.config.subconfig;

import net.labymod.api.Textures;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class PinIconConfig extends Config {

  @DropdownWidget.DropdownSetting
  private final ConfigProperty<PinIconTexture> texture = new ConfigProperty<>(PinIconTexture.PIN);

  @SwitchSetting
  private final ConfigProperty<Boolean> pinIcon = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> pinBadge = new ConfigProperty<>(true);

  public ConfigProperty<PinIconTexture> texture() {
    return this.texture;
  }

  public ConfigProperty<Boolean> pinIcon() {
    return this.pinIcon;
  }

  public ConfigProperty<Boolean> pinBadge() {
    return this.pinBadge;
  }

  public enum PinIconTexture {
    PIN(Textures.SpriteCommon.PIN, 6F),
    MARKER(Icon.sprite16(ResourceLocation.create(
            "labymod",
            "themes/vanilla/textures/settings/main/laby_1.png"
    ), 5, 2), 8F);

    private final Icon icon;
    private final float badgeSize;

    PinIconTexture(Icon icon, float badgeSize) {
      this.icon = icon;
      this.badgeSize = badgeSize;
    }

    public Icon getIcon() {
      return this.icon;
    }

    public float getBadgeSize() {
      return this.badgeSize;
    }
  }

}
