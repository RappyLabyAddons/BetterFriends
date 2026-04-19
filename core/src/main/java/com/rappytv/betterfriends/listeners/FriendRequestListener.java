package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.GroupHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.request.LabyConnectIncomingFriendRequestAddEvent;
import net.labymod.api.event.labymod.labyconnect.session.request.LabyConnectOutgoingFriendRequestRemoveEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.request.IncomingFriendRequest;
import net.labymod.api.notification.Notification;

public class FriendRequestListener {

  private final BetterFriendsAddon addon;

  public FriendRequestListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendRequestReceive(LabyConnectIncomingFriendRequestAddEvent event) {
    if(!this.addon.configuration().friendRequestNotifications().get()) {
      return;
    }
    IncomingFriendRequest request = event.request();
    Component sender = GroupHelper.getColoredName(request.getName(), request.gameUser())
        .hoverEvent(HoverEvent.showText(Component.translatable(
            "betterfriends.general.labynet",
            NamedTextColor.DARK_PURPLE
        )))
        .clickEvent(ClickEvent.openUrl(
            "https://laby.net/@" + request.getName()
        ));

    switch (this.addon.configuration().automaticFriendRequestReaction().get()) {
      case ACCEPT -> {
        request.accept();
        Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    "betterfriends.notifications.friendRequest.automatedAction.accepted",
                    NamedTextColor.GREEN,
                    sender
                ))
        );
        return;
      }
      case DECLINE -> {
        request.decline();
        Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    "betterfriends.notifications.friendRequest.automatedAction.declined",
                    NamedTextColor.RED,
                    sender
                ))
        );
        return;
      }
    }

    Laby.references().chatExecutor().displayClientMessage(
        Component.empty()
            .append(BetterFriendsAddon.getPrefix())
            .append(Component.translatable(
                "betterfriends.notifications.friendRequest.message",
                NamedTextColor.GRAY,
                sender
            ))
            .append(Component.space())
            .append(
                Component.text("✔", NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(
                        Component
                            .translatable("betterfriends.notifications.friendRequest.hover.accept")
                            .color(NamedTextColor.GREEN)
                    ))
                    .clickEvent(ClickEvent.runCommand(
                        "/bf accept " + request.getName()
                    ))
            )
            .append(Component.space())
            .append(Component.text("•", NamedTextColor.DARK_GRAY))
            .append(Component.space())
            .append(
                Component.text("✘", NamedTextColor.RED)
                    .hoverEvent(HoverEvent.showText(
                        Component
                            .translatable("betterfriends.notifications.friendRequest.hover.decline")
                            .color(NamedTextColor.RED)
                    ))
                    .clickEvent(ClickEvent.runCommand(
                        "/bf decline " + request.getName()
                    ))
            )
    );
  }

  @Subscribe
  public void onFriendRequestRemove(LabyConnectOutgoingFriendRequestRemoveEvent event) {
    if (!this.addon.configuration().friendRequestRemovalNotifications().get()) {
      return;
    }
    LabyConnectSession session = event.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }
    if (session.getFriend(event.request().getUniqueId()) != null) {
      return;
    }

    Notification.builder()
        .title(Component.translatable("betterfriends.notifications.friendRequestRemoval.title"))
        .text(Component.translatable(
            "betterfriends.notifications.friendRequestRemoval.description",
            GroupHelper.getColoredName(event.request().getName(), event.request().gameUser())
        ))
        .icon(Icon.head(event.request().getUniqueId(), true))
        .duration(15000)
        .buildAndPush();
  }

}
