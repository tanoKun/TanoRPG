package com.github.tanokun.tanorpg.command.register;

import java.util.HashMap;

public class Register {
    private static HashMap<String, Command> commands = new HashMap<>();
    public static void register(Command command){commands.put(command.getName(), command);}
    public static Command getCommand(String name) {
        if (commands.get(name) == null){
            return new NullCommand();
        }
        return commands.get(name);
    }
}
