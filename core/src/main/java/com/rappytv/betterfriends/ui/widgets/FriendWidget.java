package com.rappytv.betterfriends.ui.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

@AutoWidget
public class FriendWidget extends HorizontalListWidget {

    protected final Friend friend;

    public FriendWidget(Friend friend) {
        this.friend = friend;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        IconWidget headWidget = new IconWidget(Icon.head(this.friend.getUniqueId()))
                .addId("player-head");
        headWidget.setHoverComponent(Component.translatable("Open on laby.net"));
        headWidget.setPressable(() ->
                Laby.references().chatExecutor().openUrl("https://laby.net/@" + this.friend.getUniqueId())
        );

        ComponentWidget usernameWidget = ComponentWidget
                .component(Component.text(
                        this.friend.getName(),
                        TextColor.color(this.friend.gameUser().visibleGroup().getColor().getRGB())
                ))
                .addId("username-component");

        usernameWidget.setHoverComponent(Component.translatable("Click to copy UUID"));
        usernameWidget.setPressable(() ->
                Laby.references().chatExecutor().copyToClipboard(this.friend.getUniqueId().toString())
        );

        this.addEntry(headWidget);
        this.addEntry(usernameWidget);
    }

    public Friend getFriend() {
        return this.friend;
    }
}
