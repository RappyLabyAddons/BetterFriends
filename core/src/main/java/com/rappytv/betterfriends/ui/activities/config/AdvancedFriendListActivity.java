package com.rappytv.betterfriends.ui.activities.config;

import com.rappytv.betterfriends.ui.widgets.FriendWidget;
import com.rappytv.betterfriends.ui.widgets.FriendlistFriendWidget;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendAddEvent;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendRemoveEvent;
import net.labymod.api.event.labymod.labyconnect.session.login.LabyConnectFriendAddBulkEvent;
import net.labymod.api.event.labymod.user.UserUpdateDataEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.util.ThreadSafe;

@Link("friend_list.lss")
@AutoActivity
public class AdvancedFriendListActivity extends SimpleActivity {

  private final VerticalListWidget<FriendWidget> entries = new VerticalListWidget<>()
      .addId("friends");

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");

    this.entries.setComparator((f1, f2) -> {
      if (!(f1 instanceof FriendWidget friendWidget1 && f2 instanceof FriendWidget friendWidget2)) {
        return 0;
      }
      Friend friend1 = friendWidget1.getFriend();
      Friend friend2 = friendWidget2.getFriend();
      int comparePin = Boolean.compare(friend2.isPinned(), friend1.isPinned());
      if (comparePin != 0) {
        return comparePin;
      } else {
        int compareStatus = Boolean.compare(friend2.isOnline(), friend1.isOnline());
        if (compareStatus != 0) {
          return compareStatus;
        } else {
          ChatMessage message1 = friend1.chat().getLastMessage();
          ChatMessage message2 = friend2.chat().getLastMessage();
          if (message1 != null || message2 != null) {
            int compareLastMessage = Long.compare(
                message2 != null ? message2.getTimestamp() : 0L,
                message1 != null ? message1.getTimestamp() : 0L
            );
            if (compareLastMessage != 0) {
              return compareLastMessage;
            }
          }
          return Long.compare(friend2.getLastOnline(), friend1.getLastOnline());
        }
      }
    });

    ScrollWidget scroll = new ScrollWidget(this.entries);
    container.addFlexibleContent(scroll);
    this.document.addChild(container);

    LabyConnectSession session = Laby.references().labyConnect().getSession();

    if (session == null || !session.isAuthenticated()) {
      Laby.labyAPI().minecraft().executeOnRenderThread(
          () -> container.addContentInitialized(
              ComponentWidget.text("You are not connected to the LabyChat!", NamedTextColor.RED)
                  .addId("error")
          )
      );
      scroll.setVisible(false);
    } else {
      this.initializeWithInfo(session.getFriends());
    }
  }

  private void initializeWithInfo(List<Friend> friends) {
    if (ThreadSafe.isRenderThread()) {
      this.initializeWithInfo(friends, false);
    } else {
      Laby.labyAPI().minecraft().executeOnRenderThread(
          () -> this.initializeWithInfo(friends, true)
      );
    }
  }

  private void initializeWithInfo(List<Friend> friends, boolean initialized) {
    this.entries.getChildren().clear();
    for (Friend friend : friends) {
      if (initialized) {
        this.entries.addChildInitialized(new FriendlistFriendWidget(friend));
      } else {
        this.entries.addChild(new FriendlistFriendWidget(friend));
      }
    }
  }

  @Subscribe
  public void onUserUpdateData(final UserUpdateDataEvent event) {
    if (event.phase() == Phase.POST) {
      this.labyAPI.minecraft().executeOnRenderThread(() ->
          AdvancedFriendListActivity.this.entries.reInitializeChildrenIf(
              FriendWidget.class,
              widget -> widget.getFriend().getUniqueId().equals(
                  event.gameUser().getUniqueId()
              )
          )
      );
    }
  }

  @Subscribe
  public void onLabyConnectFriendAdd(LabyConnectFriendAddEvent event) {
    this.entries.addChildAsync(new FriendlistFriendWidget(event.friend()));
  }

  @Subscribe
  public void onLabyConnectFriendAddBulk(LabyConnectFriendAddBulkEvent event) {
    for (Friend friend : event.getFriends()) {
      this.entries.addChildAsync(new FriendlistFriendWidget(friend));
    }
  }

  @Subscribe
  public void onLabyConnectFriendRemove(LabyConnectFriendRemoveEvent event) {
    this.entries.removeChildIf(
        FriendWidget.class,
        widget -> widget.getFriend().getUniqueId().equals(event.friend().getUniqueId())
    );
  }
}