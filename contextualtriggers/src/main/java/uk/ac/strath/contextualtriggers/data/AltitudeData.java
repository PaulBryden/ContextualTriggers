package uk.ac.strath.contextualtriggers.data;

public class AltitudeData extends  AbstractData
{
    private final double altitude;

    public AltitudeData(double altitude) {
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
