package at.flori4n.mcmatchmaking.gameData;

import at.flori4n.mcmatchmaking.border.Border;
import at.flori4n.mcmatchmaking.teamClasses.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class GameData {
    private static GameData instance;

    private GameMode ingameMode = GameMode.ADVENTURE;
    private GameMode lobbyMode = GameMode.ADVENTURE;
    private static HashMap<Integer, ChatColor> teamColors= new HashMap<Integer,ChatColor>();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private Location lobby = new Location(null, 0,0,0,0,0);
    private int playersToStart;
    private boolean start = false;
    private int maxPlayers;
    private boolean worldBorder = false;
    private boolean preventBlockBreaking = true;



    private GameData (){
        initTeamColors();
        Config.setup();
        Config.getCustomFile().addDefault("start",false);
        Config.getCustomFile().addDefault("teamSize",0);
        Config.getCustomFile().addDefault("lobby", "NULL");
        Config.getCustomFile().addDefault("maxPlayers", 0);
        Config.getCustomFile().addDefault("playersToStart", 0);
        Config.getCustomFile().options().copyDefaults(true);
        Config.save();
        readFile();
        for (Team team:teams){
            maxPlayers+=team.getMaxSize();
        }

    }

    public void removePlayerFromTeam (Player player){
        try {
            findPlayerTeam(player).removePlayer(player);
        } catch (Exception e) {

        }

    }
    public Team findPlayerTeam(Player player) throws Exception {
        for (Team team:teams){
            if (team.getPlayers().contains(player)){
                return team;
            }
        }
        throw new Exception("player isnt in a team");

    }


    public String getCounterMessage(){
        return Bukkit.getOnlinePlayers().size()+"/"+ maxPlayers+" Spieler befinden sich in der Lobby";
    }

    public void setMaxPlayers(int maxPlayers) {

        this.maxPlayers = maxPlayers;

    }
    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }
    public void setSpawn(Location spawn ,int value) {
        //teams.get(value).setLocation(spawn);
        getTeam(value).setLocation(spawn);
    }
    public void writeOnFile() {
        Config.getCustomFile().set("start",start);
        Config.getCustomFile().set("preventBlockBreaking",preventBlockBreaking);
        Config.getCustomFile().set("teams.amount",teams.size());
        Config.getCustomFile().set("lobby.x",lobby.getX());
        Config.getCustomFile().set("lobby.y",lobby.getY());
        Config.getCustomFile().set("lobby.z",lobby.getZ());
        Config.getCustomFile().set("lobby.yaw",lobby.getYaw());
        Config.getCustomFile().set("lobby.pitch",lobby.getPitch());
        Config.getCustomFile().set("lobby.world",(String)lobby.getWorld().getName());
            for(int i =0;i<teams.size();i++) {
                Config.getCustomFile().set("spawn"+i+".x",teams.get(i).getLocation().getX());
                Config.getCustomFile().set("spawn"+i+".y",teams.get(i).getLocation().getY());
                Config.getCustomFile().set("spawn"+i+".z",teams.get(i).getLocation().getZ());
                Config.getCustomFile().set("spawn"+i+".yaw",teams.get(i).getLocation().getPitch());
                Config.getCustomFile().set("spawn"+i+".pitch",teams.get(i).getLocation().getYaw());
                Config.getCustomFile().set("spawn"+i+".world",teams.get(i).getLocation().getWorld().getName());
                Config.getCustomFile().set("maxSize"+i,teams.get(i).getMaxSize());
                Config.getCustomFile().set("teamLives"+i,teams.get(i).getTeamLives());
                Config.getCustomFile().set("canLoseLives"+i,teams.get(i).isCanLoseLives());
            }
        Config.getCustomFile().set("playersToStart", playersToStart);
        Config.getCustomFile().set("WorldBorder",worldBorder);
        if (worldBorder){
            Config.getCustomFile().set("BorderWorld", Border.getWorld().getName());

            Config.getCustomFile().set("BorderEndPointAmount",Border.getEndPoints().size());
            System.out.println(Border.getEndPoints().toString());
            for (int i = 0;i<Border.getEndPoints().size();i++) {
                System.out.println("TestInternal");
                Config.getCustomFile().set("BorderCenterX"+i, Border.getEndPoints().get(i).getX());
                Config.getCustomFile().set("BorderCenterY"+i, Border.getEndPoints().get(i).getY());
                Config.getCustomFile().set("BorderCenterZ"+i, Border.getEndPoints().get(i).getZ());
            }
            System.out.println("Test2");

            Config.getCustomFile().set("BorderDamage", Border.getDamage());
            Config.getCustomFile().set("BorderSpeed", Border.getSpeed());
            Config.getCustomFile().set("BorderSize", Border.getStartingSize());
            Config.getCustomFile().set("BorderWarningDistance", Border.getWarinigDistance());
            Config.getCustomFile().set("BorderStartCenterX", Border.getCenter().getX());
            Config.getCustomFile().set("BorderStartCenterY", Border.getCenter().getY());
            Config.getCustomFile().set("BorderStartCenterZ", Border.getCenter().getZ());
        }


        Config.save();
    }
    public void readFile() {
        float yaw,pitch;
        try {
            this.preventBlockBreaking = Config.getCustomFile().getBoolean("preventBlockBreaking");
            this.playersToStart = Config.getCustomFile().getInt("playersToStart");
            this.lobby.setX( Config.getCustomFile().getDouble("lobby.x"));
            this.lobby.setY( Config.getCustomFile().getDouble("lobby.y"));
            this.lobby.setZ( Config.getCustomFile().getDouble("lobby.z"));
            yaw = (float) Config.getCustomFile().getDouble("lobby.yaw");
            pitch = (float)Config.getCustomFile().getDouble("lobby.pitch");
            this.lobby.setYaw(yaw);
            this.lobby.setPitch(pitch);
            this.lobby.setWorld(Bukkit.getWorld((String) Config.getCustomFile().get("lobby.world")));
        }catch(Exception e) {
            System.out.println("Exception while reading file");
        }
        boolean canLoseLives;
        double x;
        double y;
        double z;
        World world;
        int maxSize;
        int teamLives;
        int amount = Config.getCustomFile().getInt("teams.amount");
        for(int i = 0;i<amount;i++) {
            try {
                this.start = Config.getCustomFile().getBoolean("start");
                x = Config.getCustomFile().getDouble("spawn"+i+".x");
                y = Config.getCustomFile().getDouble("spawn"+i+".y");
                z = Config.getCustomFile().getDouble("spawn"+i+".z");
                yaw = (float)Config.getCustomFile().getDouble("spawn"+i+".yaw");
                pitch = (float)Config.getCustomFile().getDouble("spawn"+i+".pitch");
                world = Bukkit.getWorld((String) Config.getCustomFile().get("spawn"+i+".world"));
                maxSize = Config.getCustomFile().getInt("maxSize"+i);
                teamLives = Config.getCustomFile().getInt("teamLives" + i);
                canLoseLives = Config.getCustomFile().getBoolean("canLoseLives"+i);
                this.teams.add(new Team(maxSize,i,new Location(world,x,y,z,yaw,pitch),teamLives,canLoseLives));
            }catch(Exception e) {
                System.out.println("ERR");
                break;
            }
        }
        worldBorder = Config.getCustomFile().getBoolean("WorldBorder");
        if (worldBorder) {
            Border.setWorld(Bukkit.getWorld((String) Config.getCustomFile().getString("BorderWorld")));

            try {
                amount = Config.getCustomFile().getInt("BorderEndPointAmount");

                for (int i = 0; i < amount; i++) {
                    Border.getEndPoints().add(new Location(Border.getWorld(),
                            Config.getCustomFile().getDouble("BorderCenterX" + i),
                            Config.getCustomFile().getDouble("BorderCenterY" + i),
                            Config.getCustomFile().getDouble("BorderCenterZ" + i)));
                    System.out.println("test1");
                }

                System.out.println("test2");
                Border.setDamage((float) Config.getCustomFile().getDouble("BorderDamage"));
                Border.setSpeed(Config.getCustomFile().getLong("BorderSpeed"));
                Border.setStartingSize((float) Config.getCustomFile().getDouble("BorderSize"));
                Border.setWarinigDistance(Config.getCustomFile().getInt("BorderWarningDistance"));

                Border.setCenter(new Location(Border.getWorld(),
                Config.getCustomFile().getDouble("BorderStartCenterX"),
                Config.getCustomFile().getDouble("BorderStartCenterY"),
                Config.getCustomFile().getDouble("BorderStartCenterZ")));

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public Location getLobby() {
        return lobby;
    }
    public int getPlayersToStart() {
        return playersToStart;
    }
    public void setPlayersToStart(int playersToStart) {
        this.playersToStart = playersToStart;
    }

    public boolean isStart() {
        return start;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }


    public void tpPlayersToSpawn(){
        Location location;
        for(Team team:teams) {
            for (Player player:team.getPlayersAlive()) {
                player.teleport(team.getLocation());
                player.getInventory().clear();
                player.playSound(player.getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
            }
        }
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public static HashMap<Integer, ChatColor> getTeamColors() {
        return teamColors;
    }

    public void initTeamColors(){
        GameData.getTeamColors().put(0,ChatColor.DARK_BLUE);
        GameData.getTeamColors().put(1,ChatColor.BLUE);
        GameData.getTeamColors().put(2,ChatColor.DARK_GREEN);
        GameData.getTeamColors().put(2,ChatColor.DARK_AQUA);
        GameData.getTeamColors().put(3,ChatColor.DARK_RED);
        GameData.getTeamColors().put(4,ChatColor.LIGHT_PURPLE);
        GameData.getTeamColors().put(5,ChatColor.GOLD);
        GameData.getTeamColors().put(6,ChatColor.GRAY);
        GameData.getTeamColors().put(7,ChatColor.DARK_GRAY);
        GameData.getTeamColors().put(8,ChatColor.GREEN);
        GameData.getTeamColors().put(9,ChatColor.AQUA);
        GameData.getTeamColors().put(10,ChatColor.RED);
        GameData.getTeamColors().put(11,ChatColor.LIGHT_PURPLE);
        GameData.getTeamColors().put(12,ChatColor.YELLOW);
        GameData.getTeamColors().put(13,ChatColor.WHITE);
        GameData.getTeamColors().put(14,ChatColor.BLACK);
        GameData.getTeamColors().put(15,ChatColor.DARK_BLUE);
        GameData.getTeamColors().put(16,ChatColor.BLUE);
        GameData.getTeamColors().put(17,ChatColor.DARK_GREEN);
        GameData.getTeamColors().put(18,ChatColor.DARK_AQUA);
        GameData.getTeamColors().put(19,ChatColor.DARK_RED);
        GameData.getTeamColors().put(20,ChatColor.LIGHT_PURPLE);
        GameData.getTeamColors().put(21,ChatColor.GOLD);
        GameData.getTeamColors().put(22,ChatColor.GRAY);
        GameData.getTeamColors().put(23,ChatColor.DARK_GRAY);
        GameData.getTeamColors().put(24,ChatColor.GREEN);
        GameData.getTeamColors().put(25,ChatColor.AQUA);
        GameData.getTeamColors().put(26,ChatColor.RED);
        GameData.getTeamColors().put(27,ChatColor.LIGHT_PURPLE);
        GameData.getTeamColors().put(28,ChatColor.YELLOW);
        GameData.getTeamColors().put(29,ChatColor.WHITE);
        GameData.getTeamColors().put(30,ChatColor.BLACK);
    }
    public Team getTeam(int number){
        for (Team team:teams){
            if (team.getTeamNumber()==number){
                return team;
            }
        }
        return null;
    }
    public void removeTeam(int teamNuber) {
        Team team = getTeam(teamNuber);
        if (team != null) {
            teams.remove(team);
            for (Team value:teams){
                if (value.getTeamNumber()>teamNuber){
                    value.setTeamNumber(value.getTeamNumber()-1);
                }
            }
        }
    }
    public void removePlayerPrefix(Player player){
            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            org.bukkit.scoreboard.Team team;

            if (board.getTeam(player.getName()) == null) {
                team = board.registerNewTeam(player.getName());
            } else {
                team = board.getTeam(player.getName());
            }
            team.setPrefix("");
            team.addPlayer(player);
    }

    public boolean isWorldBorder() {
        return worldBorder;
    }

    public void setWorldBorder(boolean worldBorder) {
        this.worldBorder = worldBorder;
    }

    public static GameData getInstance(){
        if (instance== null){
            instance=new GameData();
        }
        return instance;
    }

    public GameMode getIngameMode() {
        return ingameMode;
    }

    public void setIngameMode(GameMode ingameMode) {
        this.ingameMode = ingameMode;
    }

    public GameMode getLobbyMode() {
        return lobbyMode;
    }

    public void setLobbyMode(GameMode lobbyMode) {
        this.lobbyMode = lobbyMode;
    }

    public boolean isPreventBlockBreaking() {
        return preventBlockBreaking;
    }

    public void setPreventBlockBreaking(boolean preventBlockBreaking) {
        this.preventBlockBreaking = preventBlockBreaking;
    }
}
