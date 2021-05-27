package Tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import Tests.Bomb.Coordinates;
import Tests.Bomb.E_ErrorType;



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
		Bomb bomb = new Bomb(100, 200, 300, 10, null, 0, 0, 0);
		Coordinates actualCoords = bomb.getReleaseCoordinates();
		double actualX = actualCoords.getX();
		double actualY = actualCoords.getY();
		double actualAlt = bomb.getReleaseAltitude();
		double actualDesc = bomb.getDescentSpeed();
		
		double expectedX = 100;
		double expectedY = 200;
		double expectedAlt = 300;
		double expectedDesc = 10;
		
		assertEquals(expectedX,actualX,0.0001,"getX()");
		assertEquals(expectedY,actualY,0.0001,"getY()");
		assertEquals(expectedAlt,actualAlt,0.0001,"getReleaseAlt()");
		assertEquals(expectedDesc,actualDesc,0.0001,"getDescentSpeed()");
		
	}
	@Test
	public void TestError() {
		Bomb bomb = new Bomb(100, 200, 300, 10, E_ErrorType.NONE, 10, 0, 0);
		Bomb bomb1 = new Bomb(100, 200, 300, 10,E_ErrorType.GAUSSIAN, 10, 0, 0);
		Bomb bomb2 = new Bomb(100, 200, 300, 10, E_ErrorType.UNIFORM, 10, 0, 0);
		
		E_ErrorType expectedError0 = E_ErrorType.NONE;
		E_ErrorType expectedError1 = E_ErrorType.GAUSSIAN;
		E_ErrorType expectedError2 = E_ErrorType.UNIFORM;
		
		double actualErrorRange = bomb.getErrorRange();
		double expectedErrorRange =10;
		
		E_ErrorType actualError0 = bomb.getErrorType();
		E_ErrorType actualError1 = bomb1.getErrorType();
		E_ErrorType actualError2 = bomb2.getErrorType();
		
		assertEquals(expectedError0,actualError0);
		assertEquals(expectedError1,actualError1);
		assertEquals(expectedError2,actualError2);
		
		assertEquals(expectedErrorRange,actualErrorRange,0.0001,"getErrorRange()");
		
	}
	
//---------------------Behavioral Tests-------------------------------//
	
	
}
