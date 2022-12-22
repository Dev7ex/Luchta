package com.dev7ex.luchta;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
@Getter(AccessLevel.PUBLIC)
public class LuchtaConfiguration {

    private final FileConfiguration fileConfiguration;
    private String chatFormat;
    private boolean chatEnabled;
    private boolean tablistEnabled;
    private boolean updateMessageEnabled;

    public LuchtaConfiguration(final Plugin plugin) {
        this.fileConfiguration = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public void load() {
        this.chatFormat = this.fileConfiguration.getString("settings.chat-format");
        this.chatEnabled = this.fileConfiguration.getBoolean("settings.chat-enabled"  );
        this.tablistEnabled = this.fileConfiguration.getBoolean("settings.tablist-enabled");
        this.updateMessageEnabled = this.fileConfiguration.getBoolean("settings.update-message");
    }

}
