package whyarewesoclever.com.potionInventory;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getLogger;

public class PotionChest extends BukkitCommand implements Listener {

    public PotionChest(String name){
        super(name);
        this.setDescription("Create custom villager trades .\n Run the command /bettervillagers create .\nReload the config file to apply changes . \n Requires permission bettervillagers.commands");
        this.setUsage("/potioninventory");
        this.setPermission("potioninventory.use");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if( !(commandSender instanceof Player) ) {
            getLogger().info("This command can only be run by a player .");
            return false;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("potioninventory.use")){
            player.sendMessage("You do not have permission to use this command .");
            return false;
        }
        PotionInventory.getInstance().OpenInventory(player);
        // open the inventory for the player
        return true;

    }
}
