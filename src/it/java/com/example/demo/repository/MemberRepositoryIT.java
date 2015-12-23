package com.example.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demo.entity.Member;
import com.example.demo.util.MemberBuilder;

@RunWith(Arquillian.class)
public class MemberRepositoryIT {

	@Deployment
	public static Archive<WebArchive> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
						 .addClasses(MemberRepository.class, Member.class, MemberBuilder.class)
						 .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
						 .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
	}

	@Inject
	MemberRepository repository;
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet(value="datasets/add-expected-members.yml", excludeColumns={"id"})
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testAddMember() {
		Member member = MemberBuilder.newMember()
									 .age("68")
									 .firstName("Steven")
									 .lastName("Spielberg")
									 .build();
		repository.addMember(member);
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet("datasets/update-expected-members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testUpdateMember() {
		repository.updateMember(MemberBuilder.newMember()
											 .id(100)
											 .age("30")
											 .firstName("Scott")
											 .lastName("Adams")
											 .build());
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet("datasets/delete-expected-members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testDeleteMember() {
		repository.removeMember(101);	
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testFindMemberById() {
		Member member = repository.findMemberById(100).get();	
		assertEquals("30", member.getAge());
		assertEquals("John",member.getFirstName());
		assertEquals("Smith",member.getLastName());
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testFindMemberByUnknownId() {
		Optional<Member> member = repository.findMemberById(200);	
		assertFalse(member.isPresent());
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testFindAllMembers() {
		assertEquals(2, repository.findAll().size());
	}
	
	@After
	public void cleanUp() throws InterruptedException {
		//Thread.sleep(10000);
	}
	
	
}
