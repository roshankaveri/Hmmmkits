package org.hmmbo.hmmmkits.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hmmbo.hmmmkits.Kit.Kit;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitUtils {
    public static Map<UUID, Map<String, Long>> kitCooldowns = new HashMap<>();

    public boolean isKitCooldownExpired(Player player, String kitName) {
        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();

        if (kitCooldowns.containsKey(playerId)) {
            Map<String, Long> playerCooldowns = kitCooldowns.get(playerId);
            if (playerCooldowns.containsKey(kitName)) {
                long cooldownTime = playerCooldowns.get(kitName);
                return currentTime > cooldownTime;
            }
        }

        return true;
    }

    public String showDelay(Player player, String kitName) {
        long currentTime = System.currentTimeMillis();
        UUID playerId = player.getUniqueId();

        if (kitCooldowns.containsKey(playerId)) {
            Map<String, Long> playerCooldowns = kitCooldowns.get(playerId);
            if (playerCooldowns.containsKey(kitName)) {
                long cooldownTime = playerCooldowns.get(kitName);
                long totalSeconds = (cooldownTime - currentTime) / 1000;

                long days = totalSeconds / 86400; // 86400 seconds in a day
                long hours = (totalSeconds % 86400) / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                return "Time Left: " +days+"d "+ hours + "h " + minutes + "m " + seconds + "s";
            }
        }

        return "";
    }

    public void setKitCooldown(Player player, String kitName, Kit kit) {
        long cooldownDuration = kit.getCooldown() * 1000; // Convert seconds to milliseconds
        long cooldownTime = System.currentTimeMillis() + cooldownDuration;
        UUID playerId = player.getUniqueId();

        kitCooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(kitName, cooldownTime);
    }

    public void giveKit(Player player, Kit kit) {
        UUID playerId = player.getUniqueId();

        if (isKitCooldownExpired(player, kit.getName())) {
            for (ItemStack item : kit.getContents()) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(item);
                } else {
                    player.getWorld().dropItem(player.getLocation(), item);
                }
            }
            player.sendMessage("[Hmmkits] §eReceived " + kit.getPermission().replace('&','§'));
            setKitCooldown(player, kit.getName(), kit);
        } else {
            player.sendMessage("[Hmmkits] §eClaim After "+ showDelay(player, kit.getName()) + ".");
        }
    }

    public static void saveKitCooldowns(File file) {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Map<String, Long>> entry : kitCooldowns.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            config.save(file);
          //  System.out.println("Saved :)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadKitCooldowns(File file) {
        if (!file.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            UUID playerId = UUID.fromString(key);
            // Use getConfigurationSection to get the section for the player
            ConfigurationSection playerSection = config.getConfigurationSection(key);

            if (playerSection != null) {
                // Convert the playerSection to a Map
                Map<String, Object> cooldownsMap = playerSection.getValues(false);
                Map<String, Long> cooldowns = new HashMap<>();

                // Iterate through the entries and convert values to Long
                for (Map.Entry<String, Object> entry : cooldownsMap.entrySet()) {
                    cooldowns.put(entry.getKey(), (Long) entry.getValue());
                }

                kitCooldowns.put(playerId, cooldowns);
            }
        }
    }




}
