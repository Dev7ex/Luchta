package com.dev7ex.luchta.util;

import lombok.AccessLevel;
import lombok.Getter;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.*;

/**
 * @author Dev7ex
 * @since 16.12.2022
 * <p>
 * A Utility to simplify luckperms methods
 */
@Getter(AccessLevel.PUBLIC)
public class LuckPermsHelper {

    private final LuckPerms luckPerms;

    public LuckPermsHelper(final LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    /**
     * Returns the User from luckperms
     *
     * @param uniqueId of the player
     * @return user object from luckperms
     */
    public User getUser(final UUID uniqueId) {
        return this.luckPerms.getUserManager().getUser(uniqueId);
    }

    /**
     * Returns the Group saved by luckperms
     *
     * @param uniqueId of the player
     * @return user group
     */
    public Group getGroup(final UUID uniqueId) {
        return this.luckPerms.getGroupManager().getGroup(this.getUser(uniqueId).getPrimaryGroup());
    }

    /**
     * Returns the default group
     *
     * @return the luckperms default group
     */
    public Group getDefaultGroup() {
        return this.luckPerms.getGroupManager().getGroup("default");
    }

    /**
     * Sets the primary group in the luckperms user object
     *
     * @param user luckperms User
     * @param groupName the group to set
     */
    public void setPrimaryGroup(final User user, final String groupName) {
        user.data().add(this.getGroupNode(groupName));
        this.luckPerms.getUserManager().saveUser(user);
    }

    public InheritanceNode getGroupNode(final String groupName) {
        return InheritanceNode.builder(groupName).build();
    }

    /**
     * Removes a group from the groups of the user
     *
     * @param user luckperms User
     * @param groupName the group to remove
     */
    public void removeParentGroup(final User user, final String groupName) {
        user.data().remove(this.getGroupNode(groupName));
        this.luckPerms.getUserManager().saveUser(user);
    }

    /**
     * Returns all users who are in the specified group
     * Couldn't find such a method in the luckperms api
     *
     * @param groupName
     */
    public Map<UUID, User> getUsersInGroup(final String groupName) {
        final Map<UUID, User> users = new HashMap<>();

        for (final User user : this.luckPerms.getUserManager().getLoadedUsers()) {
            if (!user.getPrimaryGroup().equalsIgnoreCase(groupName)) {
                continue;
            }
            users.put(user.getUniqueId(), user);
        }
        return users;
    }

}
