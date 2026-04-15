package com.rappytv.betterfriends.config.subconfig;

import com.rappytv.betterfriends.ui.widget.config.PrefixPreviewWidget.PrefixPreviewSetting;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.CustomTranslation;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

public class PrefixCustomizationConfig extends Config {

  private final static Component CONTENT_PLACEHOLDER = Component.translatable(
      "betterfriends.settings.prefixCustomizationConfig.contentPlaceholder"
  );

  @PrefixPreviewSetting
  private final ConfigProperty<Boolean> preview = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> enableManualPrefix = new ConfigProperty<>(false);

  @SettingSection(value = "options", center = true)
  @CustomTranslation("betterfriends.settings.prefixCustomizationConfig.prefix")
  @TextFieldSetting
  private final ConfigProperty<String> manualPrefix = new ConfigProperty<>("&6&lBF&8 » ")
      .visibilitySupplier(this.enableManualPrefix::get);

  @TextFieldSetting
  private final ConfigProperty<String> prefix = new ConfigProperty<>("BF")
      .visibilitySupplier(() -> !this.enableManualPrefix.get());

  @SettingRequires(value = "prefix", required = "", invert = true)
  @ColorPickerSetting
  private final ConfigProperty<Color> prefixColor = new ConfigProperty<>(Color.ofRGB(255, 102, 0))
      .visibilitySupplier(() -> !this.enableManualPrefix.get());

  public ConfigProperty<Boolean> enableManualPrefix() {
    return this.enableManualPrefix;
  }

  public ConfigProperty<String> manualPrefix() {
    return this.manualPrefix;
  }

  public ConfigProperty<String> prefix() {
    return this.prefix;
  }

  public ConfigProperty<Color> prefixColor() {
    return this.prefixColor;
  }

  public Component buildPrefix() {
    if (this.enableManualPrefix.get()) {
      return LegacyComponentSerializer.legacyAmpersand()
          .deserialize(this.manualPrefix.get())
          .append(CONTENT_PLACEHOLDER);
    }
    Component component = Component.empty();

    String prefix = this.prefix.get();
    if (!prefix.isEmpty()) {
      component
          .append(
              Component.text(
                  prefix,
                  TextColor.color(this.prefixColor.get().get())
              ).decorate(TextDecoration.BOLD)
          )
          .append(Component.space())
          .append(Component.text("»", NamedTextColor.DARK_GRAY))
          .append(Component.space());
    }
    component.append(CONTENT_PLACEHOLDER);
    return component;
  }
}
