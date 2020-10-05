package tomuchrain;

public class DataWeather {
	public String weatherType;
	public int start;
	
	public DataWeather(String type, int time){
		this.weatherType = type;
		this.start = time;
	}
	
	@Override
	public String toString()
    {
        return "{" + weatherType + "," + start + "}";
    }
}

