package hr.fer.zemris.java.tecaj_13.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="blog_message")
public class BlogMessage {

	private Long id;
	private String content;
	private BlogUser user;
	
	
	@Id @GeneratedValue
	private Long getId() {
		return this.id;
	}
	
	@Column(nullable=false,length=100)
	private String getContent() {
		return this.content;
	}
	
	
	@ManyToOne
	@JoinColumn(nullable=true)
	private BlogUser getBlogUser() {
		return this.user;
	}
	
	
	
	public void setContent(String content) {
		this.content = content;
	}
}
