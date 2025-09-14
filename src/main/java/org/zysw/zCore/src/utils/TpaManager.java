package org.zysw.zCore.src.utils;

import java.util.HashMap;
import java.util.UUID;

public class TpaManager {
    // key = target player UUID, value = requester player UUID
    private static final HashMap<UUID, UUID> pendingRequests = new HashMap<>();

    public static void addRequest(UUID target, UUID requester) {
        pendingRequests.put(target, requester);
    }

    public static UUID getRequester(UUID target) {
        return pendingRequests.get(target);
    }

    public static void removeRequest(UUID target) {
        pendingRequests.remove(target);
    }

    public static boolean hasRequest(UUID target) {
        return pendingRequests.containsKey(target);
    }
}

