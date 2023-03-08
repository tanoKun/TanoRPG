package com.github.tanokun.tanorpg.player.status;

import java.util.HashMap;

public class StatusMap {
    private HashMap<StatusType, Double> hasStatuses = new HashMap<>();

    public void setStatus(StatusType status, double value) {
        hasStatuses.put(status, value);
    }

    public void addStatus(StatusType status, double value) {
        hasStatuses.merge(status, value, Double::sum);
    }

    public void addAllStatuses(HashMap<StatusType, Double> statuses) {
        for (StatusType status : statuses.keySet()) {
            double value = statuses.get(status);

            hasStatuses.merge(status, value, Double::sum);
        }
    }

    public void removeStatus(StatusType status, double value) {
        if (hasStatuses.get(status) != null) {
            hasStatuses.put(status, hasStatuses.get(status) - value);
        } else {
            hasStatuses.put(status, -value);
        }
    }

    public double getStatus(StatusType status) {
        return hasStatuses.get(status) == null ? 0 : hasStatuses.get(status);
    }

    public HashMap<StatusType, Double> getHasStatuses() {
        return hasStatuses;
    }

    public void setHasStatuses(HashMap<StatusType, Double> hasStatuses) {
        this.hasStatuses = hasStatuses;
    }
}
