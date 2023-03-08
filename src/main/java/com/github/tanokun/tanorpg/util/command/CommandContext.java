package com.github.tanokun.tanorpg.util.command;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public class CommandContext {
    public List<String> args = new ArrayList<>();

    String[] baseArgs;

    protected Set<Character> flags = new HashSet<>();

    private Location location = null;

    private CommandSender sender;

    protected Map<String, String> valueFlags = Maps.newHashMap();

    public CommandContext(CommandSender sender, String[] args) {
        this.sender = sender;
        init(sender, args);
    }

    public CommandContext(String[] args) {
        this(null, args);
    }

    public CommandContext(){}

    public void init(CommandSender sender, String[] args) {
        if (args == null) return;
        this.sender = sender;
        int i = 0;
        valueFlags.clear();
        flags.clear();
        this.args.clear();
        baseArgs = args;
        for (; i < args.length; i++) {
            args[i] = args[i].trim();
            if (args[i].length() != 0)
                if (args[i].charAt(0) == '\'' || args[i].charAt(0) == '"' || args[i].charAt(0) == '`') {
                    char quote = args[i].charAt(0);
                    String quoted = args[i].substring(1);
                    if (quoted.length() > 0 && quoted.charAt(quoted.length() - 1) == quote) {
                        args[i] = quoted.substring(0, quoted.length() - 1);
                    } else {
                        for (int inner = i + 1; inner < args.length; inner++) {
                            if (!args[inner].isEmpty()) {
                                String test = args[inner].trim();
                                quoted = quoted + " " + test;
                                if (test.charAt(test.length() - 1) == quote) {
                                    args[i] = quoted.substring(0, quoted.length() - 1);
                                    for (int j = i + 1; j <= inner; j++)
                                        args[j] = "";
                                    break;
                                }
                            }
                        }
                    }
                }
        }

        for (i = 0; i < args.length; i++) {
            int length = args[i].length();
            if (length != 0) {
                if (i + 1 < args.length && length > 2 && args[i].contains("--")) {
                    this.valueFlags.put(args[i].substring(2), args[i + 1]);
                    args[i] = "";
                    args[i + 1] = "";
                } else if (FLAG.matcher(args[i]).matches()) {
                    for (int k = 1; k < args[i].length(); k++)
                        this.flags.add(args[i].charAt(k));
                    args[i] = "";
                }
            }
        }

        List<String> copied = Lists.newArrayList();
        for (String arg : args) {
            arg = arg.trim();
            if (arg != null && !arg.isEmpty())
                copied.add(arg.trim());
        }
        this.args = copied;
    }

    public int argsLength() {
        return this.args.size();
    }

    public String getCommand() {
        return this.args.get(0);
    }

    public double getDouble(int index) throws NumberFormatException {
        return Double.parseDouble(this.args.get(index));
    }

    public double getDouble(int index, double def) throws NumberFormatException {
        return (index < this.args.size()) ? Double.parseDouble(this.args.get(index)) : def;
    }

    public String getFlag(String ch) {
        return this.valueFlags.get(ch);
    }

    public String getFlag(String ch, String def) {
        String value = this.valueFlags.get(ch);
        if (value == null)
            return def;
        return value;
    }

    public double getFlagDouble(String ch) throws NumberFormatException {
        return Double.parseDouble(this.valueFlags.get(ch));
    }

    public double getFlagDouble(String ch, double def) throws NumberFormatException {
        String value = this.valueFlags.get(ch);
        if (value == null)
            return def;
        return Double.parseDouble(value);
    }

    public int getFlagInteger(String ch) throws NumberFormatException {
        return Integer.parseInt(this.valueFlags.get(ch));
    }

    public int getFlagInteger(String ch, int def) throws NumberFormatException {
        String value = this.valueFlags.get(ch);
        if (value == null)
            return def;
        return Integer.parseInt(value);
    }

    public Set<Character> getFlags() {
        return this.flags;
    }

    public int getInteger(int index) throws NumberFormatException {
        return Integer.parseInt(this.args.get(index));
    }

    public int getInteger(int index, int def) throws NumberFormatException {
        if (index + 1 < this.args.size())
            try {
                return Integer.parseInt(this.args.get(index));
            } catch (NumberFormatException numberFormatException) {
            }
        return def;
    }

    public String getJoinedStrings(int initialIndex) {
        return getJoinedStrings(initialIndex, ' ');
    }

    public String getJoinedStrings(int initialIndex, char delimiter) {
        initialIndex++;
        StringBuilder buffer = new StringBuilder(this.args.get(initialIndex));
        for (int i = initialIndex + 1; i < this.args.size(); i++)
            buffer.append(delimiter).append(this.args.get(i));
        return buffer.toString().trim();
    }

    public String[] getPaddedSlice(int index, int padding) {
        String[] slice = new String[this.args.size() - index + padding];
        System.arraycopy(this.args, index, slice, padding, this.args.size() - index);
        return slice;
    }

    public Location getSenderTargetBlockLocation() {
        if (this.sender == null)
            return this.location;
        if (this.sender instanceof Player) {
            this.location = ((Player) this.sender).getTargetBlock((Set) null, 50).getLocation();
        } else if (this.sender instanceof BlockCommandSender) {
            this.location = ((BlockCommandSender) this.sender).getBlock().getLocation();
        }
        return this.location;
    }

    public String[] getSlice(int index) {
        String[] slice = new String[this.args.size() - index];
        System.arraycopy(this.args, index, slice, 0, this.args.size() - index);
        return slice;
    }

    public String getString(int index) {
        return this.args.get(index);
    }

    public String getString(int index, String def) {
        return (index < this.args.size()) ? this.args.get(index) : def;
    }

    public Map<String, String> getValueFlags() {
        return this.valueFlags;
    }

    public boolean hasAnyFlags() {
        return (this.valueFlags.size() > 0 || this.flags.size() > 0);
    }

    public boolean hasAnyValueFlag(String... strings) {
        for (String s : strings) {
            if (hasValueFlag(s))
                return true;
        }
        return false;
    }

    public boolean hasFlag(char ch) {
        return this.flags.contains(Character.valueOf(ch));
    }

    public boolean hasValueFlag(String ch) {
        return this.valueFlags.containsKey(ch);
    }

    public int length() {
        return this.args.size();
    }

    public boolean matches(String command) {
        return this.args.get(0).equalsIgnoreCase(command);
    }

    public String getArg(int i, String re) {
        if ((i + 1) > args.size()) {
            return re;
        }
        return args.get(i) != null ? args.get(i) : re;
    }

    public String[] getBaseArgs() {
        return baseArgs;
    }

    public List<String> getArgs() {
        return args;
    }

    private static final Pattern FLAG = Pattern.compile("^-[a-zA-Z]+$");

    private static final Splitter LOCATION_SPLITTER = Splitter.on(Pattern.compile("[,]|[:]")).omitEmptyStrings();
}
