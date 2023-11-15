package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.logging.Level;


public class DeathListener implements Listener{
	private GameData gameData = GameData.getInstance();

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
			try {
				gameData.findPlayerTeam(player).onPlayerDeath(player);
				event.setDeathMessage(player.getName() + " wurde von " + player.getKiller().getName() + " getötet");
			} catch (Exception e) {
				//wehn player isnt in team
			}
	}


}

