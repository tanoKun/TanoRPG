package com.github.tanokun.tanorpg.util.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.permissions.DefaultPermissions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CommandManager {
    private final Plugin plugin;
    private final CraftServer server;

    private HashMap<String, CommandEntity> commands = new HashMap<>();

    public CommandManager(Plugin plugin){
        this.plugin = plugin;
        this.server = (CraftServer) Bukkit.getServer();
    }

    public void registerCommand(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Command.class) == null && method.getAnnotation(TabComplete.class) == null) continue;
            Command command = method.getAnnotation(Command.class);
            CommandEntity commandEntity = null;

            if (method.getAnnotation(TabComplete.class) != null){
                TabComplete tabComplete = method.getAnnotation(TabComplete.class);
                if (commands.get(tabComplete.parentName()) == null) {
                    try {
                        commandEntity = new CommandEntity(tabComplete.parentName(), clazz.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    commands.put(tabComplete.parentName(), commandEntity);
                    server.getCommandMap().register("tanorpg", commandEntity);
                }
                commands.get(tabComplete.parentName()).registerTabComplete(tabComplete.name(), method);
                continue;
            }

            if (commands.containsKey(command.parentName())) {
                commandEntity = commands.get(command.parentName());
            } else {
                try {commandEntity = new CommandEntity(command.parentName(), clazz.newInstance());
                } catch (InstantiationException e) {e.printStackTrace();
                } catch (IllegalAccessException e) {e.printStackTrace();}

                server.getCommandMap().register("tanorpg", commandEntity);
                commands.put(command.parentName(), commandEntity);
                commandEntity.registerSubCommand(command.name(), method);
            }

            if (method.getAnnotation(CommandPermission.class) != null) {
                CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);
                DefaultPermissions.registerPermission(commandPermission.permission(), "Permission", commandPermission.perDefault());
                commandEntity.setPermission(commandPermission.permission());
            }

            commandEntity.registerSubCommand(command.name(), method);

        }
    }

    public boolean hasCommand(String name){
        return commands.containsKey(name);
    }

    public CommandEntity getCommand(String name){
        return commands.get(name);
    }

    public class CommandEntity extends org.bukkit.command.Command {
        private Object object;
        private HashMap<String, Method> subs = new HashMap<>();
        private HashMap<String, Method> ct = new HashMap<>();
        private String permission = "";

        protected CommandEntity(String name, Object o) {
            super(name);
            object = o;
        }

        public void registerSubCommand(String name, Method method){
            subs.put(name, method);
        }

        public void registerTabComplete(String name, Method method){
            ct.put(name, method);
        }

        public void setPermission(String name){
            permission = name;
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            CommandContext commandContext = new CommandContext(sender, args);
            String sub = commandContext.getArg(0, "");
            if (sub.equals("")) {sender.sendMessage("§c引数が存在しません"); return true;}
            if (!subs.containsKey(sub)) {sender.sendMessage("§cその引数は存在しません (" + sub + ")"); return true;}

            if (subs.get(sub).getAnnotation(CommandPermission.class) != null){
                CommandPermission commandPermission = subs.get(sub).getAnnotation(CommandPermission.class);
                if (!sender.hasPermission(commandPermission.permission())){
                    sender.sendMessage(commandPermission.permissionMessage());
                    return true;
                }
            }

            try {
                ArrayList<String> test = new ArrayList<>(Arrays.asList(args));
                test.remove(0);
                subs.get(sub).invoke(object, sender, new CommandContext(test.toArray(new String[test.size()])));
            } catch (IllegalAccessException e) { e.printStackTrace(); }
            catch (InvocationTargetException exception) { exception.printStackTrace();}

            return true;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
            if (!permission.equals("")) {
                if (!sender.hasPermission(permission)) return null;
            }

            CommandContext commandContext = new CommandContext(sender, args);

            ArrayList<String> result = new ArrayList<>();
            Command command;

            if (args.length <= 1){
                String search = args.length == 1 ? args[0] : "";
                Iterator commands = this.subs.keySet().iterator();
                while(commands.hasNext()) {
                    String cmd = (String) commands.next();
                    if (cmd.startsWith(search)) {
                        result.add(cmd);
                    }
                }
                return result;
            } else {
                if (subs.get(args[0]) == null) return null;
                command = subs.get(commandContext.getArg(0, "")).getAnnotation(Command.class);

                for(String flag : command.flags().split(" ")){
                    if (flag.startsWith("--")){
                        flag = flag.toLowerCase().substring(2);
                        if (commandContext.hasValueFlag(flag)) continue;
                        result.add("--" + flag);
                    }
                    else if (flag.startsWith("-")){
                        if (commandContext.hasFlag(flag.charAt(1))) continue;
                        result.add(flag);
                    }
                }

                if (ct.get(args[0]) != null){
                    Method method = ct.get(args[0]);
                    try {
                        commandContext.args.remove(0);
                        result.addAll((Collection<? extends String>) method.invoke(object, sender, commandContext));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException exception) {
                        exception.printStackTrace();
                    }
                }
            }

            //if (commandContext.args.size() == 0){
            //    for (String sub : subs.keySet()){
            //        if (subs.get(sub).getAnnotation(CommandPermission.class) != null){
            //            if (sender.hasPermission(subs.get(sub).getAnnotation(CommandPermission.class).permission()))
            //                show.add(sub);
            //        } else show.add(sub);
            //    }
            //    return show;

            //} else {
            //    String sub = commandContext.getArg(0, "");

            //    if (subs.get(sub) == null) return null;
            //    command = subs.get(commandContext.getArg(0, "")).getAnnotation(Command.class);
            //    if (subs.get(sub).getAnnotation(CommandPermission.class) != null){
            //        if (!sender.hasPermission(subs.get(sub).getAnnotation(CommandPermission.class).permission())) return null;
            //    }

            //    for(String flag : command.flags().split(" ")){
            //        if (flag.startsWith("--")){
            //            flag = flag.toLowerCase().substring(2);
            //            if (commandContext.hasValueFlag(flag)) continue;
            //            show.add("--" + flag);
            //        }
            //        else if (flag.startsWith("-")){
            //            if (commandContext.hasFlag(flag.charAt(1))) continue;
            //            show.add(flag);
            //        }
            //    }

            //    if (ct.get(sub) != null){
            //        Method method = ct.get(sub);
            //        try {
            //            commandContext.args.remove(0);
            //            commandContext.args.add("dummy");
            //            show.addAll((Collection<? extends String>) method.invoke(object, sender, commandContext));
            //        } catch (IllegalAccessException e) {
            //            e.printStackTrace();
            //        } catch (InvocationTargetException exception) {
            //            exception.printStackTrace();
            //        }
            //    }

            return result;
        }
    }
}
