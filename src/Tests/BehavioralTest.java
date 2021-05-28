package Tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Tests.Bomb.Coordinates;
import Tests.Bomb.E_ErrorType;

public class BehavioralTest {
	private final double releaseX = 200, releaseY = 300, releaseAltitude = 1500, descentSpeed = 100;
	
	public void runNoWindNoErrorBombTest() {
		Bomb noWindNoErrorBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.NONE,0,0,0);
		ArrayList<double[]> res = runTest(noWindNoErrorBomb);
		writeToCSV(res,"no_wind_no_error_bomb_output");
	}
	
	public void runWindNoErrorBombTest() {
		Bomb noErrorWithWindBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.NONE,0,60,25);
		ArrayList<double[]> res = runTest(noErrorWithWindBomb);
		writeToCSV(res,"wind_no_error_bomb_output");
	}
	
	public void uniformErrorNoWindBombTest() {
		Bomb uniformErrorNoWindBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.UNIFORM,150,0,0);
		ArrayList<double[]> res = runTest(uniformErrorNoWindBomb);
		writeToCSV(res,"no_wind_uniform_error_bomb_output");
	}
	
	public void uniformErrorWithWindBombTest() {
		Bomb uniformErrorWithWindBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.UNIFORM,150,60,25);
		ArrayList<double[]> res = runTest(uniformErrorWithWindBomb);
		writeToCSV(res,"wind_uniform_error_bomb_output");
	}
	
	public void gaussianErrorWithNoWindBombTest() {
		Bomb gaussianErrorNoWindBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.GAUSSIAN,150,0,0);
		ArrayList<double[]> res = runTest(gaussianErrorNoWindBomb);
		writeToCSV(res,"no_wind_gaussian_error_bomb_output");
		
	}
	
	public void gaussianErrorWithWindBombTest() {
		Bomb gaussianErrorWithWindBomb = new Bomb(releaseX,releaseY,releaseAltitude,descentSpeed,E_ErrorType.GAUSSIAN,150,60,25);	
		ArrayList<double[]> res = runTest(gaussianErrorWithWindBomb);
		writeToCSV(res,"wind_gaussian_error_bomb_output");
	}
	
	private ArrayList<double[]> runTest(Bomb bomb){
		ArrayList<double[]> res = new ArrayList<>();
		for(int i = 0; i < 100; i++) {
			Coordinates c = bomb.drop();
			res.add( new double [] { c.getX(),c.getY() } );
		}
		return res;
	}
	
	private void writeToCSV(ArrayList<double[]> results,String resultName) {
		try {
			PrintWriter pw = new PrintWriter(new File("results\\" + resultName + ".csv"));
			for(double []  coordinates: results) {
				double x = coordinates[0], y = coordinates[1];
				pw.print(x + "," + y + "\n");
			}
			pw.print("STDEV X, STDEVY\n=STDEV(A1:A100),=STDEV(B1:B100)\n");
			pw.close();
		}catch(FileNotFoundException e) {
			System.out.println("File Not Found");
		}
	}
	
	public static void main(String [] args) {
		BehavioralTest bt = new BehavioralTest();
		bt.runNoWindNoErrorBombTest();
		bt.runWindNoErrorBombTest();
		bt.uniformErrorNoWindBombTest();
		bt.uniformErrorWithWindBombTest();
		bt.gaussianErrorWithNoWindBombTest();
		bt.gaussianErrorWithWindBombTest();
		System.out.println("All tests are finished, all the outputs will be found in the folder above src.");
	}
	
	

}
