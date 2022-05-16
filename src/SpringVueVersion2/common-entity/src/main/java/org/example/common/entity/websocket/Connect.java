package org.example.common.entity.websocket;

import java.io.Serializable;

public class Connect implements Serializable {
    private String sourceUserName;
    private String targetUserName;

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }
}
