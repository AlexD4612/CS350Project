package Tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;



public class BombTest {

	
//-------------------Unit Tests-------------------------------------//
	@Test
	public void TestWind(){
		Bomb bomb = new Bomb(10, 10, 20, 5, null, 10, 45, 10);
		double expectedDirection = 45.0;
		double actualDirection = bomb.getWindDirection();
		
		double expectedSpeed = 10.0;
		double actualSpeed = bomb.getWindSpeed();
		
		assertEquals(expectedSpeed,actualSpeed,0.0001,"getWindSpeed()");
		assertEquals(expectedDirection,actualDirection,0.0001,"getWindDirection()");
		
	}
	
	@Test
	public void TestRelease() {
		//TODO
		
	}
	@Test
	public void TestError() {
		//TODO
		
	}
	
//---------------------Behavioral Tests-------------------------------//
	
	
}
