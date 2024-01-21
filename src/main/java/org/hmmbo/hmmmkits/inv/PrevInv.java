package org.hmmbo.hmmmkits.inv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.hmmbo.hmmmkits.Kit.Kit;

import java.io.File;
import java.util.Arrays;


public class PrevInv implements Listener {
        File file;

        public PrevInv(File config) {
            file = config;
        }

        public Inventory menugui(Player player, Kit kit) {
            Inventory menuinv = Bukkit.createInventory(player, 54, ChatColor.YELLOW + "§lPreview Menu");
            int j = 10;
            for(ItemStack item : kit.getContents()){
                menuinv.setItem(j, item);
                j++;
                for (int k : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 27, 36, 17, 26, 35, 44, 46, 47, 48, 49, 50, 51, 52}) {
                    if (j == k) {
                        j = j + 2;
                        break;
                    }
                }
            }

            ItemStack border = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
            ItemMeta bmeta = border.getItemMeta();
            bmeta.setDisplayName(" ");
            for (int k : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 27, 36,17, 26, 35, 44, 46, 47, 48, 49, 50, 51, 52}) {
                menuinv.setItem(k, border);
            }

            //close
            ItemStack close = new ItemStack(Material.BARRIER, 1);
            ItemMeta clmeta = close.getItemMeta();
            clmeta.setDisplayName(ChatColor.RED + "Close");
            clmeta.setLore(Arrays.asList(ChatColor.WHITE + "Go Back"));
            close.setItemMeta(clmeta);

            menuinv.setItem(53, close);

            //player
            ItemStack person = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta smeta = (SkullMeta) person.getItemMeta();
            smeta.setDisplayName(ChatColor.YELLOW + kit.displayname.replace('&','§'));
            smeta.setLore(Arrays.asList(ChatColor.WHITE + "Kit Name: "+ kit.name));
            smeta.setOwner(player.getDisplayName().toString());
            person.setItemMeta(smeta);
            menuinv.setItem(45, person);


            return menuinv;
        }

        @EventHandler
        public void oneditinvcclick(InventoryClickEvent f) {
            if (ChatColor.translateAlternateColorCodes('&', f.getView().getTitle()).equals(ChatColor.YELLOW + "§lPreview Menu")) {
                f.setCancelled(true);
                Player player = (Player) f.getWhoClicked();
                if(f.getRawSlot() == 53){
                    KitInv kitInv = new KitInv(file);
                    player.openInventory( kitInv.menugui(player));
                }
            }
        }
    }

