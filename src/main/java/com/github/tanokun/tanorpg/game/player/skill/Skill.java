package com.github.tanokun.tanorpg.game.player.skill;

import com.github.tanokun.tanorpg.game.player.GamePlayerJobType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Skill {

    private final String name;
    private final int lvl;
    private final int mp;
    private final ArrayList<String> combo;
    private final int ct;
    private final ArrayList<String> lore;
    private final ArrayList<GamePlayerJobType> job;
    private final Material item;

    public Skill(String name, int lvl, int mp, int ct, ArrayList<String> combo, ArrayList<String> lore, ArrayList<GamePlayerJobType> jobs, Material item){
        this.name = name;
        this.lvl = lvl;
        this.mp = mp;
        this.ct = ct;
        this.combo = combo;
        this.lore = lore;
        this.job = jobs;
        this.item = item;
    }

    public abstract void execute(Entity entity);

    public String getName() {return name;}
    public ArrayList<String> getCombo() {return combo;}
    public int getMp() {return mp;}
    public int getLvl() {return lvl;}
    public int getCT() {return ct;}
    public ArrayList<String> getLore() {return lore;}
    public String getJob() {
        String returnS = null;
        for(GamePlayerJobType jobType : job){returnS = (returnS == null) ? jobType.getName() : returnS + ", " +  jobType.getName();}
        return returnS;
    }
    public ArrayList<GamePlayerJobType> getJobs() {return job;}
    public Material getItem() {return item;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return lvl == skill.lvl && mp == skill.mp && ct == skill.ct && Objects.equals(name, skill.name) && Objects.equals(combo, skill.combo) && Objects.equals(lore, skill.lore) && Objects.equals(job, skill.job) && item == skill.item;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lvl, mp, combo, ct, lore, job, item);
    }
}
