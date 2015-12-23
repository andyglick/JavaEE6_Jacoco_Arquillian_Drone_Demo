package com.example.demo.repository;

import java.io.File;
import java.net.URL;
import java.util.concurrent.locks.LockSupport;

import javax.inject.Inject;

import org.arquillian.extension.recorder.screenshooter.api.Screenshot;
import org.eclipse.persistence.annotations.CompositeMember;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.example.demo.entity.Member;
import com.example.demo.util.MemberBuilder;


@RunWith(Arquillian.class)
public class MemberWebClientTestIT {

	@Deployment(testable = false)
	public static Archive<WebArchive> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "demo.war")
						 .addPackages(true, "com.example.demo")
						 .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
						 .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
						 .addAsWebResource("create.html", "resources/create.html")
						 .addAsWebResource("js/jquery-2.1.4.js", "resources/js/jquery-2.1.4.js");
	}
	
	@ArquillianResource
	URL baseURL;
	
	@Drone
	WebDriver driver;
	
	@Test
	/*@Screenshot(
	        takeBeforeTest = false,
	        takeAfterTest = true,
	        takeWhenTestFailed = true)*/
	@ShouldMatchDataSet(value="datasets/add-web-expected-members.yml", excludeColumns={"id"})
	public void shouldBeAbleToAddMember() {
		Member member = MemberBuilder.newMember()
				 .age("68")
				 .firstName("Steven")
				 .lastName("Spielberg")
				 .build();
		driver.get(baseURL.toString()+"resources/create.html");
		LockSupport.parkNanos(999999999);
		driver.findElement(By.id("firstName")).sendKeys(member.getFirstName());
		LockSupport.parkNanos(999999999);
		driver.findElement(By.id("lastName")).sendKeys(member.getLastName());
		LockSupport.parkNanos(999999999);
		driver.findElement(By.id("age")).sendKeys(member.getAge());
		LockSupport.parkNanos(999999999);
		driver.findElement(By.id("submit")).click();
		LockSupport.parkNanos(999999999);
	}
}
