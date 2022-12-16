package com.dev7ex.luchta.listener;

import com.dev7ex.luchta.LuchtaPlugin;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.user.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class UserGroupUpdateListener {

    private LuchtaPlugin plugin;

    public UserGroupUpdateListener(final LuchtaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        final EventBus eventBus = this.plugin.getLuckPerms().getEventBus();
        eventBus.subscribe(UserPromoteEvent.class, event -> this.handleUserGroupUpdate(event.getUser()));
        eventBus.subscribe(UserDataRecalculateEvent.class, event -> this.handleUserGroupUpdate(event.getUser()));
    }

    public void handleUserGroupUpdate(final User user) {
        if (!this.plugin.getConfiguration().isTablistEnabled()) {
            return;
        }
        final Player player = Bukkit.getPlayer(user.getUniqueId());

        if (player == null) {
            return;
        }
        this.plugin.getScoreboardService().updateNameTagsDelayed(player, this.plugin.getLuckPermsHelper().getGroup(player.getUniqueId()), 3L);
    }


}
