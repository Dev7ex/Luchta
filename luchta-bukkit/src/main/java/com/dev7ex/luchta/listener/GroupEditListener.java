package com.dev7ex.luchta.listener;

import com.dev7ex.luchta.LuchtaPlugin;

import net.luckperms.api.event.group.GroupCreateEvent;
import net.luckperms.api.event.group.GroupDeleteEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class GroupEditListener {

    private LuchtaPlugin plugin;

    public GroupEditListener(final LuchtaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        this.plugin.getLuckPerms().getEventBus().subscribe(GroupCreateEvent.class, this::handleGroupCreate);
        this.plugin.getLuckPerms().getEventBus().subscribe(GroupDeleteEvent.class, this::handleGroupDelete);
    }

    public void handleGroupCreate(final GroupCreateEvent event) {
        this.plugin.getGroupService().registerGroup(event.getGroup());
    }

    public void handleGroupDelete(final GroupDeleteEvent event) {
        this.plugin.getLuckPermsHelper().getUsersInGroup(event.getGroupName()).values().forEach(user -> {
            final Player player = Bukkit.getPlayer(user.getUniqueId());

            this.plugin.getScoreboardService().updateNameTagsDelayed(player, this.plugin.getLuckPermsHelper().getDefaultGroup(), 3L);
            this.plugin.getLuckPermsHelper().setPrimaryGroup(user, "default");
            this.plugin.getLuckPermsHelper().removeParentGroup(user, event.getGroupName());
        });
        this.plugin.getGroupService().unregisterGroup(event.getGroupName());
    }

}
