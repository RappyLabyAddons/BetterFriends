package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;

import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.User;
import net.labymod.api.labyconnect.protocol.model.chat.TextChatMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LabyChatReceiveListener {

  private static final Map<UUID, TextChatMessage> messages = new HashMap<>();
  private final BetterFriendsAddon addon;

  public LabyChatReceiveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatReceive(LabyConnectChatMessageEvent event) {
    if(!this.addon.configuration().labyChatMessageNotifications().get()) {
      return;
    }
    TextChatMessage message = (TextChatMessage) event.message();
    LabyConnectSession session = event.labyConnect().getSession();
    if(session == null) {
      return;
    }
    User self = session.self();
    User sender = message.sender();
    User receiver = self.getName().equals(sender.getName())
        ? event.chat().getParticipants().getFirst()
        : self;

    UUID uuid = UUID.randomUUID();
    messages.put(uuid, message);
    if(sender.getName().equals(self.getName())
        && !this.addon.configuration().ownLabyChatMessages().get()) {
      return;
    }

    Component component = Component.empty()
        .append(BetterFriendsAddon.prefix)
        .append(NameHelper.getColoredName(sender.getName(), sender.gameUser()))
        .append(Component.text(" → ", NamedTextColor.DARK_GRAY))
        .append(NameHelper.getColoredName(receiver.getName(), receiver.gameUser()))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
        .append(Component.text(message.getMessage(), NamedTextColor.WHITE));

    int attachments = message.getAttachments().size();
    if(attachments > 0) {
      component
          .append(Component.space())
          .append(Component.translatable(
              "betterfriends.notifications.chats.attachments",
              NamedTextColor.AQUA,
              Component.text(attachments)
          ));
    }

    if(receiver.getName().equals(Laby.labyAPI().getName())) {
      component
          .append(Component
              .text(" ✔", NamedTextColor.GREEN)
              .hoverEvent(HoverEvent.showText(Component.translatable(
                  "betterfriends.notifications.chats.read",
                  NamedTextColor.GREEN
              )))
              .clickEvent(ClickEvent.runCommand("/bf read " + uuid))
          )
          .append(Component
              .text(" ➥", NamedTextColor.BLUE)
              .hoverEvent(HoverEvent.showText(Component.translatable(
                  "betterfriends.notifications.chats.reply",
                  NamedTextColor.BLUE,
                  Component.text(sender.getName())
              )))
              .clickEvent(ClickEvent.suggestCommand(
                  "/bf msg " + sender.getName() + " "
              ))
          );
    }

    this.addon.displayMessage(component);
  }

  public static TextChatMessage getMessage(UUID uuid) {
    return messages.get(uuid);
  }
}
