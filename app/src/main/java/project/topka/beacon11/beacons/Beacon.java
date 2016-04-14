package project.topka.beacon11.beacons;

/**
 * Created by Chris on 12/4/16.
 */
public class Beacon
{
	public String creator;
	public String title;
	public double latitude;
	public double longitude;

	public Beacon(String c, String t, double lat, double longit) {
		this.creator = c;
		this.title = t;
		this.latitude = lat;
		this.longitude = longit;
	}
}