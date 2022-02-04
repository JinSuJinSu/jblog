package com.poscoict.jblog.vo;

public class CategoryVo {
	
	private int no;
	private String name;
	private String descrpition;
	private String blogId;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescrpition() {
		return descrpition;
	}
	public void setDescrpition(String descrpition) {
		this.descrpition = descrpition;
	}
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	
	@Override
	public String toString() {
		return "CategoryVo [no=" + no + ", name=" + name + ", descrpition=" + descrpition + ", blogId=" + blogId + "]";
	}
	
	

	
	
	
	

}
