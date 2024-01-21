package org.hmmbo.hmmmkits.Kit;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit implements Comparable<Kit> {
    public String name;
    public Long cooldown;
    public String displayname;
    public List<ItemStack> contents;
    public ItemStack display;
    public Sound sound;
    public int weight;

    public Kit(String name, Long cooldown, String displayname, List<ItemStack> contents, ItemStack display, Sound sound, int weight) {
        this.name = name;
        this.cooldown = cooldown;
        this.displayname = displayname;
        this.contents = contents;
        this.display = display;
        this.sound = sound;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Long getCooldown() {
        return cooldown;
    }

    public String getPermission() {
        return displayname;
    }

    public List<ItemStack> getContents() {
        return contents;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public Sound getSound() {
        return sound;
    }

    public int getWeight() {
        return weight;
    }


    @Override
    public int compareTo(Kit o) {
        return Integer.compare(o.weight, weight);
    }
}
