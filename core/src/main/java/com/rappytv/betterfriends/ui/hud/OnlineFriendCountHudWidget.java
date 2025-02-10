package com.rappytv.betterfriends.ui.hud;

import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.labyconnect.LabyConnectSession;

public class OnlineFriendCountHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine line;
  private int friendCount;

  public OnlineFriendCountHudWidget(HudWidgetCategory category) {
    super("online_friend_count");

    this.setIcon(SpriteCommon.MULTIPLAYER);
    this.bindCategory(category);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.friendCount = 0;
    this.line = super.createLine(
        Component.translatable("betterfriends.hudWidget.online_friend_count.key"),
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
    if(session.getOnlineFriendCount() != this.friendCount)
      this.friendCount = session.getOnlineFriendCount();
    return true;
  }
}
