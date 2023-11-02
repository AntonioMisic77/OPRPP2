package hr.fer.zemris.java.tecaj_13.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="blog_user")
public class BlogUser {

	@Id @GeneratedValue
	private Long id;
	
	@Column(length=100,nullable=false)
	private String firstName;
	
	@Column(length=100,nullable=false)
	private String lastName;
	
	@Column(length=50,nullable=false,unique=true)
	private String nick;
	
	@Column(length=100,nullable=false)
	private String email;
	
	@Column(length=100,nullable=false)
	private String passwordHash;
	
	@OneToMany(mappedBy="creator",fetch=FetchType.LAZY,cascade=CascadeType.PERSIST, orphanRemoval=true)
	private List<BlogEntry> entries;
	
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY,cascade=CascadeType.PERSIST, orphanRemoval=true)
	private List<BlogMessage> messages;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public List<BlogEntry> getEntries(){
		return this.entries;
	}
	
	public List<BlogMessage> getMessages(){
		return this.messages;
	}
}
