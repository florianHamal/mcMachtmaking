package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.gameManager.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {
    private GameData gameData = GameData.getInstance();
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        Manager.GameState gameState = Manager.getInstance().getGameState();
        if (gameState==Manager.GameState.lobby||gameState== Manager.GameState.starting) {
            event.setCancelled(true);
            return;
        }
        Player damager = (Player) event.getDamager();
        Player hurted = (Player) event.getEntity();
        try {
            if(gameData.findPlayerTeam(damager) == gameData.findPlayerTeam(hurted)){
                event.setCancelled(true);
            }
        }catch (Exception e){
            //Player isnt in team
        }
    }


}
