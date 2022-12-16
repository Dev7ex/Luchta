package com.dev7ex.luchta.scoreboard;

import com.dev7ex.luchta.LuchtaPlugin;

import net.luckperms.api.model.group.Group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
public class ScoreboardService {

    public final LuchtaPlugin plugin;

    public ScoreboardService(final LuchtaPlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> this.updateNameTags(player, this.plugin.getLuckPermsHelper().getGroup(player.getUniqueId())));
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.getScoreboard().clearSlot(DisplaySlot.PLAYER_LIST));
    }

    public void initializeScoreboard(final Player player) {
        if (!player.getScoreboard().equals(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard())) {
            return;
        }
        player.setScoreboard(Objects.requireNonNull(player.getServer().getScoreboardManager()).getNewScoreboard());
    }

    public void addTeamEntry(final Player entry, final Player other, final Group group) {
        final Team entryTeam = other.getScoreboard().getTeam(this.getTeamName(group)) == null ?
                other.getScoreboard().registerNewTeam(this.getTeamName(group)) :
                other.getScoreboard().getTeam(this.getTeamName(group));

        entryTeam.setPrefix(this.getTablistPrefix(group));
        entryTeam.setColor(this.getTablistColor(group));
        entryTeam.addEntry(entry.getName());
    }

    public String getTeamName(final Group group) {
        return String.format("%d%s", this.getTablistPriority(group), this.getGroupName(group));
    }

    public void updateNameTags(final Player player, final Group group) {
        this.initializeScoreboard(player);

        for (final Player target : Bukkit.getOnlinePlayers()) {
            final Group targetGroup = this.plugin.getLuckPermsHelper().getGroup(target.getUniqueId());

            this.initializeScoreboard(target);
            this.addTeamEntry(player, target, group);
            this.addTeamEntry(target, player, targetGroup);
        }
    }

    /**
     * Updates the Tablist delayed
     * prevents errors like team already initialized
     */
    public void updateNameTagsDelayed(final Player player, final Group group, final long delay) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.updateNameTags(player, group);
        }, delay);
    }

    public String getGroupName(final Group group) {
        if (group == null) {
            return this.plugin.getLuckPermsHelper().getDefaultGroup().getName();
        }
        return group.getName();
    }

    public String getTablistPrefix(final Group group) {
        if (group == null) {
            return this.plugin.getLuckPermsHelper().getDefaultGroup().getCachedData().getMetaData().getMetaValue("tablist-prefix");
        }
        return group.getCachedData().getMetaData().getMetaValue("tablist-prefix");
    }

    public ChatColor getTablistColor(final Group group) {
        if (group == null) {
            return ChatColor.getByChar(this.plugin.getLuckPermsHelper().getDefaultGroup().getCachedData().getMetaData().getMetaValue("tablist-color").charAt(0));
        }
        return ChatColor.getByChar(group.getCachedData().getMetaData().getMetaValue("tablist-color").charAt(0));
    }

    public int getTablistPriority(final Group group) {
        if (group == null) {
            return this.plugin.getLuckPermsHelper().getDefaultGroup().getCachedData().getMetaData().getMetaValue("tablist-priority", Integer::parseInt).orElseThrow();
        }
        return group.getCachedData().getMetaData().getMetaValue("tablist-priority", Integer::parseInt).orElseThrow();
    }

}
