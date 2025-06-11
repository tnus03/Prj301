/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
public class Feature {
    private int featureID;
    private String featureName;
    private String url;
    private String description;
    
    public Feature() {}
    
    public Feature(int featureID, String featureName, String url) {
        this.featureID = featureID;
        this.featureName = featureName;
        this.url = url;
    }
    
    public int getFeatureID() { return featureID; }
    public void setFeatureID(int featureID) { this.featureID = featureID; }
    
    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
