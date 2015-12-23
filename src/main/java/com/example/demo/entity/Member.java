package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="MEMBER")
public class Member {
	
	@Id
	@GeneratedValue(generator="MEMBER_SEQ_GENERATOR", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize=1, initialValue=1, name="MEMBER_SEQ_GENERATOR", sequenceName="MEMBER_SEQ")
	private long id;
	
	@NotNull
	@Size(min=1, max=256)
	@Pattern(regexp = "[a-zA-Z ']*")
	@Column(name="FIRST_NAME", length=256) 
	private String firstName;
	
	@NotNull
	@Size(min=1, max=256)
	@Pattern(regexp = "[a-zA-Z ']*")
	@Column(name="LAST_NAME", length=256) 
	private String lastName;
	
	@NotNull
	//@Pattern(regexp = "[0-9]*")
	@Column(name="AGE") 
	private String age;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	
}
