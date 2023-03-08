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
    private HashMap<StatusType, Double> pointStatuses = new HashMap<>();

    private Member member;

    private int statusPoint = 0;

    public PlayerStatusMap(Member member) {
        this.member = member;
        pointStatuses.put(StatusType.HP, 0D);
        pointStatuses.put(StatusType.MP, 0D);
        pointStatuses.put(StatusType.ATK, 0D);
        pointStatuses.put(StatusType.STR, 0D);
        pointStatuses.put(StatusType.INT, 0D);
        pointStatuses.put(StatusType.DEF, 0D);
        pointStatuses.put(StatusType.CRITICAL, 0D);
        pointStatuses.put(StatusType.CRITICAL_RATE, 0D);
        pointStatuses.put(StatusType.SUS, 0D);
    }

    @Override
    public double getStatus(StatusType status) {
        double r = getHasStatuses().get(status) == null ? 0.0 : getHasStatuses().get(status);
        if (member != null) r += member.getEquip().getStatus(status);
        return r;
    }

    public void setPointStatus(StatusType status, double value) {
        if (!StatusType.getBasicStatus().contains(status))
            throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, value);
    }

    public void setPointStatuses(HashMap<StatusType, Double> pointStatuses) {
        this.pointStatuses = pointStatuses;
    }

    public void setStatusPoint(int statusPoint) {
        this.statusPoint = statusPoint;
    }

    public void addPointStatus(StatusType status, int value) {
        if (!StatusType.getBasicStatus().contains(status))
            throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, pointStatuses.get(status) + value);
    }

    public void removePointStatus(StatusType status, int value) {
        if (!StatusType.getBasicStatus().contains(status))
            throw new IllegalArgumentException("基本ステータスではありません ->" + status.getName());
        pointStatuses.put(status, pointStatuses.get(status) - value);
    }

    public double getPointStatus(StatusType status) {
        if (!StatusType.getBasicStatus().contains(status)) return 0;
        return pointStatuses.get(status);
    }

    public double getPointAndStatus(StatusType status) {
        return getPointStatus(status) + getStatus(status);
    }

    public int getStatusPoint() {
        return statusPoint;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void save(Config config, String key) {
        Gson gson = new Gson();
        config.getConfig().set(key + "status.point", Coding.encode(gson.toJson(pointStatuses)));
        config.getConfig().set(key + "status.all", Coding.encode(gson.toJson(getHasStatuses())));
        config.getConfig().set(key + "status.statusPoint", statusPoint);
        config.saveConfig();
    }

    @Override
    public PlayerStatusMap load(Config config, String key) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<StatusType, Double>>() {}.getType();
        pointStatuses = gson.fromJson(Coding.decode(config.getConfig().getString(key + "status.point")), type);
        setHasStatuses(gson.fromJson(Coding.decode(config.getConfig().getString(key + "status.all")), type));
        statusPoint = config.getConfig().getInt(key + "status.statusPoint", 0);
        return this;
    }
}
