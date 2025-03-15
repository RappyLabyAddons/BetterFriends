package com.rappytv.betterfriends.ui.widgets;

import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.labyconnect.protocol.model.UserStatus;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

@Link("friend.lss")
@AutoWidget
public abstract class FriendWidget extends HorizontalListWidget {

  protected final Friend friend;

  public FriendWidget(Friend friend) {
    this.friend = friend;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    UserStatus userStatus = this.friend.userStatus();
    IconWidget indicatorWidget = new IconWidget(SpriteCommon.STATUS_INDICATOR);
    indicatorWidget.addId("indicator");
    indicatorWidget.color().set(userStatus.getColor().getRGB());
    Component statusComponent = Component.translatable(
        userStatus.getLocalTranslationKey(),
        userStatus.textColor()
    );

    if (userStatus == UserStatus.OFFLINE) {
      statusComponent = Component.translatable(
          "betterfriends.notifications.statusUpdate.offline",
          NamedTextColor.DARK_GRAY
      );
    }
    indicatorWidget.setHoverComponent(statusComponent);

    IconWidget headWidget = new IconWidget(Icon.head(this.friend.getUniqueId()))
        .addId("player-head");
    headWidget.setHoverComponent(Component.translatable("Open on laby.net"));
    headWidget.setPressable(() ->
        Laby.references().chatExecutor().openUrl("https://laby.net/@" + this.friend.getUniqueId())
    );

    ComponentWidget usernameWidget = ComponentWidget
        .component(Component.text(
            this.friend.getName(),
            TextColor.color(this.friend.gameUser().visibleGroup().getColor().getRGB())
        ))
        .addId("username");

    usernameWidget.setHoverComponent(Component.translatable("Click to copy UUID"));
    usernameWidget.setPressable(() ->
        Laby.references().chatExecutor().copyToClipboard(this.friend.getUniqueId().toString())
    );

    this.addEntry(indicatorWidget);
    this.addEntry(headWidget);
    this.addEntry(usernameWidget);

    if (this.friend.isPinned()) {
      IconWidget pinIconWidget = new IconWidget(SpriteCommon.PIN)
          .addId("pin-icon");
      this.addEntry(pinIconWidget);
    }

    List<ButtonWidget> buttons = this.getButtons();

    if(!buttons.isEmpty()) {
      HorizontalListWidget buttonWrapper = new HorizontalListWidget().addId("buttons");

      for(ButtonWidget button : buttons) {
        buttonWrapper.addEntry(button);
      }

      this.addEntry(buttonWrapper);
    }
  }

  public Friend getFriend() {
    return this.friend;
  }

  // Without this the comparator just doesn't work for whatever reason
  @Override
  public int getSortingValue() {
    return 1;
  }

  public abstract List<ButtonWidget> getButtons();
}
