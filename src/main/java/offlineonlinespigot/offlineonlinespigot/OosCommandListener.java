package offlineonlinespigot.offlineonlinespigot;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static offlineonlinespigot.offlineonlinespigot.OfflineOnlineSpigot.*;

public class OosCommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!sender.hasPermission("oos.reload")) return true;
        //reload config
        plugin.reloadConfig();
        config = plugin.getConfig();

        if(!OfflineOnlineSpigot .config.isSet("allowedPlayers")){
            plugin.saveDefaultConfig();
        }
        allowedUsers = config.getStringList("allowedPlayers");

        Audience audience;
        if (sender instanceof Player)
            audience = bukkitAudiences.player((Player) sender);
        else
            audience = bukkitAudiences.console();

        if (allowedUsers.isEmpty()){
            audience.sendMessage(Component.text("Config reloaded\nNo players are allowed").color(TextColor.color(0xEE1010)));
            return true;
        }

        TextComponent.Builder builder = Component.text().content("Config reloaded\nAllowed players:").color(TextColor.color(0x6565a6));
        for (String name : allowedUsers){
            builder.append(Component.text("\n"+name).color(TextColor.color(0xb5b5f5)));
        }

        audience.sendMessage(builder.build());
        return true;
    }
}
