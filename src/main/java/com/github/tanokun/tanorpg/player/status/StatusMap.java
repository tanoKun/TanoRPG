package com.github.tanokun.tanorpg.player.status;

import java.util.HashMap;

public class StatusMap {
    private HashMap<StatusType, Integer> hasStatuses = new HashMap<>();

    public void setStatus(StatusType status, int value){
        hasStatuses.put(status,value);
    }

    public void addStatus(StatusType status, int value){
        if (hasStatuses.get(status) != null){
            hasStatuses.put(status, hasStatuses.get(status) + value);
        } else {
            hasStatuses.put(status, value);
        }
    }

    public void addAllStatus(HashMap<StatusType, Integer> statuses){
        for (StatusType status : statuses.keySet()) {
            int value = statuses.get(status);

            if (hasStatuses.get(status) != null) {
                hasStatuses.put(status, hasStatuses.get(status) + value);
            } else {
                hasStatuses.put(status, value);
            }
        }
    }

    public void removeStatus(StatusType status, int value){
        if (hasStatuses.get(status) != null){
            hasStatuses.put(status, hasStatuses.get(status) - value);
        } else {
            hasStatuses.put(status, -value);
        }
    }

    public void removeAllStatus(HashMap<StatusType, Integer> statuses){
        for (StatusType status : statuses.keySet()) {
            int value = statuses.get(status);

            if (hasStatuses.get(status) != null) {
                hasStatuses.put(status, hasStatuses.get(status) - value);
            } else {
                hasStatuses.put(status, -value);
            }
        }
    }

    public void clear() {
        hasStatuses.clear();
    }

    public int getStatus(StatusType status){
        return hasStatuses.get(status) == null ? 0 : hasStatuses.get(status);
    }

    public HashMap<StatusType, Integer> getHasStatuses() {
        return hasStatuses;
    }

    public void setHasStatuses(HashMap<StatusType, Integer> hasStatuses) {
        this.hasStatuses = hasStatuses;
    }
}
