package me.redtea.carcadelibs.util;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringUtil {
    @Getter
    private static StringUtil instance;

    public StringUtil() {
        instance = this;
    }

    private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");

    public String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public List<String> color(List<String> list) {
        return list.stream().map(this::color).collect(Collectors.toList());
    }

    public String numberFormat(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    public String numberFormat(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
    }

    public void sendMessage(CommandSender sender, String text) {
        sendMessage(sender, null, text);
    }

    public void sendMessage(CommandSender sender, String prefix, String text) {
        for (String line : text.split(";")) {

            if (line.startsWith("title:")) {
                if (sender instanceof Player) {
                    String[] args = line.split("title:")[1].split("%nl%");

                    String title = color(args[0].trim());
                    String subTitle = null;

                    if (args.length > 1) {
                        subTitle = color(args[1].trim());
                    }

                    Titles.sendTitle((Player) sender, 15, 60, 15, title, subTitle);
                }
            } else if (line.startsWith("actionbar:")) {
                if (sender instanceof Player) {
                    ActionBar.sendActionBar((Player) sender, color(line.split("actionbar:")[1]));
                }
            } else {
                sender.sendMessage(color((prefix != null ? prefix : "") + line));
            }
        }
    }
}
