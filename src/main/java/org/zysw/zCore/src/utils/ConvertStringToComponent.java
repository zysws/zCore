package org.zysw.zCore.src.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertStringToComponent {
    // Converts <color> tags and replaces custom placeholders like <cords>

    private static final ConvertStringToComponent INSTANCE = new ConvertStringToComponent();
    private static final Pattern TAG_PATTERN = Pattern.compile("<(\\w+)>");

    private static final Map<String, NamedTextColor> COLOR_MAP = new HashMap<>();

    static {
        for (NamedTextColor color : NamedTextColor.NAMES.values()) {
            COLOR_MAP.put(color.toString().toLowerCase(), color);
        }
    }

    public static Component convert(String input, Map<String, String> placeholders) {
        Component result = Component.empty();
        NamedTextColor currentColor = NamedTextColor.WHITE;

        Matcher matcher = TAG_PATTERN.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBefore = input.substring(lastEnd, matcher.start());
            if (!textBefore.isEmpty()) {
                result = result.append(Component.text(textBefore, currentColor));
            }

            String tag = matcher.group(1).toLowerCase();

            if (COLOR_MAP.containsKey(tag)) {
                currentColor = COLOR_MAP.get(tag);
            } else if (placeholders != null && placeholders.containsKey(tag)) {
                String value = placeholders.get(tag);
                result = result.append(Component.text(value, currentColor));
            } else {
                // If unknown tag, keep it as plain text
                result = result.append(Component.text("<" + tag + ">", currentColor));
            }

            lastEnd = matcher.end();
        }

        String remaining = input.substring(lastEnd);
        if (!remaining.isEmpty()) {
            result = result.append(Component.text(remaining, currentColor));
        }

        return result;
    }

    public static ConvertStringToComponent getInstance() {
        return INSTANCE;
    }
}
