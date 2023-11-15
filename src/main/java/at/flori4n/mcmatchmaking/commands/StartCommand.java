package at.flori4n.mcmatchmaking.commands;

import at.flori4n.mcmatchmaking.gameManager.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StartCommand implements CommandExecutor{

	private Manager manager =Manager.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		Player player = (Player) sender;
		if(player.hasPermission("game.start")) {
			manager.setCounter(0);
		}else {
			sender.sendMessage("�cDazu hast du keine Rachte");
		}
		return false;
	}
}
