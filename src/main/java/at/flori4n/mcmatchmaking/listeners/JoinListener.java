package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.gameManager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class JoinListener implements Listener {
	GameData gameData = GameData.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
			clearPlayerInv(event.getPlayer());
			if (Manager.getInstance().getGameState() == Manager.GameState.lobby || Manager.getInstance().getGameState() == Manager.GameState.starting) {
				event.setJoinMessage(event.getPlayer().getName() + " hat die Runde betreten");
				event.getPlayer().setGameMode(gameData.getLobbyMode());
				event.getPlayer().teleport(gameData.getLobby());
				//for gui
				ItemStack itemStack = new ItemStack(Material.BED);
				itemStack.getItemMeta().setDisplayName("team selector");
				event.getPlayer().getInventory().addItem();
			} else {
				event.getPlayer().setGameMode(GameMode.SPECTATOR);
				event.getPlayer().teleport(gameData.getTeams().get(0).getLocation());
			}
			Bukkit.broadcastMessage(gameData.getCounterMessage());
	}
	public void clearPlayerInv(Player player){
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		gameData.removePlayerPrefix(player);
		player.setLevel(0);
		player.setExp(0);
	}
}
