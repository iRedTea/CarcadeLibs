package me.redtea.carcadelibs.messages.impl;

import lombok.NonNull;
import me.redtea.ultimatereiatsu.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public class NullableMessage extends Message {

    public NullableMessage() {
        super(null);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender) {

    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix) {

    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, @NonNull UnaryOperator<String> apply) {

    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix, @NonNull UnaryOperator<String> apply) {

    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public List<String> toList() {
        return Collections.emptyList();
    }
}
