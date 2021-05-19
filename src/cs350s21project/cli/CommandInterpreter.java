package cs350s21project.cli;
import java.util.ArrayList;
import cs350s21project.controller.*;
import cs350s21project.controller.command.munition.CommandMunitionDefineBomb;
import cs350s21project.controller.command.munition.CommandMunitionDefineShell;
import cs350s21project.controller.command.view.*;
import cs350s21project.datatype.*;
public class CommandInterpreter {

	private AgentID id;
	private int size;
	private String objectType;
	private String subType;
	
	private Latitude setLatitude(String str) {
		String [] latData = str.split("[*#']");
		return new Latitude(Integer.parseInt(latData[0]), Integer.parseInt(latData[1]),Double.parseDouble(latData[2]));
	}
	
	private Longitude setLongitude(String str) {
		String [] lonData = str.split("[*#']");
		return new Longitude(Integer.parseInt(lonData[0]), Integer.parseInt(lonData[1]),Double.parseDouble(lonData[2]));
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
				//TODO
				break;
			}//End of create objectType switch
			break;
		case "define":
			objectType = argumentList.get(1);
			switch(objectType) {
			case "ship":
				//TODO
				break;
	//--------Munition Commands------------\\
			case "munition":
				subType = argumentList.get(2); //Types of munition, i.e. bomb,shell,charge
				switch(subType) {
				case "bomb":
					id = new AgentID(argumentList.get(3));
					cmd.schedule(new CommandMunitionDefineBomb(cmd,originalCommandText,id));
					System.out.println(subType+" "+'"'+id.getID()+'"'+" created");
					break;
				case "shell":
					id = new AgentID(argumentList.get(3));
					cmd.schedule(new CommandMunitionDefineShell(cmd,originalCommandText,id));
					System.out.println(subType+" "+'"'+id.getID()+'"'+" created");
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
					//TODO
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
		}//End of switch(commandType)
	}//End of evaluate()
}
		
	

