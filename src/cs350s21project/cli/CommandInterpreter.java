package cs350s21project.cli;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cs350s21project.controller.*;
import cs350s21project.controller.command.actor.CommandActorCreateActor;
import cs350s21project.controller.command.actor.CommandActorDefineShip;
import cs350s21project.controller.command.munition.CommandMunitionDefineBomb;
import cs350s21project.controller.command.munition.CommandMunitionDefineShell;
import cs350s21project.controller.command.sensor.CommandSensorDefineRadar;
import cs350s21project.controller.command.view.*;
import cs350s21project.datatype.*;
public class CommandInterpreter {

	private AgentID id;
	private int size;
	private String objectType;
	private String subType;
	
	private Latitude setLatitude(String str) {
		String [] latData = str.split("[*#'\"]");
		return new Latitude(Integer.parseInt(latData[0]), Integer.parseInt(latData[1]),Double.parseDouble(latData[2]));
	}
	
	private Longitude setLongitude(String str) {
		String [] lonData = str.split("[*#'\"]");
		return new Longitude(Integer.parseInt(lonData[0]), Integer.parseInt(lonData[1]),Double.parseDouble(lonData[2]));
	}
	
	private FieldOfView setFieldOfView(String degree) {
		AngleNavigational angleDegree = new AngleNavigational(Double.parseDouble(degree));
		return new FieldOfView(angleDegree);
	}
	
	private Power setPower(String power) {
		return new Power(Double.parseDouble(power));
	}
	
	private Sensitivity setSensitivity(String sensitivity) {
		return new Sensitivity(Double.parseDouble(sensitivity));
	}
	private CoordinateWorld3D setCoordinates(String coordinates) {
		String[] coords = coordinates.split("/");
		Latitude lat = setLatitude(coords[0]);
		Longitude lon = setLongitude(coords[1]);
		Altitude alt = new Altitude(Integer.parseInt(coords[2]));
		CoordinateWorld3D coordinatesReal = new CoordinateWorld3D(lat, lon, alt);
		
		return coordinatesReal;
		
	}
	
	public void evaluate(String commandText) {
		String originalCommandText = commandText;
		commandText=commandText.toLowerCase(); // lower case to allow any input in weird casing
		commandText.trim(); //trim all whitespace
		ArrayList <String> argumentList = new ArrayList<>();
		while(!commandText.isEmpty()) {//Splits the string based on whitespace.
			String temp =(commandText.contains(" ")) ? commandText.substring(0,commandText.indexOf(" ")+1).trim():commandText; //indexOf(" ")+1 will remove the whitespace at the start of the string.
			temp = temp.replaceAll("[()]", ""); //Remove excess parameters.
			argumentList.add(temp);
			commandText = (commandText.contains(" ")) ? commandText.substring(commandText.indexOf(" ")+1).trim() : "";
		}
		argumentList.removeIf(n -> (n.isEmpty())); //Removes any blank spaces that might of been made during the while loop.
		argumentList.trimToSize();
		String commandType = argumentList.get(0);
		CommandManagers cmd = CommandManagers.getInstance();
		switch(commandType) {	
		//--------Create Commands------------\\
		case "create":
			objectType = argumentList.get(1);
			switch(objectType) {
			case "window":
				id = new AgentID(argumentList.get(2));
				String viewType = argumentList.get(3);
				if(viewType.equals("top")) {
					//index 4 and 5 are skipped since they hold arbitrary values ('with','view')
					size = Integer.parseInt(argumentList.get(6));
					Latitude latOrigin = setLatitude(argumentList.get(7));
					Latitude latExtent = setLatitude(argumentList.get(8));
					Latitude latInterval = setLatitude(argumentList.get(9));
					Longitude longOrigin = setLongitude(argumentList.get(10));
					Longitude longExtent = setLongitude(argumentList.get(11));
					Longitude longInterval = setLongitude(argumentList.get(12));
					cmd.schedule(new CommandViewCreateWindowTop(cmd,originalCommandText,id,size,latOrigin,latExtent,latInterval,longOrigin,longExtent,longInterval));
				}else if(viewType.equals("front")) {
					System.out.println("Front view accessed, but not yet ready.");
				}else if(viewType.equals("side")) {
					System.out.println("Side view accessed, but not yet ready.");
				
				}else 
					throw new RuntimeException("Invalid view type for create window.");
				break;
			case "actor":
				id = new AgentID(argumentList.get(2));
				AgentID fromId = new AgentID(argumentList.get(4));
				CoordinateWorld3D coordinates = setCoordinates(argumentList.get(6));
				Course course = new Course(Integer.parseInt(argumentList.get(9)));
				Groundspeed speed = new Groundspeed(Integer.parseInt(argumentList.get(11)));
				cmd.schedule(new CommandActorCreateActor(cmd,originalCommandText,id,fromId,coordinates,course,speed));
				System.out.printf("Actor %s from %s at %s with course %s and speed %s created", id.getID(),fromId.getID(),coordinates.toString(),course.toString(),speed.toString());
				break;
			}//End of create objectType switch
			break;
		case "define":
			objectType = argumentList.get(1);
			switch(objectType) {
			case "ship":
				id = new AgentID(argumentList.get(2));
				//index = 3, munition(s) = 4
				List<AgentID> munitionList = argumentList.subList(5,argumentList.size()).stream().map(n -> new AgentID(n)).collect(Collectors.toList());
				cmd.schedule(new CommandActorDefineShip(cmd,originalCommandText,id,munitionList));
				System.out.print("Ship: " + id.getID() + " has been created with munitions: ");
				munitionList.stream().forEach((n) -> System.out.print(n.getID() + " "));
				System.out.println();
				break;
	//--------Munition Commands------------\\
			case "munition":
				subType = argumentList.get(2); //Types of munition, i.e. bomb,shell,charge
				switch(subType) {
				case "bomb":
					id = new AgentID(argumentList.get(3));
					cmd.schedule(new CommandMunitionDefineBomb(cmd,originalCommandText,id));
					System.out.printf(subType+" %s "+"created%n",id.getID());
					break;
				case "shell":
					System.out.println("Shell created");
					id = new AgentID(argumentList.get(3));
					cmd.schedule(new CommandMunitionDefineShell(cmd,originalCommandText,id));
					System.out.printf(subType+" %s "+"created%n",id.getID());
					break;
				case "depth_charge":
					//TODO
					break;
				case "missile":
					//TODO
					break;
				case "torpedo":
					//TODO
					break;
				}
	//-------Sensor/Fuze Commands-----------\\
			case "sensor":
				subType = argumentList.get(2); //Types of sensors i.e. radar, thermal, acoustic
				switch(subType) {
				case "radar":
					id = new AgentID(argumentList.get(3));
					//with = 4, field = 5, of = 6, view = 7
					FieldOfView fov = this.setFieldOfView(argumentList.get(8));
					//power = 9
					Power power = this.setPower(argumentList.get(10));
					//sensitivity = 11
					Sensitivity sensitivity = this.setSensitivity(argumentList.get(12));
					cmd.schedule(new CommandSensorDefineRadar(cmd,originalCommandText,id,fov,power,sensitivity));
					System.out.printf("Radar Sensor %s has been made.%n",id.getID());
					break;
				case"thermal":
					//TODO
					break;
				case"acoustic":
					//TODO
					break;
				case "sonar":
					//TODO
					break;
				case "depth":
					//TODO
					break;
				case "distance":
					//TODO
					break;
				case "time":
					//TODO
					break;
				}
			} //End define commandType switch
			break;
	//---------MISC Commands-------------\\
		case "delete":
			//TODO
			break;
		case "set":
			//TODO
			break;
		case "@load":
			//TODO
			break;
		case "@wait":
			//TODO
			break;
		case"@pause":
			//TODO
			break;
		case "@set":
			//TODO
			break;
		case"@exit":
			//TODO
			break;
		}//End of switch(commandType)
	}//End of evaluate()
}
	
