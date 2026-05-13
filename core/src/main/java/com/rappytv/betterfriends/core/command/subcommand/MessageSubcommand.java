package com.rappytv.betterfriends.core.command.subcommand;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.User;
import net.labymod.api.labyconnect.protocol.model.chat.Chat;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import java.util.Arrays;
import java.util.List;

public class MessageSubcommand extends SubCommand {

    public MessageSubcommand() {
      super("message", "msg");

      this.translationKey("betterfriends.command.message");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if(arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("enterName"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      LabyConnectSession session = Laby.references().labyConnect().getSession();
      if(session == null) {
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
      List<Chat> chats = session.getChats();

      for(Chat chat : chats) {
        boolean containsUser = false;
        for(User user : chat.getParticipants()) {
          if(user.getName().equalsIgnoreCase(arguments[0])) containsUser = true;
        }
        if(!containsUser) continue;
        if(arguments.length < 2) {
          this.displayMessage(
              Component.empty()
                  .append(BetterFriendsAddon.getPrefix())
                  .append(Component.translatable(
                      this.getTranslationKey("enterText"),
                      NamedTextColor.RED
                  ))
          );
          return true;
        }
        String message = String.join(
            " ",
            Arrays.copyOfRange(arguments, 1, arguments.length)
        );

        chat.sendMessage(message);
        for(ChatMessage msg : chat.getMessages()) {
          if(!msg.isRead())
            msg.markAsRead();
        }
        return true;
      }
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  this.getTranslationKey("notFound"),
                  NamedTextColor.RED
              ))
      );
      return true;
    }
  }