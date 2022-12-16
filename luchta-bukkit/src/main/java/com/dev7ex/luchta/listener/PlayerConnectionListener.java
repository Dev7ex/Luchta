package com.dev7ex.luchta.listener;

import com.dev7ex.luchta.LuchtaPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class PlayerConnectionListener implements Listener {

    private final LuchtaPlugin plugin;

    public PlayerConnectionListener(final LuchtaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerJoin(final PlayerJoinEvent event) {
        if (!this.plugin.getConfiguration().isTablistEnabled()) {
            return;
        }
        this.plugin.getScoreboardService().updateNameTagsDelayed(event.getPlayer(),
                this.plugin.getLuckPermsHelper().getGroup(event.getPlayer().getUniqueId()),
                3L);
    }

}
