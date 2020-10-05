package tomuchrain;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class ToMuchRain extends JavaPlugin{
	// Fired when plugin is first enabled
	public void main() {
		
	}
	
	FileConfiguration config = getConfig();
	private int durationmin;
	private int durationmax;
	private int intervalmin;
	private int intervalmax;
	private boolean thunder;
	private int thunderRate;
	private boolean displayinchat;
	private boolean weatherForecast;
	private String rainmessage;
	private String clearrainmessage;
	private String clearthundermessage;
	private String thundermessage;
	private String worldname;
	Date d; 
    @Override
    public void onEnable() {
    	this.loadConfig();
    	d = new Date(System.currentTimeMillis());
    	WeatherForecast rainManager = new WeatherForecast(this.thunder,this.thunderRate,this.durationmin,this.durationmax,this.intervalmin,this.intervalmax);
    	rainManager.setDate(d);
    	rainManager.defineDataWeather();
    	List<DataWeather> weather = rainManager.getDataWeather();
    	if(weatherForecast) { 
    		this.consolForcast(weather);
    	}
    	if(Bukkit.getWorld(worldname) == null) {
			System.out.println("[ToMuchRain] World: "+ worldname +" do not exist");
		}
    	else {
    		for (int i = 0 ; i < weather.size() ; i++) {
        		DataWeather weatherData = weather.get(i);
        		new BukkitRunnable() {
        		    public void run() {
        		    	switch(weatherData.weatherType) {
        		    		case "rain" :
            		    		if(displayinchat) {
            		    			Bukkit.broadcastMessage(ChatColor.GRAY + rainmessage);
            		    		}
            		    		Bukkit.getWorld(worldname).setStorm(true);
        		    			Bukkit.getWorld(worldname).setThundering(false);
        		    			break;
        		    		case "thunder" :
        		    			if(displayinchat) {
        		    				Bukkit.broadcastMessage(ChatColor.GRAY + thundermessage);
        		    			}
        		    			Bukkit.getWorld(worldname).setStorm(true);
        		    			Bukkit.getWorld(worldname).setThundering(true);
        		    			break;
        		    		default :
        		    			if(displayinchat) {
        		    				if(Bukkit.getWorld(worldname).isThundering()) {
        		    					Bukkit.broadcastMessage(ChatColor.GRAY + clearthundermessage);
        		    				}
        		    				else {
        		    					Bukkit.broadcastMessage(ChatColor.GRAY + clearrainmessage);
        		    				}
        		    			}
        		    			Bukkit.getWorld(worldname).setStorm(false);
        		    			Bukkit.getWorld(worldname).setThundering(false);
        		    			break;
        		    	}
        		    }
        		}.runTaskLater(this,weatherData.start); // Task executed later after X tick
        	}
    	}
    	
    	
    	
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
    // Display Weather Forecast in chat
    public void consolForcast(List<DataWeather> weather) {
    	System.out.println("[ToMuchRain] Next 24h weather : ");
    	for (int i = 0 ; i < weather.size()-1 ; i++) {
    		if (weather.get(i).weatherType=="rain" || weather.get(i).weatherType=="thunder")
    			System.out.println("[ToMuchRain] " + weather.get(i).weatherType + " from "+addTickToDate(d,weather.get(i).start)+" to "+ addTickToDate(d,weather.get(i+1).start)+" duration : "+durationOfRain(weather.get(i).start, weather.get(i+1).start));
    	}
    }
    //  Time calculation, adding tick to date
    public String addTickToDate(Date d, int tick) {
    	String time = "";
    	@SuppressWarnings("deprecation")
		int hours = (d.getHours()+(tick/20)/3600)%24;
    	if (hours<10) {
    		time +="0"+Integer.toString(hours)+"h";
    	}
    	else {
    		time +=Integer.toString(hours)+"h";
    	}
    	@SuppressWarnings("deprecation")
    	int minutes = (d.getMinutes()+(tick/20)/60)%60;
    	if (minutes<10) {
    		time +="0"+Integer.toString(minutes);
    	}
    	else {
    		time +=Integer.toString(minutes);
    	}
    	return time;
    }
    
    // Calculate the duration a weather event
    public String durationOfRain(int tick1, int tick2) {
    	String time = "";
    	int minutes = (((tick2-tick1)/20)/60)%60;
    	if (minutes<10) {
    		time +="0"+Integer.toString(minutes)+" minutes";
    	}
    	else {
    		time +=Integer.toString(minutes)+" minutes";
    	}
    	return time;
    }
    
    // Load config file, set default values if it's the first time or if they are in wrong format ( Ex : Int => String )
    public void loadConfig() {
    	final int defaultdurationmin = 600;
    	final int defaultdurationmax = 1200;
    	final int defaultintervalmin = 3600;
    	final int defaultintervalmax= 8400;
    	final boolean defaultthunder = true;
    	final int defaultthunderrate = 15;
    	final boolean defaultdisplayinchat = true;
    	final boolean defaultweatherForecast = true;
    	final String defaultrainmessage = "It starts raining";
    	final String defaultclearrainmessage = "Rain stops";
    	final String defaultclearthundermessage = "Storm stops";
    	final String defaultthundermessage = "Storm begins";
    	final String defaultworldname = "world";
    	/////////////////////
    	if(config.isSet("durationmin") && config.isInt("durationmin")) {
    		this.durationmin = config.getInt("durationmin")*20;
    	}
    	else {
    		this.durationmin = defaultdurationmin*20;
    		config.set("durationmin", defaultdurationmin);
    	}
    	///////////////////////
    	if(config.isSet("durationmax") && config.isInt("durationmax")) {
    		this.durationmax = config.getInt("durationmax")*20;
    	}
    	else {
    		this.durationmax = defaultdurationmax*20;
    		config.set("durationmax", defaultdurationmax);;
    	}
    	///////////////////////
    	if(config.isSet("intervalmin") && config.isInt("intervalmin")) {
    		this.intervalmin = config.getInt("intervalmin")*20;
    	}
    	else {
    		this.intervalmin = defaultintervalmin*20;
    		config.set("intervalmin", defaultintervalmin);;
    	}
    	///////////////////////
    	if(config.isSet("intervalmax") && config.isInt("intervalmax")) {
    		this.intervalmax = config.getInt("intervalmax")*20;
    	}
    	else {
    		this.intervalmax = defaultintervalmax*20;
    		config.set("intervalmax", defaultintervalmax);;
    	}
    	///////////////////////
    	if(config.isSet("thunderAllowed") && config.isBoolean("thunderAllowed")) {
    		this.thunder = config.getBoolean("thunderAllowed");
    	}
    	else {
    		this.thunder = defaultthunder;
    		config.set("thunderAllowed", defaultthunder);
    	}
    	///////////////////////
    	if(config.isSet("thunderRate") && config.isInt("thunderRate")) {
    		this.thunderRate = config.getInt("thunderRate");
    	}
    	else {
    		this.thunderRate = defaultthunderrate;
    		config.set("thunderRate", defaultthunderrate);
    	}
    	///////////////////////
    	if(config.isSet("displayInChat") && config.isBoolean("displayInChat")) {
    		this.displayinchat = config.getBoolean("displayInChat");
    	}
    	else {
    		this.displayinchat = defaultdisplayinchat;
    		config.set("displayInChat", defaultdisplayinchat);
    	}
    	///////////////////////
    	if(config.isSet("weatherForecast") && config.isBoolean("weatherForecast")) {
    		this.weatherForecast = config.getBoolean("weatherForecast");
    	}
    	else {
    		this.weatherForecast = defaultweatherForecast;
    		config.set("weatherForecast", defaultweatherForecast);
    	}
    	///////////////////////
    	if(config.isSet("rainmessage") && config.isString("rainmessage")) {
    		this.rainmessage = config.getString("rainmessage");
    	}
    	else {
    		this.rainmessage = defaultrainmessage;
    		config.set("rainmessage", defaultrainmessage);
    	}
    	///////////////////////
    	if(config.isSet("clearrainmessage") && config.isString("clearrainmessage")) {
    		this.clearrainmessage = config.getString("clearrainmessage");
    	}
    	else {
    		this.clearrainmessage = defaultclearrainmessage;
    		config.set("clearrainmessage", defaultclearrainmessage);
    	}
    	///////////////////////
    	if(config.isSet("clearthundermessage") && config.isString("clearthundermessage")) {
    		this.clearthundermessage = config.getString("clearthundermessage");
    	}
    	else {
    		this.clearthundermessage = defaultclearthundermessage;
    		config.set("clearthundermessage", defaultclearthundermessage);
    	}
    	///////////////////////
    	if(config.isSet("thundermessage") && config.isString("thundermessage")) {
    		this.thundermessage = config.getString("thundermessage");
    	}
    	else {
    		this.thundermessage = defaultthundermessage;
    		config.set("thundermessage", defaultthundermessage);
    	}
    	///////////////////////
    	if(config.isSet("worldname") && config.isString("worldname")) {
    		this.worldname = config.getString("worldname");
    	}
    	else {
    		this.worldname = defaultworldname;
    		config.set("worldname", defaultworldname);
    	}
    	///////////////////////
        config.options().copyDefaults(true);
        saveConfig();
    }
           

}
