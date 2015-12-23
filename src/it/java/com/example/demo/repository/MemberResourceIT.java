package com.example.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demo.entity.Member;

@RunWith(Arquillian.class)
public class MemberResourceIT {
	
	private WebTarget target;

	@Deployment
	public static Archive<WebArchive> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
						 .addPackages(true, "com.example.demo")
						 .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
						 .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
	}
	
	@ArquillianResource
	URL baseURL;
	
	@Before
	public void setup() {
		target = ClientBuilder.newClient().target(baseURL.toString());
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet(value="datasets/add-expected-members.yml", excludeColumns={"id"})
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testAddMember() {
		Response response = target.path("api/members")
								  .request()
								  .post(Entity.entity(" {" + 
													  		"      \"id\": 0," + 
													  		"      \"firstName\" : \"Steven\"," + 
													  		"      \"lastName\" : \"Spielberg\"," + 
													  		"	  \"age\" : 68" + 
													  		"    }", 
											"application/vnd.qantas.add.member.v1+json"));
		assertEquals(201, response.getStatus());
		assertTrue(response.getHeaderString("Location").contains("/api/members/"));
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	public void testFindMemberById() {
		Response response = target.path("api/members/{id}")
								  .resolveTemplate("id", 100)
								  .request()
								  .accept("application/vnd.qantas.find.member.v1+json")
								  .get();
		assertEquals(200, response.getStatus());
		Member member = response.readEntity(Member.class);
		assertEquals("John", member.getFirstName());
		assertEquals("Smith", member.getLastName());
		assertEquals("30", member.getAge());
		
	}
	
	@Test
	public void testFindMemberByUnknownId() {
		Response response = target.path("api/members/{id}")
				  .resolveTemplate("id", 200)
				  .request()
				  .accept("application/vnd.qantas.find.member.v1+json")
				  .get();
		assertEquals(404, response.getStatus());
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet(value="datasets/update-expected-members.yml", excludeColumns={"id"})
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testUpdateMember() {
		Response response = target.path("api/members")
								  .request()
								  .put(Entity.entity(" {" + 
													  		"      \"id\": 100," + 
													  		"      \"firstName\" : \"Scott\"," + 
													  		"      \"lastName\" : \"Adams\"," + 
													  		"	  \"age\" : 30" + 
													  		"    }", 
											"application/vnd.qantas.update.member.v1+json"));
		assertEquals(201, response.getStatus());
		assertTrue(response.getHeaderString("Location").contains("/api/members/"));
	}
	
	@Test
	@UsingDataSet("datasets/members.yml")
	@ShouldMatchDataSet("datasets/delete-expected-members.yml")
	@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY, phase = TestExecutionPhase.AFTER)
	public void testRemoveMember() {
		Response response = target.path("api/members/{id}")
				  .resolveTemplate("id", 101)
				  .request()
				  .delete();
		assertEquals(200, response.getStatus());
	}
	
	
}
