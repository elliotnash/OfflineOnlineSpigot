package offlineonlinespigot.offlineonlinespigot;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import static offlineonlinespigot.offlineonlinespigot.OfflineOnlineSpigot.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LaterListener implements CommandExecutor, TabCompleter {

    TextComponent restartSet = Component.text("Restart scheduled").color(TextColor.color(0x6565a6));
    TextComponent alreadySet = Component.text("Restart was already scheduled").color(TextColor.color(0xEE1010));
    TextComponent restartCancelled = Component.text("Restart cancelled").color(TextColor.color(0x6565a6));
    TextComponent alreadyCancelled = Component.text("No restart was scheduled").color(TextColor.color(0xEE1010));


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("oos.restart")) return true;

        if (args.length==0){

            Audience audience;
            if (sender instanceof Player)
                audience = bukkitAudiences.player((Player) sender);
            else
                audience = bukkitAudiences.console();

            if (!shouldRestart)
                audience.sendMessage(restartSet);
            else
                audience.sendMessage(alreadySet);

            shouldRestart = true;

        }
        if (args.length==1 && args[0].equals("cancel")){

            Audience audience;
            if (sender instanceof Player)
                audience = bukkitAudiences.player((Player) sender);
            else
                audience = bukkitAudiences.console();

            if (shouldRestart)
                audience.sendMessage(restartCancelled);
            else
                audience.sendMessage(alreadyCancelled);

            shouldRestart = false;

        } else return false;

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        LinkedList<String> commands = new LinkedList<String>();
        final LinkedList<String> completions = new LinkedList<String>();
        if (args.length == 1 && sender.hasPermission("oos.restart")) {
            commands = new LinkedList<String>(Collections.singletonList("cancel"));
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
