package com.github.tanokun.tanorpg.util.command;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermission {

    String permission();

    PermissionDefault perDefault();

    String permissionMessage() default "§cパーミッションがありません！";
}
