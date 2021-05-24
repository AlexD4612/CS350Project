package Tests;
import org.junit.Test;

import Tests.Bomb.E_ErrorType;



public class BombTest {

	@Test
	public double TestWind(){
		Bomb bomb = new Bomb(10, 10, 20, 5, new E_ErrorType("NONE"), 0, 0, 0);
		return 0.0;
	}
	
	@Test
	public double TestRelease() {
		//TODO
		return 0.0;
	}
	@Test
	public double TestError() {
		//TODO
		return 0.0;
	}
	
}
