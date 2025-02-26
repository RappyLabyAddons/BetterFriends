package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.request.LabyConnectIncomingFriendRequestAddEvent;
import net.labymod.api.labyconnect.protocol.model.request.IncomingFriendRequest;

public class FriendRequestReceiveListener {

  private final BetterFriendsAddon addon;

  public FriendRequestReceiveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendRequestReceive(LabyConnectIncomingFriendRequestAddEvent event) {
    if(!this.addon.configuration().friendRequestNotifications().get()) {
      return;
    }
    IncomingFriendRequest request = event.request();
    Component sender = NameHelper.getColoredName(request.getName(), request.gameUser())
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

}
