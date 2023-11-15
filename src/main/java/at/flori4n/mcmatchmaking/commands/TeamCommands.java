package at.flori4n.mcmatchmaking.commands;

import at.flori4n.mcmatchmaking.gameData.GameData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommands implements CommandExecutor {
    private GameData gameData = GameData.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        switch (args[0]){
            case "join":
                gameData.getTeams().get(Integer.parseInt(args[1])).addPlayer(player);
                break;
            case "leave":
                try {
                    gameData.findPlayerTeam(player).removePlayer(player);
                } catch (Exception e) {
                    player.sendMessage("Du bist in keinem Team");
                }
                break;
            case "show":
                for (int i = 0;i<gameData.getTeams().size();i++) {
                    player.sendMessage(gameData.getTeams().get(i).toString());
                    Bukkit.broadcastMessage(gameData.getTeams().get(i).toString());
                }
                break;
            default: player.sendMessage("ungültiger Befehl");
        }
        return false;
    }
}
