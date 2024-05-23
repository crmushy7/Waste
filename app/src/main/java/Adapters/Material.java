package Adapters;

public class Material {
    private String description;
    private double latitude;
    private double longitude;

    public Material(String description, double latitude, double longitude) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

