package com.dev7ex.luchta.listener;

import com.dev7ex.luchta.LuchtaConfiguration;
import com.dev7ex.luchta.LuchtaPlugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class PlayerChatListener implements Listener {

    private LuchtaConfiguration luchtaConfiguration;

    public PlayerChatListener(final LuchtaConfiguration luchtaConfiguration) {
        this.luchtaConfiguration = luchtaConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerChat(final AsyncPlayerChatEvent event) {
        if (!this.luchtaConfiguration.isChatEnabled()) {
            return;
        }
        final Player player = event.getPlayer();
        final LuckPerms luckPerms = LuchtaPlugin.getInstance().getLuckPerms();

        final CachedMetaData metaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
        final String groupPrefix = (metaData.getPrefix() == null ? "" : metaData.getPrefix());

        event.setFormat(this.luchtaConfiguration.getChatFormat()
                .replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', groupPrefix))
                .replaceAll("%name%", player.getName())
                .replaceAll("%message%", event.getMessage()));
    }

}
