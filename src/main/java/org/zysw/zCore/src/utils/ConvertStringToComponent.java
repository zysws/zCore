package org.zysw.zCore.src.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.zysw.zCore.src.ZCore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertStringToComponent {
    private static final ConvertStringToComponent INSTANCE = new ConvertStringToComponent();

    // Pattern updated to also match hex colors like <#FFFFFF>
    private static final Pattern TAG_PATTERN = Pattern.compile("<(#?[a-zA-Z0-9_-]+)>");
    private static final Map<String, NamedTextColor> COLOR_MAP = new HashMap<>();

    // global variables stored as strings
    private static final Map<String, String> globalVariables = new HashMap<>();

    static {
        for (NamedTextColor color : NamedTextColor.NAMES.values()) {
            COLOR_MAP.put(color.toString().toLowerCase(), color);
        }

        globalVariables.put("server-name", (String) ZCore.getInstance().getConfig().get("global.variables.server-name"));
    }

    // set a global variable as a string
    public static void setGlobalVariable(String key, String value) {
        globalVariables.put(key.toLowerCase(), value);
    }

    // convert a string with string placeholders to a component
    public static Component convert(String input, Map<String, String> placeholders) {
        Component result = Component.empty();
        TextColor currentColor = NamedTextColor.WHITE;

        if (input == null) return result;

        // merge global variables and local placeholders (local overrides globals)
        Map<String, String> combinedPlaceholders = new HashMap<>();
        if (globalVariables != null) combinedPlaceholders.putAll(globalVariables);
        if (placeholders != null) combinedPlaceholders.putAll(placeholders);

        Matcher matcher = TAG_PATTERN.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBefore = input.substring(lastEnd, matcher.start());
            if (!textBefore.isEmpty()) {
                result = result.append(Component.text(textBefore, currentColor));
            }

            String tag = matcher.group(1);

            if (tag.startsWith("#") && tag.length() == 7) {
                // Hex color support
                try {
                    currentColor = TextColor.fromHexString(tag);
                } catch (IllegalArgumentException e) {
                    // Invalid hex, ignore or fallback
                }
            } else {
                String lowerTag = tag.toLowerCase();

                if (COLOR_MAP.containsKey(lowerTag)) {
                    currentColor = COLOR_MAP.get(lowerTag);
                } else if (combinedPlaceholders.containsKey(lowerTag)) {
                    // recursively parse placeholder value
                    String value = combinedPlaceholders.get(lowerTag);
                    Component valueComponent = convert(value, combinedPlaceholders);
                    if (valueComponent.color() == null) {
                        valueComponent = valueComponent.color(currentColor);
                    }
                    result = result.append(valueComponent);
                } else {
                    result = result.append(Component.text("<" + tag + ">", currentColor));
                }
            }

            lastEnd = matcher.end();
        }

        String remaining = input.substring(lastEnd);
        if (!remaining.isEmpty()) {
            result = result.append(Component.text(remaining, currentColor));
        }

        return result;
    }

    // convert a string with component placeholders to a component
    public static Component convertWithComponents(String input, Map<String, Component> placeholders) {
        Component result = Component.empty();
        TextColor currentColor = NamedTextColor.WHITE;

        if (input == null) return result;

        // convert global variables strings to components on the fly
        Map<String, Component> globalComponents = new HashMap<>();
        for (Map.Entry<String, String> entry : globalVariables.entrySet()) {
            globalComponents.put(entry.getKey(), convert(entry.getValue(), null));
        }

        // merge global components and local placeholders (local overrides globals)
        Map<String, Component> combinedPlaceholders = new HashMap<>(globalComponents);
        if (placeholders != null) combinedPlaceholders.putAll(placeholders);

        Matcher matcher = TAG_PATTERN.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            String textBefore = input.substring(lastEnd, matcher.start());
            if (!textBefore.isEmpty()) {
                result = result.append(Component.text(textBefore, currentColor));
            }

            String tag = matcher.group(1);

            if (tag.startsWith("#") && tag.length() == 7) {
                // Hex color support
                try {
                    currentColor = TextColor.fromHexString(tag);
                } catch (IllegalArgumentException e) {
                    // Invalid hex, ignore or fallback
                }
            } else {
                String lowerTag = tag.toLowerCase();

                if (COLOR_MAP.containsKey(lowerTag)) {
                    currentColor = COLOR_MAP.get(lowerTag);
                } else if (combinedPlaceholders.containsKey(lowerTag)) {
                    Component comp = combinedPlaceholders.get(lowerTag);
                    if (comp.color() == null) comp = comp.color(currentColor);
                    result = result.append(comp);
                } else {
                    result = result.append(Component.text("<" + tag + ">", currentColor));
                }
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
