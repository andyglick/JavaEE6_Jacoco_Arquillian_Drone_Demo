package com.example.demo.util;

import com.example.demo.entity.Member;

public class MemberBuilder {
	private Member member = new Member();
	public static MemberBuilder newMember() {
		return new MemberBuilder();
	}
	
	public MemberBuilder id(long id) {
		this.member.setId(id);
		return this;
	}
	
	public MemberBuilder age(String age) {
		this.member.setAge(age);
		return this;
	}
	
	public MemberBuilder firstName(String firstName) {
		this.member.setFirstName(firstName);
		return this;
	}
	
	public MemberBuilder lastName(String lastName) {
		this.member.setLastName(lastName);
		return this;
	}
	
	public Member build() {
		return this.member;
	}
}
