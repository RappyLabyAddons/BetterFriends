package com.rappytv.betterfriends.core.ui.widget.config;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.api.blocklist.BlocklistManager;
import com.rappytv.betterfriends.core.BetterFriendsAddon;
import java.util.UUID;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@AutoWidget
public class BlockedPlayerWidget extends HorizontalListWidget {

  private final BlockedPlayer player;
  private final BlocklistManager manager = BetterFriendsAddon.references().blocklistManager();

  public BlockedPlayerWidget(BlockedPlayer player) {
    this.player = player;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    IconWidget iconWidget = new IconWidget(this.getIconWidget(this.player.uuid()))
        .addId("avatar");
    this.addEntry(iconWidget);

    ComponentWidget nameWidget = ComponentWidget.text(this.player.username())
        .addId("username");
    this.addEntry(nameWidget);

    ButtonWidget removeButtonWidget = ButtonWidget.text("✘", () ->
        this.manager.unblock(this.player.uuid())
    ).addId("remove-button");
    this.addEntry(removeButtonWidget);
  }

  private Icon getIconWidget(UUID uuid) {
    return Icon.head(uuid);
  }

  @Override
  public int getSortingValue() {
    return -1;
  }

  public static int compare(Widget a, Widget b) {
    if (!(a instanceof BlockedPlayerWidget p1)
        || !(b instanceof BlockedPlayerWidget p2)) {
      return 0;
    }

    return p1.player.username().compareToIgnoreCase(p2.player.username());
  }

}
