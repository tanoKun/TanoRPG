package com.github.tanokun.tanorpg.game.entity.base;

import com.github.tanokun.tanorpg.game.entity.EntityDropItems;
import com.github.tanokun.tanorpg.game.entity.EntityTypes;
import com.github.tanokun.tanorpg.game.entity.exception.TanoEntityException;
import com.github.tanokun.tanorpg.game.item.ItemManager;
import com.github.tanokun.tanorpg.game.item.itemtype.base.Item;
import com.github.tanokun.tanorpg.game.player.status.Status;
import com.github.tanokun.tanorpg.game.player.status.StatusType;
import com.github.tanokun.tanorpg.util.io.Config;
import com.github.tanokun.tanorpg.util.io.MapNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectEntity {
    private Config entityConfig;

    private EntityTypes entityTypes;
    private String name;
    private int HP;
    private int LEVEL;
    private long EXP;

    private double speed = 1;

    private String mainHand = "";
    private String offHand = "";
    private String helmet = "";
    private String chestPlate = "";
    private String leggings = "";
    private String boots = "";

    private EntityDropItems dropItems;

    private Map<StatusType, Status> statuses = new HashMap<>();

    public abstract Entity spawn(Location location);
    public abstract Entity setOptions(Entity entity);

    public ObjectEntity(Config config) throws TanoEntityException {
        MapNode<String, Object> data = null;
        try {
            data = ItemManager.get("DisplayName", config);
            if (data.getValue() != null) {name = (String) data.getValue();} else {throw new NullPointerException("エンティティ名が設定されていません");}
            try {
                data = ItemManager.get("BaseOptions.HP", config);
                if (data.getValue() != null) {HP = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("HPが設定されていません");}
                data = ItemManager.get("BaseOptions.LEVEL", config);
                if (data.getValue() != null) {LEVEL = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("LEVELが設定されていません");}
                data = ItemManager.get("BaseOptions.EXP", config);
                if (data.getValue() != null) {EXP = Integer.valueOf((String) data.getValue());} else {throw new NullPointerException("EXPが設定されていません");}
                if (config.getConfig().isSet("BaseOptions.Speed")) {
                    data = ItemManager.get("BaseOptions.Speed", config);
                    if (data.getValue() != null) {
                        speed = Integer.valueOf((String) data.getValue());
                    }
                }
            } catch (NumberFormatException e){
                throw new NumberFormatException("「" + data.getValue() + "」は数字で入力して下さい");
            }

            int ATK = 0;
            int DEF = 0;
            int MATK = 0;
            int MDEF = 0;
            int AGI = 0;
            int ING = 0;
            int INT = 0;
            data.setKey("BaseStatus.*");
            if (config.getConfig().isSet("BaseStatus")) {
                for (String key : config.getConfig().getConfigurationSection("BaseStatus").getKeys(false)) {
                    if (key.equalsIgnoreCase("atk")) {
                        ATK = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("def")) {
                        DEF = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("matk")) {
                        MATK = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("mdef")) {
                        MDEF = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("agi")) {
                        AGI = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("ing")) {
                        ING = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                    if (key.equalsIgnoreCase("int")) {
                        INT = config.getConfig().getInt("BaseStatus." + key, 0);
                    }
                }
            }
            setStatuses(ATK, DEF, MATK, MDEF, AGI, ING, INT);

            String mainHand = "";
            String offHand = "";
            String helmet = "";
            String chestPlate = "";
            String leggings = "";
            String boots = "";
            data.setKey("Armor.*");
            if (config.getConfig().isSet("Armor")) {
                if (config.getConfig().getConfigurationSection("Armor").getKeys(false) != null) {
                    for (String key : config.getConfig().getConfigurationSection("Armor").getKeys(false)) {
                        if (key.equals("main")) {
                            mainHand = config.getConfig().getString("Armor." + key);
                        }
                        if (key.equals("sub")) {
                            offHand = config.getConfig().getString("Armor." + key);
                        }
                        if (key.equals("helmet")) {
                            helmet = config.getConfig().getString("Armor." + key);
                        }
                        if (key.equals("chestplate")) {
                            chestPlate = config.getConfig().getString("Armor." + key);
                        }
                        if (key.equals("leggings")) {
                            leggings = config.getConfig().getString("Armor." + key);
                        }
                        if (key.equals("boots")) {
                            boots = config.getConfig().getString("Armor." + key);
                        }
                    }
                }
            }
            setArmors(mainHand, offHand, helmet, chestPlate, leggings, boots);
            EntityDropItems customEntityDropItems = new EntityDropItems();
            if (config.getConfig().isSet("Armor")) {
                ArrayList<String> drops = (ArrayList<String>) config.getConfig().getList("Drops");
                for (String drop : drops) {
                    String[] temp = drop.split("@");
                    double percent = Integer.valueOf(temp[1]);
                    Item ci = ItemManager.getItem(temp[0]);
                    customEntityDropItems.addItem(ci, percent);
                }
            }
            setDropItems(customEntityDropItems);
        }catch (Exception e){
            throw new TanoEntityException(e.getMessage(), data);
        }
    }

    public void setArmors(String mainHand, String offHand, String helmet, String chestPlate, String leggings, String boots) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestPlate = chestPlate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public EntityTypes getEntityTypes() {
        return entityTypes;
    }
    public String getName() {
        return name;
    }
    public Config getEntityConfig() {
        return entityConfig;
    }
    public int getHP() {
        return HP;
    }
    public int getLEVEL() {
        return LEVEL;
    }
    public long getEXP() {
        return EXP;
    }
    public Map<StatusType, Status> getStatuses() {
        return statuses;
    }
    public double getSpeed() {
        return 0.25 * speed;
    }

    public void setEntityTypes(EntityTypes entityTypes) {
        this.entityTypes = entityTypes;
    }
    public void setEntityConfig(Config entityConfig) {
        this.entityConfig = entityConfig;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHP(int HP) {
        this.HP = HP;
    }
    public void setLEVEL(int LEVEL) {
        this.LEVEL = LEVEL;
    }
    public void setEXP(long EXP) {
        this.EXP = EXP;
    }
    public void setStatuses(double i, double i2, double i3, double i4, double i5, double i6, double i7){
        statuses.put(StatusType.ATK, new Status(StatusType.ATK, i));
        statuses.put(StatusType.DEF, new  Status(StatusType.DEF, i2));
        statuses.put(StatusType.MATK, new Status(StatusType.MATK, i3));
        statuses.put(StatusType.MDEF, new Status(StatusType.MDEF, i4));
        statuses.put(StatusType.AGI, new Status(StatusType.AGI, i5));
        statuses.put(StatusType.ING, new Status(StatusType.ING, i6));
        statuses.put(StatusType.INT, new Status(StatusType.INT, i7));
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDropItems(EntityDropItems dropItems) {
        this.dropItems = dropItems;
    }

    public EntityDropItems getDropItems() {
        return dropItems;
    }

    public double getATK() {return statuses.get(StatusType.ATK).getLevel();}
    public double getDEF() {return statuses.get(StatusType.DEF).getLevel();}
    public double getMATK() {return statuses.get(StatusType.MATK).getLevel();}
    public double getMDEF() {return statuses.get(StatusType.MDEF).getLevel();}
    public double getAGI() {return statuses.get(StatusType.AGI).getLevel();}
    public double getING() {return statuses.get(StatusType.ING).getLevel();}
    public double getINT() {return statuses.get(StatusType.INT).getLevel();}

    public String getMainHand() {return mainHand;}
    public String getOffHand() {return offHand;}
    public String getHelmet() {return helmet;}
    public String getChestPlate() {return chestPlate;}
    public String getLeggings() {return leggings;}
    public String getBoots() {return boots;}
}