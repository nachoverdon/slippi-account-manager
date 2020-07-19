package com.nachoverdon.sam.model;

import lombok.Data;

@Data
public class Account {
    private transient boolean isCurrent = false;
    private String uid;
    private String playKey;
    private String connectCode;
    private String displayName;
    private String latestVersion;

    @Override
    public String toString() {
        return displayName + "  |  " + connectCode + (isCurrent ? " (CURRENT)" : "");
    }

    public void setFields(Account account) {
        uid = account.getUid();
        playKey = account.getPlayKey();
        connectCode = account.getConnectCode();
        displayName = account.getDisplayName();
        latestVersion = account.getLatestVersion();
    }
}
