package at.flori4n.mcmatchmaking.commands;


import at.flori4n.mcmatchmaking.border.Border;
import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.teamClasses.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetupCommands implements CommandExecutor {
	GameData gameData = GameData.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission("mcMatchmaking.set")) {
			switch(args[0]) {
				case"setLobby":
					gameData.setLobby(player.getLocation());
					break;
				case"setSpawn":
					gameData.setSpawn(player.getLocation(),Integer.parseInt(args[1]));
					break;
				case"save":
					gameData.writeOnFile();
					break;
				case"setPlayersToStart":
					gameData.setPlayersToStart(Integer.parseInt(args[1]));
					break;
				case"testData":
					player.sendMessage(gameData.getTeams().toString());
					break;
				case"removeTeam":
					gameData.removeTeam(Integer.parseInt(args[1]));
					break;
				case"addTeam":
					gameData.getTeams().add(new Team(Integer.parseInt(args[1]),gameData.getTeams().size(),player.getLocation(),0,true));
					break;
				case "toggleStart":
					gameData.setStart(Boolean.parseBoolean(args[1]));
					break;
				case "toggleWorldBorder":
					gameData.setWorldBorder(Boolean.parseBoolean(args[1]));
					break;
				case "setWorldBorder":
					Border.setStartingSize(Float.parseFloat(args[1]));
					Border.setDamage(Float.parseFloat(args[2]));
					Border.setSpeed(Long.parseLong(args[3]));
					break;
				case "addEndPoint":
					Border.getEndPoints().add(player.getLocation());
					break;
				case "removeEndPoint":
					Border.getEndPoints().remove(player.getLocation());
					break;
				case "showEndPoints":
					player.sendMessage(Border.getEndPoints().toString());
					break;
				case "setCenter":
					Border.setCenter(player.getLocation());
					Border.setWorld(player.getWorld());
					player.getWorld().getWorldBorder().setCenter(player.getLocation());

					break;
				case "help":
					player.sendMessage("setLobby -> setzt die lobby" +
										  "setSpawn -> setzt den Spawn eines Teams neu" +
							"save -> speichert die EInstellungen"+
							"setPlayersToStart -> setzt die Spielerzahl ab der die Runde startet" +
							"reoveTeam ->entfernt das Team mit der Nummer");
					break;
			default:
				player.sendMessage("falsche reinfolge\n" +
									"/mcmatchmaking help for more info");
				break;
			}
			
		}else{
			player.sendMessage("�cDazu hast du keine Rechte!");
		}
		return false;
	}

}
