package com.rappytv.betterfriends.command;

import com.rappytv.betterfriends.BetterFriendsAddon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.rappytv.betterfriends.listeners.LabyChatReceiveListener;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.network.server.ServerInfo;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.User;
import net.labymod.api.labyconnect.protocol.model.chat.Chat;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.chat.TextChatMessage;
import net.labymod.api.labyconnect.protocol.model.request.IncomingFriendRequest;

public class BetterFriendsCommand extends Command {

  public BetterFriendsCommand() {
    super("betterfriends", "bf");

    this.translationKey("betterfriends.command");
    this.withSubCommand(new AcceptFriendRequestSubcommand());
    this.withSubCommand(new DeclineFriendRequestSubcommand());
    this.withSubCommand(new JoinServerSubcommand());
    this.withSubCommand(new MessageSubcommand());
    this.withSubCommand(new ReadSubcommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    List<String> subCommands = new ArrayList<>();
    for (SubCommand subCommand : this.getSubCommands()) {
      subCommands.add(subCommand.getPrefix());
    }

    this.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.getPrefix())
            .append(Component.translatable(
                this.getTranslationKey("usage"),
                NamedTextColor.RED,
                Component.text(
                    "/" + prefix + " <" + String.join("/", subCommands) + ">",
                    NamedTextColor.AQUA
                )
            ))
    );
    return true;
  }

  public static class AcceptFriendRequestSubcommand extends SubCommand {

    protected AcceptFriendRequestSubcommand() {
      super("accept");

      this.translationKey("betterfriends.command.requests");
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
      List<IncomingFriendRequest> requests = session.getIncomingRequests();

      for(IncomingFriendRequest request : requests) {
        if(!request.getName().equalsIgnoreCase(arguments[0])) continue;
        request.accept();
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("success.accept"),
                    NamedTextColor.GRAY,
                    NameHelper.getColoredName(request.getName(), request.gameUser())
                ))
        );
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

  public static class DeclineFriendRequestSubcommand extends SubCommand {

    protected DeclineFriendRequestSubcommand() {
      super("decline");

      this.translationKey("betterfriends.command.requests");
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
      List<IncomingFriendRequest> requests = session.getIncomingRequests();

      for(IncomingFriendRequest request : requests) {
        if(!request.getName().equalsIgnoreCase(arguments[0])) continue;
        request.decline();
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("success.decline"),
                    NamedTextColor.GRAY,
                    NameHelper.getColoredName(request.getName(), request.gameUser())
                ))
        );
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

  public static class JoinServerSubcommand extends SubCommand {

    protected JoinServerSubcommand() {
      super("join");

      this.translationKey("betterfriends.command.join");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if (arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("enterAddress"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }

      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(this.getTranslationKey("connecting")))
      );
      Laby.references()
          .serverController()
          .joinServer(ServerInfo.builder().address(arguments[0]).build());
      return true;
    }
  }

  public static class MessageSubcommand extends SubCommand {

    protected MessageSubcommand() {
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

  public static class ReadSubcommand extends SubCommand {

    protected ReadSubcommand() {
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
}
