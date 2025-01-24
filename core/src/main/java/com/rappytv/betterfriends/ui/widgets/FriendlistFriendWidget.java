package com.rappytv.betterfriends.ui.widgets;

import net.labymod.api.Laby;
import net.labymod.api.Textures;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

@AutoWidget
public class FriendlistFriendWidget extends FriendWidget {

  private boolean skipConfirmation = false;
  private ButtonWidget pinButton = null;

  public FriendlistFriendWidget(Friend friend) {
    super(friend);
    Laby.labyAPI().eventBus().registerListener(this);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    HorizontalListWidget buttons = new HorizontalListWidget().addId("buttons");

    this.pinButton = ButtonWidget.icon(Textures.SpriteCommon.PIN, () -> {
      if (this.friend.isPinned()) {
        this.friend.unpin();
      } else {
        this.friend.pin();
      }
      this.updatePinButton();
    }).addId("pin-button");
    this.updatePinButton();
    ButtonWidget noteButton = ButtonWidget.icon(Textures.SpriteCommon.CHAT_BUBBLE,
            this.friend::openNoteEditor)
        .addId("note-button");
    noteButton.setHoverComponent(
        Component.translatable("labymod.activity.labyconnect.chat.action.note"));
    ButtonWidget removeButton = ButtonWidget.icon(Textures.SpriteCommon.X, () -> {
      if (this.skipConfirmation) {
        System.out.println("Removed friend without confirmation");
//          this.friend.remove();
      } else {
        // TODO: show confirmation
        System.out.println("Open friend remove confirmation");
      }
    }).addId("remove-button");
    removeButton.setHoverComponent(
        Component.translatable("Remove friend", NamedTextColor.RED)
            .append(Component.newline())
            .append(Component.translatable(
                "(Hold shift to skip confirmation)",
                NamedTextColor.DARK_GRAY
            ))
    );

    buttons.addEntry(this.pinButton);
    buttons.addEntry(noteButton);
    buttons.addEntry(removeButton);

    this.addEntry(buttons);
  }

  private void updatePinButton() {
    if (this.pinButton == null) {
      return;
    }
    this.pinButton.setHoverComponent(
        Component.translatable(
            this.friend.isPinned()
                ? "labymod.activity.labyconnect.chat.action.unpin"
                : "labymod.activity.labyconnect.chat.action.pin"
        )
    );
  }

  @Subscribe
  public void onKeyDown(KeyEvent event) {
    if (event.key() != Key.L_SHIFT) {
      return;
    }
    if (event.state() == KeyEvent.State.PRESS) {
      this.skipConfirmation = true;
    } else if (event.state() == KeyEvent.State.UNPRESSED) {
      this.skipConfirmation = false;
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    Laby.labyAPI().eventBus().unregisterListener(this);
  }
}
