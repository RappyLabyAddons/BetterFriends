package com.rappytv.betterfriends.core.command.subcommand;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.listeners.LabyChatReceiveListener;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.labyconnect.protocol.model.chat.TextChatMessage;
import java.util.UUID;

public class ReadSubcommand extends SubCommand {

    public ReadSubcommand() {
      super("read");

      this.translationKey("betterfriends.command.read");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if(Laby.references().labyConnect().getSession() == null) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    "betterfriends.errors.notConnected",
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      if(arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      UUID uuid;

      try {
        uuid = UUID.fromString(arguments[0]);
      } catch (IllegalArgumentException e) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }

      TextChatMessage message = LabyChatReceiveListener.getMessage(uuid);

      if(message == null) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      if(message.isRead()) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("alreadyRead"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      message.markAsRead();
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(this.getTranslationKey("success")))
      );
      return true;
    }
  }