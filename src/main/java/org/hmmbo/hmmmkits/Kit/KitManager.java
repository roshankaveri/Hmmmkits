package org.hmmbo.hmmmkits.Kit;


import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public  class KitManager {

    private File configFile;
    private YamlConfiguration config;

    public KitManager(File configFile) {
        this.configFile = configFile;
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveKit(Kit kit) {
        String n = "kit."+ kit.getName().toLowerCase()+ ".";
        config = YamlConfiguration.loadConfiguration(configFile);
        if(configFile == null) {
           // System.out.println("Null da");
        }else{
          //  System.out.println("Not Null");
        }
        config.set(n+"cooldown", kit.getCooldown());
        config.set(n+"permission", kit.getPermission());
        int k=0;
        for(ItemStack i : kit.getContents()){
            if(i!= null) {
                config.set(n+"contents."+k++, i.serialize());
            }
        }
        config.set(n+"display",kit.getDisplay());
       // kitSection.set("sound",kit.getSound().toString());
        config.set(n+"weigth",kit.getWeight());
        try {
            config.save(configFile);
           // System.out.println("saved");
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public Kit loadKit(String kitName) {
        kitName = kitName.toLowerCase();
        String n = "kit."+kitName+".";
        if (config.contains("kit." + kitName)) {
            Long cooldown = config.getLong(n+"cooldown");
            String permission = config.getString(n+"permission");
            List<ItemStack> contents = new ArrayList<>();
            for (String key : config.getConfigurationSection(n+"contents").getKeys(false)) {
             //   System.out.println("Worsk" + key);
               // System.out.println(config.getConfigurationSection(key));
                ItemStack item = ItemStack.deserialize(Objects.requireNonNull(config.getConfigurationSection(n+"contents."+key)).getValues(false));
            contents.add(item);
            }
            ItemStack display = config.getItemStack(n+"display");
           // Sound sound = Sound.valueOf( config.getString(n+"sound"));
            Integer w = config.getInt(n+"weigth");

            return new Kit(kitName, cooldown, permission, contents, display, Sound.ITEM_GOAT_HORN_SOUND_0, w);
        }

        return null;
    }
}

