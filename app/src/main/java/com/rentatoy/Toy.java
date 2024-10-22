package com.rentatoy;

public class Toy {
    public Toy() {
    }

    public int toyID;
    public String toyName;
    public Long toyRent;
    public String toyCategory;
    public boolean isAvailable;
    public String toyAgeGroup;
    public String toyDesc;
    public String toyImage;

    public Toy(int toyID, String toyName, Long toyRent, String toyCategory, boolean isAvailable, String toyAgeGroup, String toyDesc, String toyImage) {
        this.toyID = toyID;
        this.toyName = toyName;
        this.toyRent = toyRent;
        this.toyCategory = toyCategory;
        this.isAvailable = isAvailable;
        this.toyAgeGroup = toyAgeGroup;
        this.toyDesc = toyDesc;
        this.toyImage = toyImage;
    }


}
