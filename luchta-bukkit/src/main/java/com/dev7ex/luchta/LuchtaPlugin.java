package com.dev7ex.luchta;

import com.dev7ex.luchta.group.*;
import com.dev7ex.luchta.listener.GroupEditListener;
import com.dev7ex.luchta.listener.PlayerChatListener;
import com.dev7ex.luchta.listener.PlayerConnectionListener;
import com.dev7ex.luchta.listener.UserGroupUpdateListener;
import com.dev7ex.luchta.scoreboard.ScoreboardService;
import com.dev7ex.luchta.util.LuckPermsHelper;

import lombok.AccessLevel;
import lombok.Getter;

import net.luckperms.api.LuckPerms;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
@Getter(AccessLevel.PUBLIC)
public class LuchtaPlugin extends JavaPlugin {

    private static final int SERVICE_ID = 17151;
    private static final int RESOURCE_ID = 106763;

    private boolean updateAvailable = false;

    private LuchtaConfiguration configuration;

    private LuckPerms luckPerms;
    private LuckPermsHelper luckPermsHelper;

    private GroupConfiguration groupConfiguration;
    private GroupService groupService;

    private ScoreboardService scoreboardService;

    @Override
    public void onLoad() {
        this.configuration = new LuchtaConfiguration(this);
        this.configuration.load();

        this.groupConfiguration = new GroupConfiguration(this, "group.yml");
    }

    @Override
    public void onEnable() {
        this.luckPerms = super.getServer().getServicesManager().load(LuckPerms.class);
        this.luckPermsHelper = new LuckPermsHelper(this.luckPerms);

        this.groupService = new GroupService(this.groupConfiguration);
        this.groupService.onEnable();

        this.scoreboardService = new ScoreboardService(this);
        this.scoreboardService.onEnable();

        final Metrics metrics = new Metrics(this, LuchtaPlugin.SERVICE_ID);
        this.checkUpdates();

        this.registerListeners();
    }

    @Override
    public void onDisable() {
        this.groupService.onDisable();
        this.scoreboardService.onDisable();
    }

    public void registerListeners() {
        final PluginManager pluginManager = super.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerConnectionListener(this), this);
        pluginManager.registerEvents(new PlayerChatListener(this.configuration), this);

        final GroupEditListener groupEditListener = new GroupEditListener(this);
        groupEditListener.register();

        final UserGroupUpdateListener updateListener = new UserGroupUpdateListener(this);
        updateListener.register();
    }

    public void checkUpdates() {
        super.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try (final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + LuchtaPlugin.RESOURCE_ID).openStream()) {
                try (final Scanner scanner = new Scanner(inputStream)) {
                    if (scanner.hasNext()) {
                        if (!this.getDescription().getVersion().equalsIgnoreCase(scanner.next())) {
                            this.updateAvailable = true;
                            super.getServer().getScheduler().runTask(this, () -> {
                                super.getLogger().info("There is a new update available.");
                            });

                        }
                    }
                }
            } catch (final IOException exception) {
                super.getServer().getScheduler().runTask(this, () -> {
                    super.getLogger().info("Unable to check for updates: " + exception.getMessage());
                });
            }
        });
    }

    public static LuchtaPlugin getInstance() {
        return JavaPlugin.getPlugin(LuchtaPlugin.class);
    }

}
