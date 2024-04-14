package Adapters;

import java.util.List;

public class ImageModel {
    private List<String> imageUrls;

    public ImageModel() {
        // Default constructor required for Firestore
    }

    public ImageModel(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
