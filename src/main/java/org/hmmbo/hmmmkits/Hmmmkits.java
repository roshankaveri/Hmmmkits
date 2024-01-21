package org.hmmbo.hmmmkits;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.hmmbo.hmmmkits.commands.KitCommands;
import org.hmmbo.hmmmkits.commands.TabCompleter;
import org.hmmbo.hmmmkits.inv.KitInv;
import org.hmmbo.hmmmkits.inv.PrevInv;
import org.hmmbo.hmmmkits.utils.AdvancedLicense;
import org.hmmbo.hmmmkits.utils.KitUtils;

import java.io.File;
import java.util.Objects;

public final class Hmmmkits extends JavaPlugin {

    @Override
    public void onEnable() {
        Thread t = new Thread(()->{
            if(!new AdvancedLicense("KIT-VERSION-1-0","https://mineplug.000webhostapp.com/verify.php",this).register()) {
                getLogger().info("----------------------Plugin Is Disabled By Developer------------------------");
                getLogger().info("----------------------Plugin Is Disabled By Developer------------------------");
                getLogger().info("----------------------Plugin Is Disabled By Developer------------------------");
                onDisable();
            }
        });

     //   if(!new AdvancedLicense("KIT-VERSION-1-0","https://mineplug.000webhostapp.com/verify.php",this).register()) return;
        getConfig().options().copyDefaults();
        getLogger().info("--------------------------------");
        getLogger().info("--------Loading Hmmkits---------".replace('-',' '));
        getLogger().info("-------Hmmbo Development--------".replace('-',' '));
        getLogger().info("--------------------------------");
        File qfile = new File(getDataFolder(), "config.yml");
        if (!qfile.exists()) {
            saveResource("config.yml", false);
        }
        File ff = new File(getDataFolder(), "cooldown.yml");
        if (!ff.exists()) {
            saveResource("cooldown.yml", false);
        }
        KitUtils.loadKitCooldowns(ff);
        KitCommands k = new KitCommands(qfile);
        Bukkit.getPluginManager().registerEvents(new KitInv(qfile),this);
        Bukkit.getPluginManager().registerEvents(new PrevInv(qfile),this);
        Objects.requireNonNull(getCommand("kit")).setExecutor(k);
        getCommand("kit").setTabCompleter(new TabCompleter());
        t.start();
    }

    @Override
    public void onDisable() {
        File ff = new File(getDataFolder(), "cooldown.yml");
        if (ff.exists()) {
            KitUtils.saveKitCooldowns(ff);
        }

    }
}
