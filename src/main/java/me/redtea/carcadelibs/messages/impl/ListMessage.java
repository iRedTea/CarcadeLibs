package me.redtea.carcadelibs.messages.impl;


import lombok.NonNull;
import lombok.val;
import me.redtea.carcadelibs.messages.Messages;
import me.redtea.carcadelibs.util.StringUtil;
import me.redtea.carcadelibs.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ListMessage extends Message {
    private StringUtil stringUtil = StringUtil.getInstance();

    private final @NonNull List<String> valueList;

    public ListMessage(@NonNull Messages messages, @NonNull List<String> valueList) {
        super(messages);
        this.valueList = valueList;
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender) {
        this.sendMessage(sender, false);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix) {
        this.sendMessage(sender, enablePrefix, message -> message);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, @NonNull UnaryOperator<String> apply) {
        this.sendMessage(sender, false, apply);
    }

    @Override
    public void sendMessage(@NonNull CommandSender sender, boolean enablePrefix, @NonNull UnaryOperator<String> apply) {
        val prefix = this.messages.getPrefix();

        this.valueList.forEach(message -> {
            val value = apply.apply(message);

            if (prefix != null && enablePrefix) {
                stringUtil.sendMessage(sender, prefix, value);
            } else {
                stringUtil.sendMessage(sender, value);
            }
        });
    }

    @Override
    public String toString() {
        return stringUtil.color(String.join("\n", this.valueList));
    }

    @Override
    public List<String> toList() {
        return new ArrayList<>(this.valueList);
    }
}
