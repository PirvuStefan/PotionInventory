package whyarewesoclever.com.potionInventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PotionInventory extends JavaPlugin {

    public static PotionInventory getInstance() {
        return getPlugin(PotionInventory.class);
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        // there we go
        saveDefaultConfig();
        // this will create the config.yml file if it doesn't exist
        File folder = getDataFolder();
        if (!folder.exists()) {
            if (folder.mkdir()) {
                getLogger().info("Folder 'PotionInventory' created successfully!");
            } else {
                getLogger().info("Failed to create folder 'PotionInventory'.");
            }
        }
        File folder2 = new File(getDataFolder(), "inventories");
        if (!folder2.exists()) {
            if (folder2.mkdir()) {
                getLogger().info("Folder 'inventories' created successfully!");
            } else {
                getLogger().info("Failed to create folder 'inventories'.");
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public boolean CheckPotion(ItemStack item){

        return switch (item.getType()) {
            case POTION, SPLASH_POTION, LINGERING_POTION -> true;
            default -> false;
        };
       // return true only if the item is a potion ( might also introduce tipped arrows for true case )
    }

    public void OpenInventory(Player player){
        // open the inventory for the player
        Inventory inv = Bukkit.createInventory(player, 9, "Potion Inventory");
        // add the potions to the inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (CheckPotion(item)) {
                inv.addItem(item);
            }
        } // the fuck ?
        // open the inventory for the player
        player.openInventory(inv);
    }

    // BASIC STRUCTURE //
    // PotionInventory -> Inventories -> player_name -> 9*4 yml. files
    // folder -> folder -> folder -> .yml
    // like ender chest functionalities

    // YML FILE //
    // PotionType : SPLASH
    // JSON :
    // Display

    // amount not necessary since it is unstackable
}


