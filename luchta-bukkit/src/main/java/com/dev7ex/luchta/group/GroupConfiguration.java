package com.dev7ex.luchta.group;

import lombok.SneakyThrows;
import net.luckperms.api.model.group.Group;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class GroupConfiguration {

    private final File configurationFile;
    private final YamlConfiguration fileConfiguration;

    @SneakyThrows
    public GroupConfiguration(final Plugin plugin, final String name) {
        this.configurationFile = new File(plugin.getDataFolder() + File.separator + name);

        if (!this.configurationFile.exists()) {
            this.configurationFile.createNewFile();
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configurationFile);
    }

    public boolean isRegistered(final Group group) {
        return this.fileConfiguration.contains("groups." + group.getName());
    }

    public void register(final Group group, final char tablistColor, final String tablistPrefix, final int priority) {
        this.fileConfiguration.set("groups." + group.getName() + ".tablist-color", tablistColor);
        this.fileConfiguration.set("groups." + group.getName() + ".tablist-prefix", tablistPrefix);
        this.fileConfiguration.set("groups." + group.getName() + ".tablist-priority", priority);
        this.saveFile();
    }

    public void unregister(final String groupName) {
        this.fileConfiguration.set("groups." + groupName, null);
        this.saveFile();
    }

    public char getTablistColor(final String groupName) {
        return this.fileConfiguration.getString("groups." + groupName + ".tablist-color").charAt(0);
    }

    public String getTablistPrefix(final String groupName) {
        return this.fileConfiguration.getString("groups." + groupName + ".tablist-prefix");
    }

    public int getTablistPriority(final String groupName) {
        return this.fileConfiguration.getInt("groups." + groupName + ".tablist-priority");
    }

    @SneakyThrows
    public void saveFile() {
        this.fileConfiguration.save(this.configurationFile);
    }

}
