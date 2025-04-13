package whyarewesoclever.com.potionInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

import static org.bukkit.Bukkit.getLogger;

public final class PotionInventory extends JavaPlugin implements Listener {

    public static PotionInventory getInstance() {
        return getPlugin(PotionInventory.class);
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        // there we go
        saveDefaultConfig();
        // this will create the config.yml file if it doesn't exist
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            PotionChest reloadCommand = new PotionChest("potioninventory");
            commandMap.register("potioninventory", reloadCommand);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


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

       getServer().getPluginManager().registerEvents(this, this);  // should register the events
        // getServer().getPluginManager().registerEvents(new PotionInventory(), this);
        // this will register the events for the plugin

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
        Inventory inv = Bukkit.createInventory(player, 9, "ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀʏ"); // fancy fo
        // add the potions to the inventory
         // the fuck ?
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
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();
        InventoryView view = player.getOpenInventory();
        Inventory inventory = event.getInventory();
        //boolean block = !view.getTitle().equals(ChatColor.DARK_AQUA + "ᴄʀᴇᴀᴛᴇ ᴄᴜꜱᴛᴏᴍ ᴛʀᴀᴅᴇꜱ");
        boolean block = view.getTitle().equals("ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀ");
        getLogger().info(view.getTitle());
        if( !block) return; // we dont register if it is other inventory

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        InventoryView view = player.getOpenInventory();
        Inventory inventory = event.getInventory();
        boolean block = view.getTitle().equals("ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀ");
        if( !block) return; // we dont register if it is other inventory
        getLogger().info("Inventory closed");
        // save the inventory to a file because it is updated
    }
}


