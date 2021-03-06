package me.dtbcds.carrier;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private int slownessLevel = 0;
    private float hungerExhaustion = 0f;
    private ListType type = ListType.BLACKLIST;
    private List<String> list = new ArrayList<>();

    public Config() {}

    public List<String> getList() {
        return list;
    }

    public ListType getType() {
        return type;
    }

    public float getHungerExhaustion() {
        return hungerExhaustion;
    }

    public boolean doGlovesExist() { return false; }

    public int getSlownessLevel() {
        return slownessLevel;
    }

    public enum ListType {
        WHITELIST, BLACKLIST
    }
}
