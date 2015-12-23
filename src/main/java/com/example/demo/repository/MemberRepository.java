package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.example.demo.entity.Member;

@Stateless
public class MemberRepository {

	@PersistenceContext(unitName="member_pu")
	EntityManager em;
	
	public long addMember(Member member) {
		em.persist(member);
		return member.getId();
	}
	
	public long updateMember(Member member) {
		em.merge(member);
		return member.getId();
	}
	
	/*public Member findMemberById(long memberId) {
		return em.find(Member.class, memberId);
	}*/
	
	public boolean removeMember(long memberId) {
		Optional<Member> member = findMemberById(memberId);
		if(member.isPresent()) {
			em.remove(member.get());
			return true;
		}
		return false;
	}
	
	public List<Member> findAll() {
		return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
	}
	
	public Optional<Member> findMemberById(long memberId) {
		return em.createQuery("SELECT m FROM Member m WHERE m.id = :id", Member.class)
				 .setParameter("id", memberId)
				 .getResultList()
				 .stream()
				 .findFirst();
	}
	
}
