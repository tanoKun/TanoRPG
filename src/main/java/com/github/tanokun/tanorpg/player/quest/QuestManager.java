package com.github.tanokun.tanorpg.player.quest;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.ItemRarityType;
import com.github.tanokun.tanorpg.player.quest.action.Action;
import com.github.tanokun.tanorpg.player.quest.action.ActionType;
import com.github.tanokun.tanorpg.player.quest.condition.Condition;
import com.github.tanokun.tanorpg.player.quest.condition.ConditionType;
import com.github.tanokun.tanorpg.player.quest.task.Task;
import com.github.tanokun.tanorpg.player.quest.task.TaskType;
import com.github.tanokun.tanorpg.util.command.CommandContext;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.Folder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class QuestManager {
    public static final String PX = "§6[§a-｜ §b§lMission§a ｜-§6] §7=> §a";

    HashMap<Integer, ArrayList<Quest>> quests = new HashMap<>();

    public QuestManager(Player p, Plugin plugin){
        if (!TanoRPG.getPlugin().getDataManager().isInitFile()) new Config("quests" + File.separator + "Sample.yml", plugin).saveDefaultConfig();
        if (p != null) p.sendMessage(TanoRPG.PX + "§bLoading quest configs...");
        else Bukkit.getConsoleSender().sendMessage("[TanoRPG] §bLoading quest configs...");
        loadQuest(p);
        if (p != null) p.sendMessage(TanoRPG.PX + " ");
        else Bukkit.getConsoleSender().sendMessage(" ");
    }

    public void loadQuest(Player p){
        String path = "";
        String filePath = "";
        HashSet<String> errors = new HashSet<>();
        errors.add("§a    Quest configs loaded without errors.");

        CommandContext cc = new CommandContext(null, new String[]{""});

        try {
            path = "quests";
            for (Config config : new Folder(path, TanoRPG.getPlugin()).getFiles()) {
                filePath = path + File.separator + config.getName() + File.separator;

                path = "options.name";
                String name = config.getConfig().getString(path, "unknown");

                path = "options.npc";
                int npcId = config.getConfig().getInt(path, 0);

                path = "options.difficulty";
                ItemRarityType difficulty = ItemRarityType.valueOf(config.getConfig().getString(path, "COMMON"));

                path = "options.lore";
                List<String> lore = new ArrayList<>();
                config.getConfig().getStringList(path).stream().forEach(t -> lore.add("§f" + t));

                path = "options.result";
                List<String> result = new ArrayList<>();
                config.getConfig().getStringList(path).stream().forEach(t -> result.add("§f - " + t));

                path = "conditions";
                List<Condition> conditions = new ArrayList<>();
                for (String condition : config.getConfig().getConfigurationSection(path).getKeys(false)){
                    conditions.add(ConditionType.valueOf(condition).getCondition().getConstructor(Config.class).newInstance(config));
                }

                path = "tasks";
                List<Task> tasks = new ArrayList<>();
                for (String task : config.getConfig().getStringList(path)){
                    cc.init(null, task.split(" "));
                    tasks.add(TaskType.valueOf(cc.args.remove(0)).getTask().getConstructor(CommandContext.class).newInstance(cc));
                }

                path = "showQuestActions";
                List<Action> showQuestActions = new ArrayList<>();
                for (String showQuestAction : config.getConfig().getStringList(path)){
                    cc.init(null, showQuestAction.split(" "));
                    showQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                }

                path = "startQuestActions";
                List<Action> startQuestActions = new ArrayList<>();
                for (String startQuestAction : config.getConfig().getStringList(path)){
                    cc.init(null, startQuestAction.split(" "));
                    startQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                }

                path = "finishQuestActions";
                List<Action> finishQuestActions = new ArrayList<>();
                for (String finishQuestAction : config.getConfig().getStringList(path)){
                    cc.init(null, finishQuestAction.split(" "));
                    finishQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                }

                path = "cancelQuestActions";
                List<Action> cancelQuestActions = new ArrayList<>();
                for (String cancelQuestAction : config.getConfig().getStringList(path)){
                    cc.init(null, cancelQuestAction.split(" "));
                    cancelQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                }

                ArrayList<Quest> quests = this.quests.get(npcId) == null ? new ArrayList<>() : this.quests.get(npcId);
                quests.add(new Quest(name, npcId, lore, result, difficulty, conditions, tasks, showQuestActions, startQuestActions, finishQuestActions, cancelQuestActions));
                this.quests.put(npcId, quests);

            }
        }catch (Exception e){
            errors.remove("§a    Quest configs loaded without errors.");
            errors.add("§c    " + e.getMessage() + "§7" + "(Path: " + filePath + path + ")");
        }

        showErrors(errors, p);
    }

    private void showErrors(HashSet<String> errors, Player p){
        if (p != null) errors.stream().forEach(e -> p.sendMessage( e));
        else errors.stream().forEach(e -> Bukkit.getConsoleSender().sendMessage(e));
    }

    public Quest getQuest(int npcId, String name){
        if (quests.get(npcId) == null) return null;
        final Quest[] r = {null};
        quests.get(npcId).stream()
                .filter(q -> q.getName().equals(name))
                .forEach(q -> r[0] = q);
        return r[0];
    }

    public List<Quest> getQuests(int npcId){
        return quests.get(npcId);
    }
}
