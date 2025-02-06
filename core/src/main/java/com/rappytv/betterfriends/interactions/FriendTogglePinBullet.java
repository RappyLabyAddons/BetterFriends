package com.rappytv.betterfriends.interactions;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class FriendTogglePinBullet implements BulletPoint {

    private Friend friend;

    @Override
    public Component getTitle() {
        return Component.translatable("betterfriends.interactions.togglePin." + (this.friend.isPinned() ? "un" : "") + "pin.title");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        if (this.friend.isPinned()) {
            this.friend.unpin();
        } else {
            this.friend.pin();
        }
    }

    @Override
    public boolean isVisible(Player player) {
        LabyConnectSession session = Laby.references().labyConnect().getSession();
        if (session == null || !session.isAuthenticated()) return false;

        Friend friend = session.getFriend(player.getUniqueId());
        if (friend == null) return false;

        this.friend = friend;
        return true;
    }
}
