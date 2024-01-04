package com.mcmiddleearth.pvpplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    RUN        ("pvp.gameCanRun",      PermissionDefault.OP),
    MAP_EDITOR ("pvp.mapEditor",       PermissionDefault.OP),
    PVP_ADMIN  ("pvp.adminPermission", PermissionDefault.OP);

    private final String permissionNode;

    private final Permissions[] children;

    private final PermissionDefault defaultPerm;

    Permissions(String permissionNode, PermissionDefault defaultPerm, Permissions... children) {
        this.permissionNode = permissionNode;
        this.children = children;
        this.defaultPerm = defaultPerm;
    }

    public Permissions[] getWithChildren() {
        Permissions[] result = Arrays.copyOf(children, children.length+1);
        result[children.length] = this;
        return result;
    }

    public static void register() {
        for(Permissions editorPermission: Permissions.values()) {
            Map<String, Boolean> children = new HashMap<>();
            for(Permissions child: editorPermission.getChildren()) {
                children.put(child.getPermissionNode(), Boolean.TRUE);
            }
            Permission bukkitPerm = new Permission(editorPermission.getPermissionNode(),
                    editorPermission.getDefaultPerm(),
                    children);
            Bukkit.getServer().getPluginManager().addPermission(bukkitPerm);
        }
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public Permissions[] getChildren() {
        return children;
    }

    public PermissionDefault getDefaultPerm() {
        return defaultPerm;
    }
}
