package whyarewesoclever.com.potionInventory;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Registry.MATERIAL;

public final class PotionInventory extends JavaPlugin implements Listener {

        String itemName = null;
        String openSound = null;
        String closeSound = null;

        Set< String > potionTypes = Set.of("POTION", "SPLASH_POTION", "LINGERING_POTION");


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
        itemName = getConfig().getString("Name");
        openSound = getConfig().getString("OpenSound");
        closeSound = getConfig().getString("CloseSound");
        initialiseSet();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void OpenInventory(Player player){
        // open the inventory for the player
        Inventory inv = Bukkit.createInventory(player, 9, "ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀʏ"); // fancy fo
        // add the potions to the inventory
         // the fuck ?
        // open the inventory for the player
        createInventory(player.getName(),inv); // load the inventory from the yml file
        player.openInventory(inv);
    }

    public void createInventory(String PlayerName, Inventory inv) {
        // Retrieve the player's inventory from the map
        File folder = new File(getDataFolder(), "inventories");
        File file = new File(folder, PlayerName + ".yml");
        if( !file.exists()) createInventoryFile(PlayerName); // if no such file exist, we create one

       try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
           for( int i = 0;i<=8;i++){
                String material = reader.readLine(); // Read the next line from the file
                String second = reader.readLine(); // Read the next line from the file
                String json = null;

//               String[] parts = second.split(" ", 3); // Split into at most 3 parts
//               json = parts.length > 0 ? parts[0] : null;
//               customName = parts.length > 1 ? parts[1] : null;
//               displayName = parts.length > 2 ? parts[2] : null;
               json = second;
               ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
               if(!material.equals("AIR")) {
                   String [] jsonHolder = new String[1];
                   jsonHolder[0] = json;


                   ReadWriteNBT nbt = NBT.parseNBT(jsonHolder[0]);
                   ItemStack itemStack = NBT.itemStackFromNBT(nbt);







                   inv.setItem(i, itemStack);
               }

           }
       } catch (IOException e) {
           e.printStackTrace();
       }

    }

    public void createInventoryFile(String PlayerName){
        // update the inventory file when we close the inventory
        // we should also register it in the folder when any changes do occur in the inventory, like when we open the gui and when we close it since that it when we have to stock it
        // updateInvetoryFile()
        File folder = new File(getDataFolder(), "inventories");
        File file = new File(folder, PlayerName + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // create the file if it doesn't exist
        // write the inventory to the file
        for(int i = 0;i<=8;i++){
            String json = "{}";
            String material = "AIR";
            // write the json to the file
            // write the material to the file
            // write the display name to the file
            // write the custom name to the file
            try ( java.io.FileWriter writer = new java.io.FileWriter(file, true)) {
                writer.write(material + "\n");
                writer.write(json + "\n");
                //writer.write(json + (custom_name == null ? "" : (" " + custom_name)) + (display_name == null ? "" : (display_name + " ")) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // first line is the material and the second line is the json with the display name and custom name if they exist
        }
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
        boolean block = view.getTitle().equals("ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀʏ");
        if( !block) return; // we dont register if it is other inventory
        if( !checkPotion(event.getCurrentItem()))
            event.setCancelled(true); // we cancel the event if it is a potion
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        InventoryView view = player.getOpenInventory();
        Inventory inventory = event.getInventory();
        String fileName = player.getName() + ".yml";
        boolean block = view.getTitle().equals("ᴘᴏᴛɪᴏɴ ɪɴᴠᴇɴᴛᴏʀʏ");
        if( !block) return; // we dont register if it is other inventory

        if( !closeSound.equals("none") ) player.playSound(player.getLocation(), closeSound, 1.0f, 1.0f);

        // save the inventory to a file because it is updated
        try (java.io.FileWriter writer = new java.io.FileWriter(new File(getDataFolder(), "inventories/" + fileName))) {
            for (int i = 0; i <= 8; i++) {
                ItemStack item = inventory.getItem(i);
                String material;
                String json;
                String[] jsonHolder = new String[1];

                if (item == null) {
                    material = "AIR";
                    json = "{}";
                } else {
                    material = item.getType().toString();


//                    NBT.getComponents(item, nbt -> {
//
//                        jsonHolder[0] = nbt.toString();
//
//                        getLogger().info("NBT Tag: " + nbt);
//                    });

                    ReadWriteNBT nbt = NBT.itemStackToNBT(item);
                    json = nbt.toString();
                }
               // json = jsonHolder[0];
                writer.write(material + "\n");
                writer.write(json + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPotion(ItemStack itemStack){

        if(itemStack == null) return true;
        if(itemStack.getType() == Material.AIR) return true;
        if(itemStack.getType() == Material.POTION) return true;
        if(itemStack.getType() == Material.SPLASH_POTION) return true;
        if(itemStack.getType() == Material.LINGERING_POTION) return true;
        return potionTypes.contains(itemStack.getType().toString());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if it's a right-click action
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Ignore off-hand to prevent duplicate events
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        // Now you can test if the item is what you're looking for
        if( hasDisplayNameCheck(itemInHand, itemName)){
            OpenInventory(player);
           if( !openSound.equals("none") ) player.playSound(player.getLocation(), openSound, 1.0f, 1.0f);
        }

    }

    public boolean hasDisplayNameCheck(ItemStack item, String name){

        if(item == null)
            return false;
        if(item.getType() == Material.AIR)
            return false;

        return  NBT.getComponents(item, nbt -> {

            String customName = nbt.getString("minecraft:item_name");



            int index = customName.indexOf(name);
            return index != -1;


        });
    }

    public void initialiseSet(){
        if( !getConfig().getBoolean("CanAlsoUsePotionIngredients") )
            return;
        // we can also store the potion ingredients
        // we can also store the potion ingredients
        potionTypes.add("BREWING_STAND");
        potionTypes.add("BLAZE_POWDER");
        potionTypes.add("GLASS_BOTTLE");
        potionTypes.add("GUNPOWDER");
        potionTypes.add("FERMENTED_SPIDER_EYE");
        potionTypes.add("GLOWSTONE_DUST");
        potionTypes.add("GOLDEN_CARROT");
        potionTypes.add("NETHER_WART");
        potionTypes.add("REDSTONE_DUST");
        potionTypes.add("SPIDER_EYE");
        potionTypes.add("GLISTERING_MELON_SLICE");
        potionTypes.add("MAGMA_CREAM");
        potionTypes.add("SUGAR");
        potionTypes.add("PHANTOM_MEMBRANE");
        potionTypes.add("TIPPED_ARROW");
        potionTypes.add("DRAGON_BREATH");
        potionTypes.add("SPECKLED_MELON");
        potionTypes.add("RABBIT_FOOT");
        potionTypes.add("GHAST_TEAR");
        potionTypes.add("GLOWSTONE");

        // not sure if they are all
    }//fututi

}


