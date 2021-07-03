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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestManager {
    public static final String PX = "§6[§a-｜ §b§lMission§a ｜-§6] §7=> §a";

    HashMap<Integer, ArrayList<Quest>> quests = new HashMap<>();

    public QuestManager(Player player, Plugin plugin){
        if (!TanoRPG.getPlugin().getDataManager().isInitFile()) new Config("quests" + File.separator + "Sample.yml", plugin).saveDefaultConfig();
        loadQuest(player);
    }

    public void loadQuest(Player p){
        final CommandContext cc = new CommandContext(null, new String[]{""});
        try {
            for (Config config : new Folder("quests", TanoRPG.getPlugin()).getFiles()) {
                String name = config.getConfig().getString("options.name", "unknown name");
                int npcId = config.getConfig().getInt("options.npc", 0);
                ItemRarityType difficulty = ItemRarityType.valueOf(config.getConfig().getString("options.difficulty", "COMMON"));
                List<Condition> conditions = new ArrayList<>();
                List<Task> tasks = new ArrayList<>();
                List<Action> showQuestActions = new ArrayList<>();
                List<Action> startQuestActions = new ArrayList<>();
                List<Action> finishQuestActions = new ArrayList<>();
                List<Action> cancelQuestActions = new ArrayList<>();
                List<String> lore = new ArrayList<>();
                List<String> result = new ArrayList<>();

                config.getConfig().getStringList("options.lore").stream().forEach(t -> lore.add("§f" + t));
                config.getConfig().getStringList("options.result").stream().forEach(t -> result.add("§f - " + t));


                config.getConfig().getConfigurationSection("conditions").getKeys(false).stream()
                        .forEach(condition ->{
                            try {
                                conditions.add(ConditionType.valueOf(condition).getCondition().getConstructor(Config.class).
                                        newInstance(config));
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            }
                        });

                config.getConfig().getList("tasks", new ArrayList<String>()).stream()
                        .forEach(t -> {
                            String task = String.valueOf(t);
                            cc.init(null, task.split(" "));
                            try {
                                tasks.add(TaskType.valueOf(cc.args.remove(0)).getTask().getConstructor(CommandContext.class).newInstance(cc));
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        });

                config.getConfig().getList("showQuestActions", new ArrayList<String>()).stream()
                        .forEach(t -> {
                            String task = String.valueOf(t);
                            cc.init(null, task.split(" "));
                            try {
                                showQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        });

                config.getConfig().getList("startQuestActions", new ArrayList<String>()).stream()
                        .forEach(t -> {
                            String task = String.valueOf(t);
                            cc.init(null, task.split(" "));
                            try {
                                startQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        });

                config.getConfig().getList("finishQuestActions", new ArrayList<String>()).stream()
                        .forEach(t -> {
                            String task = String.valueOf(t);
                            cc.init(null, task.split(" "));
                            try {
                                finishQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        });

                config.getConfig().getList("cancelQuestActions", new ArrayList<String>()).stream()
                        .forEach(t -> {
                            String task = String.valueOf(t);
                            cc.init(null, task.split(" "));
                            try {
                                cancelQuestActions.add(ActionType.valueOf(cc.args.remove(0)).getAction().getConstructor(CommandContext.class).newInstance(cc));
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException exception) {
                                exception.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        });

                ArrayList<Quest> quests = this.quests.get(npcId) == null ? new ArrayList<>() : this.quests.get(npcId);
                quests.add(new Quest(name, npcId, lore, result, difficulty, conditions, tasks, showQuestActions, startQuestActions, finishQuestActions, cancelQuestActions));
                this.quests.put(npcId, quests);

            }
        }catch (Exception e){
            if (p != null){
                p.sendMessage(TanoRPG.PX + "§cクエストのコンフィグでエラーが発生しました。");
            } else {
                TanoRPG.getPlugin().getLogger().warning("クエストのコンフィグでエラーが発生しました。");
            }
        }

        if (p != null){
            p.sendMessage(TanoRPG.PX + "§aクエストのコンフィグをロードしました。");
        } else {
            TanoRPG.getPlugin().getLogger().warning("クエストのコンフィグをロードしました。");
        }
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
