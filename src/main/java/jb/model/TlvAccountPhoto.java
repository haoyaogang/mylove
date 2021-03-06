
/*
 * @author John
 * @date - 2015-07-06
 */

package jb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "lv_account_photo")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TlvAccountPhoto implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "LvAccountPhoto";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_OPEN_ID = "用户openId";
	public static final String ALIAS_PHOTO_IMG = "照片路径";
	public static final String ALIAS_AUDIT_STATUS = "审核状态";
	public static final String ALIAS_CREATE_TIME = "上传时间";
	
	//date formats
	public static final String FORMAT_CREATE_TIME = jb.util.Constants.DATE_FORMAT_For_Entity;
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//@Length(max=36)
	private java.lang.String id;
	//@NotNull 
	private java.lang.Integer openId;
	//@NotBlank @Length(max=100)
	private java.lang.String photoImg;
	//@NotBlank @Length(max=20)
	private java.lang.String auditStatus;
	//@NotNull 
	private java.util.Date createTime;
	//columns END


		public TlvAccountPhoto(){
		}
		public TlvAccountPhoto(String id) {
			this.id = id;
		}
	

	public void setId(java.lang.String id) {
		this.id = id;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 36)
	public java.lang.String getId() {
		return this.id;
	}
	
	@Column(name = "openId", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getOpenId() {
		return this.openId;
	}
	
	public void setOpenId(java.lang.Integer openId) {
		this.openId = openId;
	}
	
	@Column(name = "auditStatus", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public java.lang.String getAuditStatus() {
		return this.auditStatus;
	}
	
	public void setAuditStatus(java.lang.String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Column(name = "photoImg", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public java.lang.String getPhotoImg() {
		return this.photoImg;
	}
	
	public void setPhotoImg(java.lang.String photoImg) {
		this.photoImg = photoImg;
	}
	

	@Column(name = "createTime", unique = false, nullable = false, insertable = true, updatable = true, length = 19)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	/*
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("OpenId",getOpenId())
			.append("PhotoImg",getPhotoImg())
			.append("CreateTime",getCreateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof LvAccountPhoto == false) return false;
		if(this == obj) return true;
		LvAccountPhoto other = (LvAccountPhoto)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}*/
}

