package com.dev7ex.luchta.listener;

import com.dev7ex.luchta.LuchtaPlugin;

import org.bukkit.entity.Player;
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
        final Player player = event.getPlayer();

        if (this.plugin.getConfiguration().isTablistEnabled()) {
            this.plugin.getScoreboardService().updateNameTagsDelayed(player,
                    this.plugin.getLuckPermsHelper().getGroup(player.getUniqueId()),
                    3L);
        }

        if ((this.plugin.isUpdateAvailable()) && (this.plugin.getConfiguration().isUpdateMessageEnabled()) && (player.hasPermission("luchta.update.notify"))) {
            player.sendMessage("§8[§aLuchta§8] §7There is a new update available. https://www.spigotmc.org/resources/luchta.106763/");
        }

    }

}
