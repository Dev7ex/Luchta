package com.dev7ex.luchta.group;

import com.dev7ex.luchta.LuchtaPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Dev7ex
 * @since 15.12.2022
 */
@Getter(AccessLevel.PUBLIC)
public class GroupService {

    private final GroupConfiguration configuration;

    public GroupService(final GroupConfiguration configuration) {
        this.configuration = configuration;
    }

    public void onEnable() {
        final ArrayList<Group> loadedGroups = new ArrayList<>(LuchtaPlugin.getInstance().getLuckPerms().getGroupManager().getLoadedGroups());

        for (final Group group : loadedGroups) {
            if (this.configuration.isRegistered(group)) {
                this.writeNodes(group);
                continue;
            }
            this.configuration.register(group, '7', "§8[§7" + group.getName() + "§8] §7", loadedGroups.indexOf(group));
            this.writeNodes(group);
        }
    }

    public void onDisable() {
        for (final Group group : LuchtaPlugin.getInstance().getLuckPerms().getGroupManager().getLoadedGroups()) {
            this.clearNodes(group);
        }
    }

    public void registerGroup(final Group group) {
        if (!this.configuration.isRegistered(group)) {
            this.configuration.register(group, '7', "§8[§7" + group.getName() + "§8] §7", 0);
        }
        this.writeNodes(group);
    }

    public void unregisterGroup(final String groupName) {
        this.configuration.unregister(groupName);
    }

    public void writeNodes(final Group group) {
        final MetaNode colorNode = MetaNode.builder("tablist-color", Character.toString(this.configuration.getTablistColor(group.getName()))).build();
        final MetaNode prefixNode = MetaNode.builder("tablist-prefix", this.configuration.getTablistPrefix(group.getName())).build();
        final MetaNode priorityNode = MetaNode.builder("tablist-priority", Integer.toString(this.configuration.getTablistPriority(group.getName()))).build();

        group.data().add(colorNode);
        group.data().add(prefixNode);
        group.data().add(priorityNode);
        LuchtaPlugin.getInstance().getLuckPerms().getGroupManager().saveGroup(group);
    }

    public void clearNodes(final Group group) {
        group.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("tablist-color")));
        group.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("tablist-prefix")));
        group.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("tablist-priority")));
        LuchtaPlugin.getInstance().getLuckPerms().getGroupManager().saveGroup(group);
    }

}
