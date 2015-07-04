package jb.pageModel;

import java.util.Date;

@SuppressWarnings("serial")
public class LvFeedback implements java.io.Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	private java.lang.String id;	
	private java.lang.String accountId;	
	private java.lang.String contactWay;	
	private java.lang.String content;	
	private Date createTime;			

	

	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	public java.lang.String getId() {
		return this.id;
	}

	
	public void setAccountId(java.lang.String accountId) {
		this.accountId = accountId;
	}
	
	public java.lang.String getAccountId() {
		return this.accountId;
	}
	public void setContactWay(java.lang.String contactWay) {
		this.contactWay = contactWay;
	}
	
	public java.lang.String getContactWay() {
		return this.contactWay;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	
	public java.lang.String getContent() {
		return this.content;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

}
