package com.rappytv.betterfriends.ui.widget.config;

import com.rappytv.betterfriends.config.subconfig.PrefixCustomizationConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;

@Link("prefix-preview.lss")
@AutoWidget
@SettingWidget
public class PrefixPreviewWidget extends HorizontalListWidget {

  private final PrefixCustomizationConfig config;

  private ComponentWidget prefixWidget;

  public PrefixPreviewWidget(PrefixCustomizationConfig config) {
    this.config = config;
    config.enableManualPrefix().addChangeListener(this::update);
    config.manualPrefix().addChangeListener(this::update);
    config.prefix().addChangeListener(this::update);
    config.prefixColor().addChangeListener(this::update);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    this.addEntry(this.prefixWidget = ComponentWidget.component(this.config.buildPrefix()));
  }

  private void update() {
    this.prefixWidget.setComponent(this.config.buildPrefix());
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  @SettingElement(extended = true)
  public @interface PrefixPreviewSetting {

  }

  @SettingFactory
  public static class Factory implements WidgetFactory<PrefixPreviewSetting, PrefixPreviewWidget> {

    @Override
    public PrefixPreviewWidget[] create(Setting setting, PrefixPreviewSetting annotation,
        SettingAccessor accessor) {
      if (!(accessor.config() instanceof PrefixCustomizationConfig config)) {
        return new PrefixPreviewWidget[0];
      }

      return new PrefixPreviewWidget[]{new PrefixPreviewWidget(config)};
    }

    @Override
    public Class<?>[] types() {
      return new Class[0];
    }
  }
}
