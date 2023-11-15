package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;



public class QuitListener implements Listener{
	GameData gameData =GameData.getInstance();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try {
			gameData.findPlayerTeam(event.getPlayer()).removePlayer(event.getPlayer());
		}catch (Exception e){
			System.out.println("strange things on player "+event.getPlayer().getName());
		}
		event.setQuitMessage(event.getPlayer().getName()+" hat die Runde verlassen");
		Bukkit.broadcastMessage(gameData.getCounterMessage());
	}
	
}
