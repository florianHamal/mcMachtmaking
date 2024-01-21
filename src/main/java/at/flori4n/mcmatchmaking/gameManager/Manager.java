package at.flori4n.mcmatchmaking.gameManager;

import at.flori4n.mcmatchmaking.McMatchmaking;
import at.flori4n.mcmatchmaking.border.Border;
import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.teamClasses.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Manager {
	public enum GameState{
		lobby,starting,ingame,over
	}
	private static Manager instance;
	GameData gameData = GameData.getInstance();
	private int counter;
	private GameState gameState = GameState.lobby;
	
	private Manager() {};
	public void start(){
		Border.reset();
		if (gameData.isStart()) {
			run();
		}
	}

	public void run(){

			Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmaking.getPlugin(), new Runnable() {

				@Override
				public void run() {
					switch (gameState) {
						case lobby:
							lobby();
							break;
						case starting:
							starting();
							break;
						case ingame:
							ingame();
							break;
						case over:
							over();
							break;
						default:
							Bukkit.broadcastMessage("§6Ein Fehler ist passiert");
					}

					if (counter >= 0) {
						counter--;
					}
				}
			}, 0, 20);

	}

	private void lobby(){
		if (Bukkit.getOnlinePlayers().size() >= gameData.getPlayersToStart()) {
			counter = 60;
			gameState = GameState.starting;
		}
	}
	private void starting(){
		if (Bukkit.getOnlinePlayers().size() < gameData.getPlayersToStart()) {
			gameState = gameState.lobby;
			Bukkit.broadcastMessage("Countdown abgebrochen. Zu Wenig Spieler!");
		} else {
			if (counter < 6 && counter > 0) {
				Bukkit.broadcastMessage("Das Game Startet in " + counter + " Sekunden");
				for (Player player:Bukkit.getOnlinePlayers()){
					player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
				}

			} else if (counter % 10 == 0) {
				Bukkit.broadcastMessage("Das Game Startet in " + counter + " Sekunden");
			} else if (counter <= 0) {
				Bukkit.broadcastMessage("Das Game Startet");
				gameState = GameState.ingame;
				//addLeftOverPlayersToTeam();  ///<------------ 			FIX
				putPlayersInnTeams();
				gameData.tpPlayersToSpawn();
				for (Team team:gameData.getTeams()){
					team.getPlayersAlive().forEach(p->{p.setGameMode(gameData.getIngameMode());});
				}
				if (gameData.isWorldBorder()){
					Border.start();
				}
			}
		}
	}
	private void ingame(){
		try {
			sendWinMessage(checkWinnerTeam());
			counter = 15;
			if (gameData.isWorldBorder()){
				Border.stop();
			}
			gameState = GameState.over;
		}catch (Exception ex){

		}
	}
	private void over(){
		if (counter <= 0) {
			Bukkit.shutdown();
		}
	}

	public Team checkWinnerTeam() throws Exception {
		int teamsAlive = 0;
		Team winnerTeam = null;
		for (Team team:gameData.getTeams()){
			if(team.isTeamAlive()){
				teamsAlive++;
				if (teamsAlive>1)throw new Exception("noTeamHasWonYet");
				winnerTeam = team;
			}
		}
		return winnerTeam;
	}
	public void putPlayersInnTeams(){
		boolean isInTeam;
		for(Player player:Bukkit.getOnlinePlayers()){
			try {
				gameData.findPlayerTeam(player);
				isInTeam = true;
			} catch (Exception e) {
				isInTeam = false;
				for (Team team : gameData.getTeams()) {
					if (!(team.isFull())) {
						team.addPlayer(player);
						isInTeam = true;
						break;
					}
				}
				if (!isInTeam) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			}

		}
	}

	public GameState getGameState() {
		return gameState;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public static Manager getInstance(){
		if (instance==null){
			instance=new Manager();
		}
		return instance;
	}
	public static void sendWinMessage(Team team){
		if (team.getMaxSize()<=1){
			//if singleplayer teams
			Bukkit.broadcastMessage("§3-> §4"+ team.getPlayers().get(0).getName() + " §3hat die Runde gewonnen");
			for (Player player:Bukkit.getOnlinePlayers()) {
				player.sendTitle("§4"+team.getPlayers().get(0).getName(),"§6hat die Runde gewonnen");
				player.playSound(player.getLocation(),Sound.ENDERDRAGON_DEATH,1,1);
			}
		}else {
			//if teams with multiple players
			Bukkit.broadcastMessage("§3-> §6Team §4"+ team.getTeamNumber() + " §3hat die Runde gewonnen");
			for (Player player:team.getPlayers()) {
				Bukkit.broadcastMessage("§3--> "+player.getName());
			}
			for (Player player:Bukkit.getOnlinePlayers()) {
				player.sendTitle("§6Team §4"+team.getTeamNumber(),"§6hat die Runde gewonnen");
				player.playSound(player.getLocation(),Sound.ENDERDRAGON_DEATH,1,1);
			}
		}



	}
}
