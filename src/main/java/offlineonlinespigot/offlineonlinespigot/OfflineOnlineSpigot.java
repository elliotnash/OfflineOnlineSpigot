package offlineonlinespigot.offlineonlinespigot;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import protocolsupport.api.events.PlayerLoginFinishEvent;
import protocolsupport.api.events.PlayerLoginStartEvent;

import java.util.List;

public final class OfflineOnlineSpigot extends JavaPlugin implements Listener {
    public static FileConfiguration config;
    public static List<String> allowedUsers;
    public static BukkitAudiences bukkitAudiences;
    public static Plugin plugin;
    public static Boolean shouldRestart = false;
    @Override
    public void onEnable() {

        //set static vars
        config = this.getConfig();
        bukkitAudiences = BukkitAudiences.create(this);
        plugin = this;


        //if config empty save default
        if(!config.isSet("allowedPlayers")){
            this.saveDefaultConfig();
        }
        allowedUsers = config.getStringList("allowedPlayers");

        System.out.println(allowedUsers.isEmpty());

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getCommand("oosreload").setExecutor(new OosReloadListener());
        getCommand("later").setExecutor(new LaterListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLoginStart(final PlayerLoginStartEvent event) {
        if (allowedUsers.contains(event.getConnection().getProfile().getName()))
            event.setOnlineMode(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(shouldRestart && getServer().getOnlinePlayers().isEmpty()){
                    getServer().spigot().restart();
                }
            }
        });
    }
}
