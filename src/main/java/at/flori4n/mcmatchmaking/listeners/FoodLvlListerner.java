package at.flori4n.mcmatchmaking.listeners;

import at.flori4n.mcmatchmaking.gameManager.Manager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLvlListerner implements Listener {
    private Manager manager = Manager.getInstance();
    @EventHandler
    public void onFoodLvlChange(FoodLevelChangeEvent e){
        //heal if in lobby
        if (manager.getGameState()== Manager.GameState.starting||manager.getGameState()== Manager.GameState.lobby)
            e.setCancelled(true);
    }
}
