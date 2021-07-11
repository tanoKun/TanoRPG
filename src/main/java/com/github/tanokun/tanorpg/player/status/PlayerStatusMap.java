package com.github.tanokun.tanorpg.player.status;

import com.github.tanokun.tanorpg.player.Member;
import com.github.tanokun.tanorpg.util.SaveMarker;
import com.github.tanokun.tanorpg.util.io.Coding;
import com.github.tanokun.tanorpg.util.io.Config;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;

public class PlayerStatusMap extends StatusMap implements SaveMarker<PlayerStatusMap> {
    private HashMap<StatusType, Integer> pointStatuses = new HashMap<>();

    private int statusPoint = 0;

    public PlayerStatusMap(){
        pointStatuses.put(StatusType.HP, 0);
        pointStatuses.put(StatusType.MP, 0);
        pointStatuses.put(StatusType.ATK, 0);
        pointStatuses.put(StatusType.MATK, 0);
        pointStatuses.put(StatusType.DEF, 0);
        pointStatuses.put(StatusType.CRITICAL, 0);
        pointStatuses.put(StatusType.CRITICAL_RATE, 0);
        pointStatuses.put(StatusType.SUS, 0);
    }

    @Override
    public int getStatus(StatusType status){
        int r = getHasStatuses().get(status) == null ? 0 : getHasStatuses().get(status);
        return r;
    }

    public int getStatus(StatusType status, Member member){
        int r = getHasStatuses().get(status) == null ? 0 : getHasStatuses().get(status);
        return r;
    }

    public void setPointStatus(StatusType status, int value){
        if (!StatusType.getBasicStatus().contains(status)) throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, value);
    }

    public void setPointStatuses(HashMap<StatusType, Integer> pointStatuses) {
        this.pointStatuses = pointStatuses;
    }

    public void setStatusPoint(int statusPoint) {
        this.statusPoint = statusPoint;
    }

    public void addPointStatus(StatusType status, int value){
        if (!StatusType.getBasicStatus().contains(status)) throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, pointStatuses.get(status) + value);
    }

    public void removePointStatus(StatusType status, int value){
        if (!StatusType.getBasicStatus().contains(status)) throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, pointStatuses.get(status) - value);
    }

    public int getPointStatus(StatusType status){
        if (!StatusType.getBasicStatus().contains(status)) return 0;
        return pointStatuses.get(status);
    }

    public int getPointAndStatus(StatusType status){
        return getPointStatus(status) + getStatus(status);
    }

    public int getStatusPoint() {
        return statusPoint;
    }

    @Override
    public void save(Config config, String key) {
        Gson gson = new Gson();
        config.getConfig().set(key + "status.point", Coding.encode(gson.toJson(pointStatuses)));
        config.getConfig().set(key + "status.all", Coding.encode(gson.toJson(getHasStatuses())));
        config.saveConfig();
    }

    @Override
    public PlayerStatusMap load(Config config, String key) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<StatusType, Integer>>(){}.getType();
        pointStatuses = gson.fromJson(Coding.decode(config.getConfig().getString(key + "status.point")), type);
        setHasStatuses(gson.fromJson(Coding.decode(config.getConfig().getString(key + "status.all")), type));
        return this;
    }
}
