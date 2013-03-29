package com.example.myapp;

public class Item {
	private String text;
	private String hd;
	private String imgUrl;
	private String ltn;
	public Item(String text, String hd, String imgUrl, String ltn) {
		super();
		this.text = text;
		this.hd = hd;
		this.imgUrl = imgUrl;
		this.ltn = ltn;
	}
	public Item() {
		
	}
	public Item(Item itr) {
		this.text = String.valueOf(itr.text);
		this.hd = String.valueOf(itr.hd);
		this.imgUrl = String.valueOf(itr.imgUrl);
		this.ltn = String.valueOf(itr.ltn);
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setHd(String hd) {
		this.hd = hd;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public void setLtn(String ltn) {
		this.ltn = ltn;
	}
	public String getText() {
		return text;
	}
	public String getHd() {
		return hd;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public String getLtn() {
		return ltn;
	}
}