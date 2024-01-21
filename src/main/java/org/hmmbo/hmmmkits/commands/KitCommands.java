package org.hmmbo.hmmmkits.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hmmbo.hmmmkits.Kit.Kit;
import org.hmmbo.hmmmkits.Kit.KitManager;
import org.hmmbo.hmmmkits.inv.KitInv;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class KitCommands implements CommandExecutor {
    File file;

    public KitCommands(File file) {
        this.file = file;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length == 5) {
            if(player.isOp()) {
                if (args[0].equalsIgnoreCase("create")) {
                    KitManager kms = new KitManager(file);
                    if (kms.loadKit(args[1]) == null) {
                        Kit kit = new Kit(args[1], Long.parseLong(args[3]), args[2], Arrays.stream(player.getInventory().getStorageContents()).toList(),
                                new ItemStack(Material.RED_STAINED_GLASS_PANE, 1), Sound.ITEM_GOAT_HORN_SOUND_0, Integer.parseInt(args[4]));
                        KitManager km = new KitManager(file);
                        km.saveKit(kit);
                        player.sendMessage("[HmmKits] §aKit "+ args[1] +" Created");
                    }else {
                        player.sendMessage("[HmmKits] §cKit Already Exists");
                    }
                }
            }
        }
        if (args.length == 2) {
            if(player.isOp()) {
                if (args[0].equalsIgnoreCase("delete")) {
                    KitManager km = new KitManager(file);
                    if (km.loadKit(args[1]) != null) {
                        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                        yml.set("kit." + args[1], null);
                        try {
                            yml.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        player.sendMessage("[HmmKits] §aKit "+ args[1] +" Deleted");
                    }else {
                        player.sendMessage("[HmmKits] §cKit Doesn't Exist");
                    }
                }
            }
        }else
        if (args.length == 1) {
            if(player.isOp()) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage("             §b§lHmmKits Help\n" +
                            "§f§lA Product Of Hmmbo Development\n "+"\n" +
                            "§b/kit -> §fopens kit menu\n" +
                            "§b/kit delete (name) -> §fdelete the kit\n" +
                            "§b/kit create (name) (displayname) (cooldown in seconds) (weight)\n" +
                            " \n" +" \n"+
                            "§f§lPlugin Understanding\n" +
                            "§bWeight : §fHigher Weight Kit Will Appear First In GUI\n" +
                            "§bName : §fOne Which Will Be Used To Create & Delete Kits\n" +
                            "§bDisplay Name : §fName With Color Codes\n" +
                            "§bGui Index : §fYou Can Set Where To Add Kits\n"+
                            "                In The GUI (Any Number Of Kits)\n" +
                            " \n"+" \n" +
                            "§f§lUseFul Resources\n" +
                            "§b1.Inventory Slot Number : §f https://mcutils.com/inventory-slots\n" +
                            "§b2.Support Server :§f https://discord.hmmbo.fun/");



                }
            }
        }else if (args.length == 0) {
            KitInv kitInv = new KitInv(file);
            player.openInventory( kitInv.menugui(player));
        }



        return false;
    }
}
