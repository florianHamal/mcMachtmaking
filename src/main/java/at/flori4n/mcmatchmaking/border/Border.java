package at.flori4n.mcmatchmaking.border;

import at.flori4n.mcmatchmaking.McMatchmaking;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Random;

public class Border {
    private static World world;
    private static float startingSize,damage;
    private static int warinigDistance = 10;
    private static long speed;
    private static int task;
    private static Location center;
    private static ArrayList<Location> endPoints = new ArrayList<Location>();
    private static Location currLoc;
    public static void start(){
        int randInt  =new Random().nextInt(endPoints.size());
        Location endPoint = endPoints.get(randInt);
        world.getWorldBorder().setCenter(center);
        world.getWorldBorder().setDamageAmount(damage);
        world.getWorldBorder().setSize(startingSize);
        world.getWorldBorder().setWarningDistance(warinigDistance);

        currLoc=world.getWorldBorder().getCenter();

        world.getWorldBorder().setSize(0,speed);//close border



        //distance the border has to travel to set speed of the movement
        int distX = (int) (world.getWorldBorder().getCenter().getX() - endPoint.getX());
        if (distX<0)distX*=-1;

        int distY = (int) (world.getWorldBorder().getCenter().getY() - endPoint.getY());
        if (distY<0)distY*=-1;

        int movmentSpeed = (int) (speed/(distX+distY));

        //moveBorderToEndPoint
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmaking.getPlugin(), new Runnable() {
            @Override
            public void run() {

                //if border center=! endpoint --> endpoint move to center
                if ((int)world.getWorldBorder().getCenter().getX()+2<(int) endPoint.getX()){
                    currLoc.setX(currLoc.getX()+1);
                    world.getWorldBorder().setCenter(currLoc);
                }else if ((int)world.getWorldBorder().getCenter().getX()>(int) endPoint.getX()){
                    currLoc.setX(currLoc.getX()-1);
                    world.getWorldBorder().setCenter(currLoc);
                }else if ((int)world.getWorldBorder().getCenter().getZ()+2<(int) endPoint.getZ()){
                    currLoc.setZ(currLoc.getZ()+1);
                    world.getWorldBorder().setCenter(currLoc);
                }else if ((int)world.getWorldBorder().getCenter().getZ()>(int) endPoint.getZ()){
                    currLoc.setZ(currLoc.getZ()-1);
                    world.getWorldBorder().setCenter(currLoc);
                }else {
                    world.getWorldBorder().setCenter(endPoint);
                    Bukkit.getScheduler().cancelTask(task);
                }

            }
        },0,20*movmentSpeed);
    }


    public static void reset(){
        if(world==null)return;
        world.getWorldBorder().reset();
    }
    public static void stop(){
        world.getWorldBorder().setSize(world.getWorldBorder().getSize());
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
        Border.world = world;
    }

    public static float getStartingSize() {
        return startingSize;
    }

    public static void setStartingSize(float startingSize) {
        Border.startingSize = startingSize;
    }

    public static float getDamage() {
        return damage;
    }

    public static void setDamage(float damage) {
        Border.damage = damage;
    }

    public static int getWarinigDistance() {
        return warinigDistance;
    }

    public static void setWarinigDistance(int warinigDistance) {
        Border.warinigDistance = warinigDistance;
    }

    public static long getSpeed() {
        return speed;
    }

    public static void setSpeed(long speed) {
        Border.speed = speed;
    }

    public static ArrayList<Location> getEndPoints() {
        return endPoints;
    }

    public static void setEndPoints(ArrayList<Location> endPoints) {
        Border.endPoints = endPoints;
    }

    public static Location getCenter() {
        return center;
    }

    public static void setCenter(Location center) {
        Border.center = center;
    }
}
