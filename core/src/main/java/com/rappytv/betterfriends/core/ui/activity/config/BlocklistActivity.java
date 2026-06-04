package com.rappytv.betterfriends.core.ui.activity.config;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.api.blocklist.BlocklistManager;
import com.rappytv.betterfriends.api.blocklist.event.BlockPlayerEvent;
import com.rappytv.betterfriends.api.blocklist.event.UnblockPlayerEvent;
import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.ui.widget.config.BlockedPlayerWidget;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.event.Subscribe;

@AutoActivity
@Link("blocklist.lss")
public class BlocklistActivity extends SimpleActivity {

  private static final Component EMPTY_LABEL = Component.translatable(
      "betterfriends.blocklist.empty");
  private static final Component HINT_LABEL = Component.translatable(
      "betterfriends.blocklist.hint");

  private final Map<UUID, BlockedPlayerWidget> playerWidgets = new HashMap<>();
  private final VerticalListWidget<BlockedPlayerWidget> participantList;
  private final BlocklistManager manager = BetterFriendsAddon.references().blocklistManager();

  public BlocklistActivity() {
    this.loadParticipants();
    this.participantList = new VerticalListWidget<>().addId("player-list");
    this.participantList.setComparator(BlockedPlayerWidget::compare);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    Map<UUID, BlockedPlayerWidget> widgets = this.playerWidgets;
    if (widgets.isEmpty()) {
      VerticalListWidget<ComponentWidget> infoComponents = new VerticalListWidget<>();
      infoComponents.addId("info-components");
      infoComponents.addChild(ComponentWidget.component(EMPTY_LABEL).addId("empty-label"));
      infoComponents.addChild(ComponentWidget.component(HINT_LABEL).addId("hint-label"));
      this.document.addChild(infoComponents);
      return;
    }
    for (BlockedPlayerWidget playerWidget : widgets.values()) {
      this.participantList.addChild(playerWidget);
    }

    this.document.addChild(new ScrollWidget(this.participantList));
  }

  @Override
  public void reload() {
    this.loadParticipants();
    super.reload();
  }

  @Subscribe
  public void onBlock(BlockPlayerEvent event) {
    this.reload();
  }

  @Subscribe
  public void onBlock(UnblockPlayerEvent event) {
    this.reload();
  }

  private void loadParticipants() {
    this.playerWidgets.clear();
    for (BlockedPlayer player : this.manager.getBlockedPlayers()) {
      this.playerWidgets.put(
          player.uuid(),
          new BlockedPlayerWidget(player)
      );
    }
  }
}
