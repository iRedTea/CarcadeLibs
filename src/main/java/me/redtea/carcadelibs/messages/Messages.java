package me.redtea.carcadelibs.messages;

import lombok.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Messages {

    private final Component DEFAULT_NULLABLE_MESSAGE = Component.text("");

    private @Setter
    Component prefix;

    MiniMessage mm = MiniMessage.miniMessage();
    private @Setter
    @NonNull Map<String, Component> messages = new HashMap<>();
    private @Setter Component nullable;

    public Messages(ConfigurationSection section) {
        init(section);
    }
    
    public void init(ConfigurationSection section) {
        try {
            prefix = mm.deserialize(Objects.requireNonNull(section.getString("prefix")));
        } catch (NullPointerException e) {
            prefix = null;
        }

        val data = fromConfigurationToMap(section);
        messages.putAll(data);
    }


    private Map<String, Component> fromConfigurationToMap(@NonNull ConfigurationSection section) {
        Map<String, Component> data = new HashMap<>();

        section.getKeys(false).forEach(key -> {
            if (section.isConfigurationSection(key)) {
                Map<String, Component> newMessages = fromConfigurationToMap(section.getConfigurationSection(key));
                newMessages.forEach((keyMessage, message) -> data.put(key + "." + keyMessage, message));
            } else {
                Component message = null;
                if (section.isString(key)) {
                    message = mm.deserialize(section.getString(key));
                } else if (section.isList(key)) {
                    Component listComponent = Component.text("");
                    section.getStringList(key).forEach((s) -> {
                        listComponent.append(mm.deserialize(s));
                    });
                    message = listComponent;
                }

                if (message != null) {
                    data.put(key, message);
                }
            }
        });

        return data;
    }

    public boolean has(@NonNull String key) {
        return this.messages.containsKey(key);
    }

    public Component get(@NonNull String key) {
        val message = this.messages.get(key);
        if (message == null) {
            return nullable != null ? nullable : DEFAULT_NULLABLE_MESSAGE;
        }
        if(prefix != null) return message.append(prefix);
        return message;
    }

    public Component get(@NonNull String key, boolean withPrefix) {
        val message = this.messages.get(key);
        if (message == null) {
            return nullable != null ? nullable : DEFAULT_NULLABLE_MESSAGE;
        }
        if(prefix != null && withPrefix) return message.append(prefix);
        return message;
    }

    public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(get("no-permission"));
            return false;
        }
        return true;
    }


    public void put(@NonNull String key, @NonNull Component message) {
        this.messages.put(key, message);
    }

    public void putIfAbsent(@NonNull String key, @NonNull Component message) {
        this.messages.putIfAbsent(key, message);
    }

    public boolean hasPrefix() {
        return prefix != null;
    }
}
