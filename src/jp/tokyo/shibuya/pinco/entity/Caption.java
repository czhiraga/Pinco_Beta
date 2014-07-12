package jp.tokyo.shibuya.pinco.entity;

public class Caption {
	
	public String id;
	public String createdTime;
	public String text;
	/** 投稿ユーザー名 */
	public String userName;
	/** 投稿ユーザーID */
	public String userId;
	/** 投稿ユーザープロフィール */
	public String profilePicture;
	public String fullName;
	
	public void setCaption(String id, String createdTime, String text, String userName, String userId, String profilePicture, String fullName) {
		this.id = id;
		this.createdTime = createdTime;
		this.text = text;
		this.userName = userName;
		this.userId = userId;
		this.profilePicture = profilePicture;
		this.fullName = fullName;
	}
}
