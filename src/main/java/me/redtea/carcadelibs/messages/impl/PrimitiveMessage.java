package me.redtea.carcadelibs.messages.impl;

import lombok.NonNull;
import lombok.val;
import me.redtea.ultimatereiatsu.messages.Message;
import me.redtea.ultimatereiatsu.messages.Messages;
import me.redtea.ultimatereiatsu.util.StringUtil;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class PrimitiveMessage extends Message {

    private final @NonNull String value;

    public PrimitiveMessage(@NonNull Messages messages, @NonNull String value) {
        super(messages);
        this.value = value;
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender) {
        this.sendMessage(sender, true);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix) {
        this.sendMessage(sender, enablePrefix, message -> message);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, @NonNull UnaryOperator<String> apply) {
        this.sendMessage(sender, true, apply);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix, UnaryOperator<String> apply) {
        val prefix = this.messages.getPrefix();
        val value = apply.apply(this.value);

        if (prefix != null && enablePrefix) {
            StringUtil.sendMessage(sender, prefix, value);
        } else {
            StringUtil.sendMessage(sender, value);
        }
    }

    @Override
    public String toString() {
        return StringUtil.color(this.value);
    }

    @Override
    public List<String> toList() {
        return new ArrayList<>(Arrays.asList(this.value.split("\n")));
    }
}
