package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.gameManager.Manager;
import at.flori4n.mcmatchmaking.teamClasses.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui implements Listener {

    private GameData gameData =GameData.getInstance();
    private static String GUI_NAME = "Select Team";


    public void openGUI(Player player){
        Team team;
        int size = (gameData.getTeams().size()/9)+1;
        Inventory inventory = Bukkit.createInventory(null,size*9,GUI_NAME);
        for (int i = 0;i<gameData.getTeams().size();i++){
            team = gameData.getTeams().get(i);
            ItemStack itemStack = new ItemStack(Material.BED);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(team.getFormatedForGUI());
            itemMeta.setDisplayName("Team "+i);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i,itemStack);
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void handleOpener(PlayerInteractEvent event){
        try {
            if (event.getItem().getType() != Material.BED) return;
            if (Manager.getInstance().getGameState()== Manager.GameState.lobby||Manager.getInstance().getGameState()== Manager.GameState.starting) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    openGUI(event.getPlayer());
                }
            }
        }catch (NullPointerException e){

        }
    }

    @EventHandler
    public void handleSelectorGUIClick(InventoryClickEvent event){
        if (!(event.getWhoClicked()instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals(GUI_NAME)){
            if (event.getCurrentItem().getType()==Material.BED){
                    String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                    try {
                        gameData.removePlayerFromTeam(player);
                        gameData.getTeams().get(Integer.parseInt(itemName.substring(5))).addPlayer(player);
                    }catch (Exception ex){
                        player.sendMessage("Dieses Team ist leider voll");
                    }
                    player.closeInventory();
                    event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void handleOplayerJoin(PlayerJoinEvent e){

    }
}
