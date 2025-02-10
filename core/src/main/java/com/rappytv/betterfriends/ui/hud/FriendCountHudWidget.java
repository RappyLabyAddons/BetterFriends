package com.rappytv.betterfriends.ui.hud;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.labyconnect.LabyConnectSession;

public class FriendCountHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine line;
  private int friendCount;

  public FriendCountHudWidget(HudWidgetCategory category) {
    super("friend_count");

    this.setIcon(Icon.sprite16(
        ResourceLocation.create("labymod", "themes/vanilla/textures/settings/hud/hud.png"),
        3,
        0
    ));
    this.bindCategory(category);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.friendCount = 0;
    this.line = super.createLine(
        Component.translatable("betterfriends.hudWidget.friend_count.key"),
        this.friendCount
    );
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.line.updateAndFlush(this.friendCount);
  }

  @Override
  public boolean isVisibleInGame() {
    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if(session == null || !session.isAuthenticated()) return false;
    if(session.getFriends().size() != this.friendCount)
      this.friendCount = session.getFriends().size();
    return true;
  }
}
