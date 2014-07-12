package jp.tokyo.shibuya.pinco.dto;

import java.util.ArrayList;

import jp.tokyo.shibuya.pinco.entity.FeedEntity;

public class FeedDto {
	
	public String nextUrl;
	public String nextMaxId;
	public String code;
	public ArrayList<FeedEntity> dataList;
	
}
