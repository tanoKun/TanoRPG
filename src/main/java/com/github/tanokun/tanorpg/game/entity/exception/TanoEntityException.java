package com.github.tanokun.tanorpg.game.entity.exception;

import com.github.tanokun.tanorpg.util.io.MapNode;

public class TanoEntityException extends RuntimeException {
    private MapNode<String, Object> mapNode;
    public TanoEntityException(String message, MapNode mapNode){
        super(message);
        this.mapNode = mapNode;
    }

    public MapNode<String, Object> getMapNode() {return mapNode;}
}
