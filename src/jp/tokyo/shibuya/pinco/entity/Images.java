package jp.tokyo.shibuya.pinco.entity;

public class Images {
	
	public String lowResolutionUrl;
	public String lowResolutionWidth;
	public String lowResolutionHeight;
	
	public String thumbnailUrl;
	public String thumbnailWidth;
	public String thumbnailHeight;
	
	public String standardResolutionUrl;
	public String standardResolutionWidth;
	public String standardResolutionHeight;
	
	
	public void setLowResolution(String url, String width, String height) {
		this.lowResolutionUrl = url;
		this.lowResolutionWidth = width;
		this.lowResolutionHeight = height;
	}
	
	public void setThumbnail(String url, String width, String height) {
		this.thumbnailUrl = url;
		this.thumbnailWidth = width;
		this.thumbnailHeight = height;
	}
	
	public void setStandardResolution(String url, String width, String height) {
		this.standardResolutionUrl = url;
		this.standardResolutionWidth = width;
		this.standardResolutionHeight = height;
	}
}
