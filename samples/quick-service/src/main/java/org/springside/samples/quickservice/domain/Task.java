package org.springside.samples.quickservice.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;
import org.springside.modules.domain.IdEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

// JPA实体类标识
@Entity
@Table(name = "ss_task")
public class Task extends IdEntity {
	private String title;
	private String description;
	private User user;

	// Bean Validator的校验规则
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// 基于user_id列的多对一关系定义.
	@ManyToOne
	@JoinColumn(name = "user_id")
	// JSON输出时忽略本字段
	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// 持久化时忽略本字段，仅用于JSON输出
	@Transient
	@JsonProperty("user")
	public String getUserName() {
		return user.getName();
	}

	// 仅用于客户端解释JSON时使用
	public void setUserName(String name) {
		user = new User();
		user.setName(name);
	}
}
