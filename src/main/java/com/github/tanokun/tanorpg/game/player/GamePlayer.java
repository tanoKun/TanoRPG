package com.github.tanokun.tanorpg.game.player;

import com.github.tanokun.tanorpg.TanoRPG;
import com.github.tanokun.tanorpg.game.item.CustomItemManager;
import com.github.tanokun.tanorpg.game.item.CustomItemType;
import com.github.tanokun.tanorpg.game.player.status.Sidebar;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class GamePlayer {
    private GamePlayerJobType job;

    private String name;
    private UUID uuid;

    private double THIS_MAX_HP = 200;
    private double MAX_HP = 200;
    private double HAS_HP = 200;

    private double MAX_MP = 100;
    private double THIS_MAX_MP = 100;
    private double HAS_MP = 100;

    private int LEVEL = 1;
    private long MAX_EXP = 100;
    private long HAS_EXP = 0;

    private HashMap<StatusType, Status> statuses;
    private ArrayList<String> skills = new ArrayList<>();

    public HashMap<StatusType, Status>  getStatuses() {return statuses;}
    public String getName() {return name;}
    public GamePlayerJobType getJob() {return job;}
    public UUID getUuid() { return uuid;}
    public long getMoney() {
        return Math.round(TanoRPG.getEcon().getBalance(getOfflinePlayer(uuid)));
    }
    public double getMAX_HP() {return MAX_HP;}
    public double getTHIS_MAX_HP() {return THIS_MAX_HP;}
    public double getHAS_HP() {return HAS_HP;}
    public double getHAS_MP() {return HAS_MP;}
    public double getMAX_MP() {return MAX_MP;}
    public double getTHIS_MAX_MP() {return THIS_MAX_MP;}
    public int getLEVEL() {return LEVEL;}
    public long getMAX_EXP() {return MAX_EXP;}
    public long getHAS_EXP() {return HAS_EXP;}

    public void setMoney(long money) {
        TanoRPG.getEcon().depositPlayer(Bukkit.getPlayer(uuid), getMoney() - money);
        Sidebar.updateSidebar(getPlayer());
    }
    public void removeMoney(long money) {
        TanoRPG.getEcon().withdrawPlayer(Bukkit.getPlayer(uuid), money);
        Sidebar.updateSidebar(getPlayer());
    }
    public void addMoney(long money) {
        TanoRPG.getEcon().depositPlayer(Bukkit.getPlayer(uuid), money);
        Sidebar.updateSidebar(getPlayer());
    }
    public void setTHIS_MAX_HP(double THIS_MAX_HP) {this.THIS_MAX_HP = THIS_MAX_HP; Sidebar.updateSidebar(getPlayer());}
    public void setMAX_HP(double MAX_HP) {this.MAX_HP = MAX_HP; Sidebar.updateSidebar(getPlayer());}
    public void setHAS_HP(double HAS_HP) {this.HAS_HP = HAS_HP; Sidebar.updateSidebar(getPlayer());}
    public void setMAX_MP(double MAX_MP) {this.MAX_MP = MAX_MP; Sidebar.updateSidebar(getPlayer());}
    public void setTHIS_MAX_MP(double THIS_MAX_MP) {this.THIS_MAX_MP = THIS_MAX_MP; Sidebar.updateSidebar(getPlayer());}
    public void setHAS_MP (double HAS_MP){this.HAS_MP = HAS_MP; Sidebar.updateSidebar(getPlayer());}
    public void setLEVEL ( int LEVEL){this.LEVEL = LEVEL; Sidebar.updateSidebar(getPlayer());}
    public void setMAX_EXP (long MAX_EXP){this.MAX_EXP = MAX_EXP; Sidebar.updateSidebar(getPlayer());}

    public void setHAS_EXP (long HAS_EXP){
        this.HAS_EXP = HAS_EXP;
        for (int i = 0; this.HAS_EXP >= MAX_EXP; i++) {
            this.LEVEL += 1;
            this.THIS_MAX_HP += 5;
            this.THIS_MAX_MP += 5;
            getPlayer().sendMessage(TanoRPG.PX + "§aレベルが §b" + LEVEL + "Lv §aになりました！");
            this.HAS_EXP = this.HAS_EXP - MAX_EXP;
            MAX_EXP = Math.round(MAX_EXP * 1.1);
        }
        Sidebar.updateSidebar(getPlayer());
    }
    public void setStatuses(HashMap<StatusType, Status> statuses) { this.statuses = statuses;}

    public GamePlayer(UUID uuid, GamePlayerJobType job){
        this.uuid = uuid;
        this.job = job;
        this.name = Bukkit.getPlayer(uuid).getName();
        this.THIS_MAX_HP += job.getHP(); this.MAX_HP = THIS_MAX_HP; this.HAS_HP = MAX_HP;
        this.THIS_MAX_MP += job.getMP(); this.MAX_MP = THIS_MAX_MP; this.HAS_MP = MAX_MP;
        this.statuses = job.getStatuses();

    }
    public Status getStatus(StatusType statusType){
        final Status[] returnS = new Status[1];
        try{
            returnS[0] = new Status(statusType, 0);
            Player player = Bukkit.getPlayer(uuid);
            ArrayList<ItemStack> items = new ArrayList<>();
            items.add(player.getEquipment().getItemInMainHand());
            items.add(player.getEquipment().getHelmet());
            items.add(player.getEquipment().getChestplate());
            items.add(player.getEquipment().getLeggings());
            items.add(player.getEquipment().getBoots());
            int i = 0;
            for (ItemStack item : items) {
                i += 1;
                if (item == null) item = new ItemStack(Material.AIR);
                if (!item.getType().equals(Material.AIR)) {
                    if (CustomItemManager.getID(item).equals("")) continue;
                    if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)) == null) continue;
                    if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getStatuses() == null) continue;
                    CustomItemType cit = CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getCit();
                    if (cit.equals(CustomItemType.MATERIAL)) continue;
                    if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getJobs().contains(job)) {
                        if (i == 1){
                            if (cit.equals(CustomItemType.MAGIC_WEAPON) || cit.equals(CustomItemType.WEAPON)) {
                                for (Status status : CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getStatuses()) {
                                    if (status.getStatusType().equals(returnS[0].getStatusType())) {
                                        returnS[0].addLevel(status.getLevel());
                                    }
                                }
                            }
                        } else if (cit.equals(CustomItemType.EQUIPMENT)){
                            for (Status status : CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getStatuses()) {
                                if (status.getStatusType().equals(returnS[0].getStatusType())) {
                                    returnS[0].addLevel(status.getLevel());
                                }
                            }
                        }
                    }
                }
            }
            returnS[0].addLevel(statuses.get(statusType).getLevel());
        }catch (NullPointerException e){
            returnS[0].addLevel(0);
        }
        return returnS[0];
    }
    public ArrayList<Status> getStatus(){
        ArrayList<Status> statuses = new ArrayList<>();
        for (StatusType type : StatusType.values()){
            statuses.add(getStatus(type));
        }
        return statuses;
    }

    public boolean isProper(ItemStack item){
        try {
            if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getJobs().contains(getJob())){
                return true;
            }
        } catch (Exception e2){return false;}
        return false;
    }
    public boolean isLv(ItemStack item){
        try {
            if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getLvl() <= getLEVEL()){
                return true;
            }
        } catch (Exception e2){return false;}
        return false;
    }
    public boolean isCIT(ItemStack item, CustomItemType cit){
        try {
            if (CustomItemManager.getCustomItem(CustomItemManager.getID(item)).getCit().equals(cit)){
                return true;
            }
        } catch (Exception e2){return false;}
        return false;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public void addSkill(String name){
        if (skills.contains(name)) return;
        skills.add(name);
    }
    public boolean hasSkill(String name) {
        return (skills.contains(name)) ? true : false;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return
                Double.compare(that.THIS_MAX_HP, THIS_MAX_HP) == 0 &&
                Double.compare(that.MAX_HP, MAX_HP) == 0 &&
                Double.compare(that.HAS_HP, HAS_HP) == 0 &&
                Double.compare(that.MAX_MP, MAX_MP) == 0 &&
                Double.compare(that.THIS_MAX_MP, THIS_MAX_MP) == 0 &&
                Double.compare(that.HAS_MP, HAS_MP) == 0 &&
                LEVEL == that.LEVEL &&
                MAX_EXP == that.MAX_EXP &&
                HAS_EXP == that.HAS_EXP &&
                job == that.job &&
                Objects.equals(name, that.name) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(statuses, that.statuses) &&
                Objects.equals(skills, that.skills);
    }
    public int hashCode() {
        return Objects.hash(job, name, uuid, THIS_MAX_HP, MAX_HP, HAS_HP, MAX_MP, THIS_MAX_MP, HAS_MP, LEVEL, MAX_EXP, HAS_EXP, statuses, skills);
    }

}