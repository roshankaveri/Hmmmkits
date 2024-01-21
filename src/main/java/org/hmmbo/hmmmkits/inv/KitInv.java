package org.hmmbo.hmmmkits.inv;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hmmbo.hmmmkits.Kit.Kit;
import org.hmmbo.hmmmkits.Kit.KitManager;
import org.hmmbo.hmmmkits.utils.KitUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class KitInv implements Listener {
    File file;

    public KitInv(File config) {
        file = config;
    }

    public Inventory menugui(Player player) {
        Inventory menuinv = Bukkit.createInventory(player, 54, ChatColor.YELLOW + "§lKitGUI");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Integer> Index = config.getIntegerList("allowed_index");
        List<Kit> kits = new ArrayList<>();
        if(config.getConfigurationSection("kit") != null) {
            for (String key : config.getConfigurationSection("kit").getKeys(false)) {
                KitManager km = new KitManager(file);
                Kit kit = km.loadKit(key);
                kits.add(kit);
            }
        }
        kits.sort(Comparator.naturalOrder());
        int i = 0;
        for(Kit k : kits){
            if(Index.size() <= i) {
                break;
            }
            int index = Index.get(i);
            i++;
            KitUtils ku = new KitUtils();
            ItemStack item = k.getDisplay();
            item.setType(Material.RED_STAINED_GLASS_PANE);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName((ChatColor.translateAlternateColorCodes('&', k.displayname)));
            im.setLocalizedName(k.name);
            im.setLore(Arrays.asList("§cYou don't have access","§fRight-click to preview"));
            if(player.hasPermission("kits."+k.name)) {
                if(!ku.isKitCooldownExpired(player, k.name)){
                    im.setLore(Arrays.asList("§eYou are on coolDown", "§cClaim after §f" + ku.showDelay(player,k.name),"§fRight-click to preview"));
                    item.setType(Material.YELLOW_STAINED_GLASS_PANE);
                }else {
                    im.setLore(Arrays.asList("§aLeft Click To Claim", "§fRight-click to preview"));
                    item.setType(Material.GREEN_STAINED_GLASS_PANE);
                }
            }
            item.setItemMeta(im);
            menuinv.setItem(index,item);
        }

        ItemStack border = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta bmeta = border.getItemMeta();
        bmeta.setDisplayName(" ");
        for (int k : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 27, 36,53,45,17, 26, 35, 44, 46, 47, 48, 49, 50, 51, 52}) {
            menuinv.setItem(k, border);
        }
        return menuinv;
    }

    @EventHandler
    public void oneditinvcclick(InventoryClickEvent f) {
        if (ChatColor.translateAlternateColorCodes('&', f.getView().getTitle()).equals(ChatColor.YELLOW + "§lKitGUI")) {
            f.setCancelled(true);
            Player player = (Player) f.getWhoClicked();
            if(f.isShiftClick()){

            }else{
                if( f.getCurrentItem() != null && f.getCurrentItem().getType() != Material.BLUE_STAINED_GLASS_PANE){
                    KitManager kn = new KitManager(file);
                    Kit k = kn.loadKit(f.getCurrentItem().getItemMeta().getLocalizedName());
                 //   System.out.println("before : " + k.contents);
                    if(f.isLeftClick()) {
                        if (k != null) {
                            if(player.hasPermission("kits."+k.name)) {
                                KitUtils ku = new KitUtils();
                                ku.giveKit(player, k);
                                KitInv inv = new KitInv(file);
                                player.openInventory(inv.menugui(player));
                          //      System.out.println("before 2: " + k.contents);
                            }else {
                                player.sendMessage("[Hmmkits] §cNo Permission To Claim This Kit");
                            }
                        }
                    } else if (f.isRightClick()) {
                        PrevInv p = new PrevInv(file);
                        player.openInventory(p.menugui(player,k));
                   //     System.out.println("before 3: " + k.contents);
                    }
                }
            }
        }
    }
}
