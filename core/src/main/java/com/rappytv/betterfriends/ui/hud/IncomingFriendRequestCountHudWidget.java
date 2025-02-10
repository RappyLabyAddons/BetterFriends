package com.rappytv.betterfriends.ui.hud;

import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.labyconnect.LabyConnectSession;

public class IncomingFriendRequestCountHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine line;
  private int requests;

  public IncomingFriendRequestCountHudWidget(HudWidgetCategory category) {
    super("incoming_friend_request_count");

    this.setIcon(SpriteCommon.SMALL_ADD_WITH_SHADOW);
    this.bindCategory(category);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.requests = 0;
    this.line = super.createLine(
        Component.translatable("betterfriends.hudWidget.incoming_friend_request_count.key"),
        this.requests
    );
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.line.updateAndFlush(this.requests);
  }

  @Override
  public boolean isVisibleInGame() {
    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if(session == null || !session.isAuthenticated()) return false;
    if(session.getIncomingRequests().size() != this.requests)
      this.requests = session.getIncomingRequests().size();
    return this.requests > 0;
  }
}
