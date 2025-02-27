package com.rappytv.betterfriends.ui.hud;

import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.labyconnect.LabyConnectSession;

public class UnreadChatCountWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine line;
  private int unread;

  public UnreadChatCountWidget(HudWidgetCategory category) {
    super("unread_count");

    this.setIcon(SpriteCommon.CHAT_BUBBLE);
    this.bindCategory(category);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.unread = 0;
    this.line = super.createLine(
        Component.translatable("betterfriends.hudWidget.unread_count.key"),
        this.unread
    );
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.line.updateAndFlush(this.unread);
  }

  @Override
  public boolean isVisibleInGame() {
    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if(session == null || !session.isAuthenticated()) return false;
    if(session.getUnreadCount() != this.unread) this.unread = session.getUnreadCount();
    return this.unread > 0;
  }

}
