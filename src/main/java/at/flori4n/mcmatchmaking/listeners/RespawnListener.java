package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    private GameData gameData;
    public RespawnListener(GameData gameData){
        this.gameData = gameData;
    }
    public void onRespawn (PlayerRespawnEvent e){
        Player player = e.getPlayer();

        if (!gameData.isStart()){
            player.teleport(gameData.getLobby());
            player.setGameMode(GameMode.ADVENTURE);
            return;
        }

        try {
            player.teleport(gameData.findPlayerTeam(player).getLocation());
        } catch (Exception ex) {
            player.setGameMode(GameMode.SPECTATOR);
            try {
                player.teleport(gameData.getTeams().get(0).getLocation());
            }catch (Exception exept){

            }

        }


    }
}
