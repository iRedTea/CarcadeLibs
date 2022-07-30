package me.redtea.carcadelibs.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.UnaryOperator;

@Getter
@AllArgsConstructor
public abstract class Message {

    protected final Messages messages;

    public abstract void sendMessage(@NonNull CommandSender sender);

    public abstract void sendMessage(@NonNull CommandSender sender, boolean enablePrefix);

    public abstract void sendMessage(@NonNull CommandSender sender, @NonNull UnaryOperator<String> apply);

    public abstract void sendMessage(@NonNull CommandSender sender, boolean enablePrefix, @NonNull UnaryOperator<String> apply);

    public abstract String toString();

    public abstract List<String> toList();
}
