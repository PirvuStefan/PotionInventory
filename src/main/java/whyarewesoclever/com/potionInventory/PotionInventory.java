package whyarewesoclever.com.potionInventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class PotionInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        // there we go
        a

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
}


