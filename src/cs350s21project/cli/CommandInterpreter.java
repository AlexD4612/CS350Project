package cs350s21project.cli;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import cs350s21project.controller.command.actor.*;
import cs350s21project.controller.command.munition.*;
import cs350s21project.controller.command.sensor.*;
import cs350s21project.controller.command.view.*;
import cs350s21project.controller.command.misc.*;
import cs350s21project.datatype.*;



public class CommandInterpreter {

	private AgentID id;
	private int size;
	private String objectType;
	private String subType;


	private AgentID fuzeId;
	private AgentID sensorId;
	private AgentID munitionId;
	private FieldOfView fov;
	private Power power;
	private Sensitivity sensitivity;
	


	
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
	
	private Time setTime(String time) {
		return new Time(Double.parseDouble(time));
	}
	
	public void evaluate(String commandText) {
		String [] commandArray = commandText.split(";");
		for(String command: commandArray)
				this.evaluateString(command);
	}
	
	private void evaluateString(String commandText) {
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
				System.out.printf("Actor %s from %s at %s with course %f and speed %f created%n", id.getID(),fromId.getID(),coordinates.toString(),course.getValue_(),speed.getValue_());
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
					id = new AgentID(argumentList.get(3));
					cmd.schedule(new CommandMunitionDefineShell(cmd,originalCommandText,id));
					System.out.printf(subType+" %s "+"created%n",id.getID());
					break;
				case "depth_charge":
					id = new AgentID(argumentList.get(3));
					fuzeId = new AgentID(argumentList.get(6));
					cmd.schedule(new CommandMunitionDefineDepthCharge(cmd,originalCommandText,id,fuzeId));
					System.out.printf("Depth charge %s created with fuze %s%n",id.getID(),fuzeId.getID());
					break;
				case "missile":
					id = new AgentID(argumentList.get(3));
					sensorId = new AgentID(argumentList.get(6));
					fuzeId = new AgentID (argumentList.get(8));
					DistanceNauticalMiles distance = new DistanceNauticalMiles(Double.parseDouble(argumentList.get(11)));
					cmd.schedule(new CommandMunitionDefineMissile(cmd,originalCommandText,id,sensorId,fuzeId,distance));
					System.out.printf("Created missile %s with sensor %s and fuze %s and arming distance %f%n", id.getID(),sensorId.getID(),fuzeId.getID(),distance.getValue_());
					break;
				case "torpedo":
					id = new AgentID(argumentList.get(3));
					sensorId = new AgentID(argumentList.get(6));
					fuzeId = new AgentID (argumentList.get(8));
					Time time = setTime(argumentList.get(11));
					cmd.schedule(new CommandMunitionDefineTorpedo(cmd,originalCommandText,id,sensorId,fuzeId,time));
					System.out.printf("Created torpedo %s with sensor %s and fuze %s and arming time %f seconds%n", id.getID(),sensorId.getID(),fuzeId.getID(),time.getValue_());
					break;
				}
	//-------Sensor/Fuze Commands-----------\\
			case "sensor":
				subType = argumentList.get(2); //Types of sensors i.e. radar, thermal, acoustic
				switch(subType) {
				case "radar":
					sensorId = new AgentID(argumentList.get(3));

					//with = 4, field = 5, of = 6, view = 7
					fov = this.setFieldOfView(argumentList.get(8));
					//power = 9
					power = this.setPower(argumentList.get(10));
					//sensitivity = 11
					sensitivity = this.setSensitivity(argumentList.get(12));
					cmd.schedule(new CommandSensorDefineRadar(cmd,originalCommandText,sensorId,fov,power,sensitivity));
					System.out.printf("Radar Sensor %s has been made.%n",sensorId.getID());
					break;
				case"thermal":
					sensorId = new AgentID(argumentList.get(3));
					//with = 4, field = 5, of = 6, view = 7
					fov = this.setFieldOfView(argumentList.get(8));
					//sensitivity = 9
					sensitivity = this.setSensitivity(argumentList.get(10));
					cmd.schedule(new CommandSensorDefineThermal(cmd,originalCommandText,sensorId,fov,sensitivity));
					System.out.printf("Thermal Sensor %s has been made with fov: %f and sensitivity: %f.%n", sensorId.getID(),fov.getLimit().getValue_(),sensitivity.getSensitivity());
					break;
				case"acoustic":
					sensorId = new AgentID(argumentList.get(3));
					//with = 4, sensitivity = 5
					sensitivity = this.setSensitivity(argumentList.get(6));
					cmd.schedule(new CommandSensorDefineAcoustic(cmd,originalCommandText,sensorId,sensitivity));
					System.out.printf("Acoustic Sensor %s has been made with sensitivity: %f.%n", sensorId.getID(),sensitivity.getSensitivity());
					break;
				case "sonar": //Can have either active or passive.
					//TODO
					break;
				case "depth":
					fuzeId = new AgentID(argumentList.get(3));
					Altitude alt = new Altitude(Integer.parseInt(argumentList.get(7)));
					cmd.schedule(new CommandSensorDefineDepth(cmd,originalCommandText,fuzeId,alt));
					System.out.printf("Depth sensor %s created with trigger depth %f%n", fuzeId.getID(),alt.getValue_());
					
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
			id = new AgentID(argumentList.get(2));
			cmd.schedule(new CommandViewDeleteWindow(cmd,originalCommandText,id));
			System.out.printf("deleted window %s%n", id.getID());
			break;

		case "set":
			switch(argumentList.get(2)) {
				case "load":
					id = new AgentID(argumentList.get(1));
					munitionId = new AgentID(argumentList.get(4));
					cmd.schedule(new CommandActorLoadMunition(cmd,originalCommandText,id,munitionId));
					System.out.printf("Loaded new munition for actor %s from %s%n",id.getID(),munitionId.getID());
				break;
          
				case "deploy":
					if(!argumentList.contains("at")) {
						id = new AgentID(argumentList.get(1));
						munitionId = new AgentID(argumentList.get(4));
						cmd.schedule(new CommandActorDeployMunition(cmd,originalCommandText,id,munitionId));
						System.out.printf("Muniton %s deployed from %s%n", munitionId.getID(),id.getID());
					}
					else {
						id = new AgentID(argumentList.get(1));
						munitionId = new AgentID(argumentList.get(4));
						AttitudeYaw azimuth = new AttitudeYaw(Integer.parseInt(argumentList.get(7)));
						AttitudePitch elevation = new AttitudePitch(Integer.parseInt(argumentList.get(9)));
						cmd.schedule(new CommandActorDeployMunitionShell(cmd, originalCommandText, id, munitionId, azimuth, elevation));
						System.out.printf("Muniton %s deployed from %s at azimuth %f and elevation %f%n", munitionId.getID(),id.getID(),azimuth.getValue_(),elevation.getValue_());
					}
          break;
          
					case "speed":
          id = new AgentID(argumentList.get(1));
					Groundspeed newSpeed = new Groundspeed(Double.parseDouble(argumentList.get(3)));
					
					cmd.schedule(new CommandActorSetSpeed(cmd, 
														  originalCommandText,
														  actorID,
														  newSpeed
														  ));
				break;
				
				case "course":
           id = new AgentID(argumentList.get(1));
					Course newCourse = new Course(Double.parseDouble(argumentList.get(3)));
					
					cmd.schedule(new CommandActorSetCourse(cmd,
														   originalCommandText,
														   actorID,
														   newCourse));
				break;
				
				// depth and altitude are nearly identical cases,
				// both are measures of distance from the surface
				// neither should be less than zero.
				// internally, however, we treat depth as negative 
				// altitude, so we need to cover that case in the code
				case "depth":
				case "altitude":
           id = new AgentID(argumentList.get(1));
					double altitudeValue = Double.parseDouble(argumentList.get(3));
				
					// in practice, this means just flip the sign if the user specified depth
					if(argumentList.get(2).equals("depth"))
						altitudeValue *= -1.0;
				
					Altitude newAltitude = new Altitude(altitudeValue);
					
					cmd.schedule(new CommandActorSetAltitudeDepth(cmd,
																  originalCommandText,
																  id,
																  newAltitude));					
				break;
			}

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
			cmd.schedule(new CommandMiscExit(cmd, originalCommandText));
			break;
		}//End of switch(commandType)
	}//End of evaluate()
}
	
