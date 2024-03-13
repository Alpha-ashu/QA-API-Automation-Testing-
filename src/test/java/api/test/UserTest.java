package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoint.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest 
{
	Faker faker;
	User userPayload;
	public Logger logger;
	@BeforeClass
	public void setupData() 
	{
		faker=new Faker();
		userPayload=new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger=LogManager.getLogger(this.getClass());
	}
	@Test(priority=1)
	public void testPostUser() 
	{
		logger.info("***********Creating user***********");
		Response response= UserEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),(200));
		logger.info("***********User Created***********");
	}
	
	@Test(priority=2)
	public void testGetUserByName()
	{
		logger.info("***********Reading user***********");
		Response response=UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***********User Info Displayed***********");
		
	}
	@Test(priority=3)
	public void testUpdateUserByName() 
	{
		//Updating the data
		logger.info("***********Updating user data***********");
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response= UserEndPoints.updateUser (this.userPayload.getUsername(),userPayload);
		response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(), 200);
		// Checking the data after update
		Response responseafterUpdate= UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseafterUpdate.getStatusCode(), 200);
		logger.info("***********User Data Updated***********");
		
	}@Test(priority=4)
	public void testDeleteUserByName() 
	{
		logger.info("***********Deleting User***********");
		Response response=UserEndPoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***********User Data Deleted***********");
	}
}








