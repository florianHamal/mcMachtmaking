package at.flori4n.mcmatchmaking.teamClasses;


import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.gameManager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private ChatColor color;
    private Location location;
    private Integer teamNumber;
    private int maxSize;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Player> playersAlive = new ArrayList<>();
    private int teamLives;
    private boolean canLoseLives;

    public Team(int maxSize, int teamNumber, Location location,Integer teamLives,boolean canLoseLives){
        this.teamNumber=teamNumber;
        this.maxSize = maxSize;
        color = GameData.getTeamColors().get(teamNumber);
        this.location = location;
        this.teamLives = teamLives;
        this.canLoseLives = canLoseLives;
    }

    public void addPlayer (Player player){
        if(players.size()<maxSize) {
            players.add(player);
            playersAlive.add(player);
            try {
                setNameColor(player);
            } catch (Exception e){
            }
        }else{
            player.sendMessage("Dieses Team ist leider voll");
            System.out.println("endelse");
        }
    }
    public void removePlayer (Player player){
        players.remove(player);
        if (playersAlive.contains(player)){
            playersAlive.remove(player);
        }
    }
    public void setPlayerDeath(Player player){
        playersAlive.remove(player);
        player.setGameMode(GameMode.SPECTATOR);
    }
    public void onPlayerDeath(Player player){
        if(!(Manager.getInstance().getGameState()== Manager.GameState.lobby||Manager.getInstance().getGameState()== Manager.GameState.starting)) {
            if (canLoseLives) {
                if (teamLives > 0) {
                    System.out.println("teamlives --");
                    teamLives--;
                } else {
                    setPlayerDeath(player);
                }
            }
        }
    }


    public boolean isTeamAlive(){
        if(playersAlive.size()>0){
            return true;
        }
        return false;
    }
    public int getFreeSpace(){
        return maxSize - players.size();
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getMaxSize() {
        return maxSize;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }
    public boolean isFull(){
        if(playersAlive.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String retValue = "\nTeamNumber: " + teamNumber + "\nmaxSize" + maxSize + "\nplayers:\n";
        for (int i = 0; i < players.size(); i++) {
            retValue = retValue + "\t" + players.get(i).getName();
        }
        retValue = retValue + "\nAlive:\n";
        for (int i = 0; i < playersAlive.size(); i++) {
            retValue = retValue + "\t" + playersAlive.get(i).getName();

        }
        return retValue;
    }

    public ArrayList<Player> getPlayersAlive() {
        return playersAlive;
    }

    public List getFormatedForGUI(){
        List<String> retValue= new ArrayList<String>();
        retValue.add(color+" Members:");
        for(Player player:players){
            retValue.add(player.getName());
        }
    return retValue;
    }

    private void setNameColor(Player player){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        org.bukkit.scoreboard.Team team;

        if (board.getTeam(player.getName())==null){
            team = board.registerNewTeam(player.getName());
        }else {
            team = board.getTeam(player.getName());
        }
        team.setPrefix(color+"[Team "+teamNumber+"]");
        team.addPlayer(player);
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPlayersAlive(ArrayList<Player> playersAlive) {
        this.playersAlive = playersAlive;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getTeamLives() {
        return teamLives;
    }

    public void setTeamLives(int teamLives) {
        this.teamLives = teamLives;
    }

    public boolean isCanLoseLives() {
        return canLoseLives;
    }

    public void setCanLoseLives(boolean canLoseLives) {
        this.canLoseLives = canLoseLives;
    }
}
