package org.zysw.zCore.src.core;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.regex.Pattern;

public class AutoFormatChat implements Listener {
    private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("&([0-9a-fA-F])");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    @EventHandler
    public void OnChat(AsyncChatEvent chatEvent) {
        String message = PlainTextComponentSerializer.plainText().serialize(chatEvent.message());

        message = HEX_COLOR_PATTERN.matcher(message).replaceAll(matchResult -> {
            String hex = matchResult.group(1);
            return "<#" + hex + ">";
        });

        message = LEGACY_COLOR_PATTERN.matcher(message).replaceAll(matchResult -> {
            char code = matchResult.group(1).toLowerCase().charAt(0);
            NamedTextColor color = convertLegacyCodeToNamedColor(code);
            if (color != null) {
                return "<" + color.toString().toLowerCase() + ">";
            }
            return "";
        });

        Component coloredMessage = ConvertStringToComponent.convert(message, null);
        chatEvent.message(coloredMessage);
    }

    private NamedTextColor convertLegacyCodeToNamedColor(char code) {
        switch (code) {
            case '0':
                return NamedTextColor.BLACK;
            case '1':
                return NamedTextColor.DARK_BLUE;
            case '2':
                return NamedTextColor.DARK_GREEN;
            case '3':
                return NamedTextColor.DARK_AQUA;
            case '4':
                return NamedTextColor.DARK_RED;
            case '5':
                return NamedTextColor.DARK_PURPLE;
            case '6':
                return NamedTextColor.GOLD;
            case '7':
                return NamedTextColor.GRAY;
            case '8':
                return NamedTextColor.DARK_GRAY;
            case '9':
                return NamedTextColor.BLUE;
            case 'a':
                return NamedTextColor.GREEN;
            case 'b':
                return NamedTextColor.AQUA;
            case 'c':
                return NamedTextColor.RED;
            case 'd':
                return NamedTextColor.LIGHT_PURPLE;
            case 'e':
                return NamedTextColor.YELLOW;
            case 'f':
                return NamedTextColor.WHITE;
            default:
                return null;
        }
    }
}
