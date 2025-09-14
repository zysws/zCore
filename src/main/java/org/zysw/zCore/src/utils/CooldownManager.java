package org.zysw.zCore.src.utils;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private static final CooldownManager INSTANCE = new CooldownManager();
    private final Map<String, Long> cooldowns = new HashMap<>();

    // check if a command is on cooldown
    public boolean isOnCooldown(String command) {
        long currentTime = System.currentTimeMillis();

        if (!cooldowns.containsKey(command)) return false;

        long expiresAt = cooldowns.get(command);
        return currentTime < expiresAt;
    }

    // set a cooldown for a command (in seconds)
    public void setOnCooldown(String command, int seconds) {
        long expiresAt = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(command, expiresAt);
    }

    // optionally, get time left on cooldown (in seconds)
    public long getCooldownTimeLeft(String command) {
        if (!cooldowns.containsKey(command)) return 0;

        long timeLeft = cooldowns.get(command) - System.currentTimeMillis();
        return Math.max(timeLeft / 1000, 0);
    }

    public static CooldownManager getInstance() {
        return INSTANCE;
    }
}
