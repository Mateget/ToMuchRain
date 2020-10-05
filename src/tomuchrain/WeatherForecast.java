package tomuchrain;


import java.util.ArrayList;
import java.util.Date;



public class WeatherForecast {
	Date initDate;
	private int durationmin;
	private int durationmax;
	private int intervalmin;
	private int intervalmax;
	private boolean thunder;
	private int thunderRate;
	private ArrayList<DataWeather> weather = new ArrayList<DataWeather>();
	
	public WeatherForecast(boolean thunder, int thunderRate,int duramin, int duramax, int intermin, int intermax) {
		this.durationmin = duramin;
		this.durationmax = duramax;
		this.intervalmin = intermin;
		this.intervalmax = intermax;
		this.thunder = thunder;
		this.thunderRate = thunderRate;
		
	}
	
	public ArrayList<DataWeather> getDataWeather(){
		return this.weather;
	}
	
	public void setDate(Date d) {
		this.initDate = d;
	}
	
	// Create an Array with weather event listed for the next 24h
	public void defineDataWeather() {
		int currentime = 0;
		String lastWeather="rain";
		do {
			if (lastWeather=="clear") {
				DataWeather dataW;
				if (thunderRate>=(int)(Math.random()*100) && thunder){
					dataW = new DataWeather("thunder",currentime);
					lastWeather = "thunder";
				}
				else {
					dataW = new DataWeather("rain",currentime);
					lastWeather = "rain";
				}
				
				this.weather.add(dataW);
				currentime += durationmin + (int) (Math.random()*(durationmax-durationmin));
			}
			else if(lastWeather=="rain" || lastWeather=="thunder") {
				DataWeather dataW = new DataWeather("clear",currentime);
				lastWeather = "clear";
				this.weather.add(dataW);
				currentime += intervalmin + (int) (Math.random()*(intervalmax-intervalmin));
			}
			
		}
		while(currentime < 1728000); // 1728000 ticks = 24h 
		if(lastWeather=="rain") {
			this.weather.remove(this.weather.size()-1); //never rain at the end
		}
	}
	
}
