package cs350s21project.cli;
import java.util.ArrayList;
import cs350s21project.controller.CommandManagers;
import cs350s21project.controller.command.view.CommandViewCreateWindowTop;
import cs350s21project.datatype.AgentID;
import cs350s21project.datatype.Latitude;
import cs350s21project.datatype.Longitude;
public class CommandInterpreter {

	
	private Latitude setLatitude(String [] latData) {
		return new Latitude(Integer.parseInt(latData[0]), Integer.parseInt(latData[1]),Double.parseDouble(latData[2]));
	}
	
	private Longitude setLongitude(String [] lonData) {
		return new Longitude(Integer.parseInt(lonData[0]), Integer.parseInt(lonData[1]),Double.parseDouble(lonData[2]));
	}
	
	public void evaluate(String commandText) {
		commandText=commandText.toLowerCase(); // lower case to allow any input in weird casing
		commandText.trim(); //trim all whitespace
		ArrayList <String> argumentList = new ArrayList<>();
		while(!commandText.isEmpty()) {//Split the commandText based on whitespaces inside the string to allow for easier access to different kinds of 
			String temp =(commandText.contains(" ")) ? commandText.substring(0,commandText.indexOf(" ")+1).trim():commandText;
			temp = temp.replaceAll("[()]", ""); //Remove excess parameters
			argumentList.add(temp);
			commandText = (commandText.contains(" ")) ? commandText.substring(commandText.indexOf(" ")+1).trim() : "";
		}
		argumentList.removeIf(n -> (n.isEmpty())); //Removes any blank spaces that might of been made during the while loop.
		argumentList.trimToSize();
		String commandType = argumentList.get(0);
		CommandManagers cmd = CommandManagers.getInstance();
		switch(commandType) {	
		case "create":
			String objectType = argumentList.get(1);
			AgentID id = new AgentID(argumentList.get(2));
			String viewType = argumentList.get(3);
			switch(objectType) {
			case "window":
				if(viewType.equals("top")) {
					//index 4 and 5 are skipped since they hold arbitrary values ('with','view')
					int size = Integer.parseInt(argumentList.get(6));
					Latitude latOrigin = setLatitude(argumentList.get(7).split("[*#']"));
					Latitude latExtent = setLatitude(argumentList.get(8).split("[*#']"));
					Latitude latInterval = setLatitude(argumentList.get(9).split("[*#']"));
					Longitude longOrigin = setLongitude(argumentList.get(10).split("[*#']"));
					Longitude longExtent = setLongitude(argumentList.get(11).split("[*#']"));
					Longitude longInterval = setLongitude(argumentList.get(12).split("[*#']"));
					cmd.schedule(new CommandViewCreateWindowTop(cmd,"Insert Message Here",id,size,latOrigin,latExtent,latInterval,longOrigin,longExtent,longInterval));
				}else if(viewType.equals("front")) {
					System.out.println("Front view accessed, but not yet ready.");
				}else if(viewType.equals("side")) {
					System.out.println("Side view accessed, but not yet ready.");
				
				}else 
					throw new RuntimeException("Invalid view type for create window.");
			break;
				
			}
			
			
		}
			
		//case "command":
				//code;
				//break;
			//case "different command":
				//code
				//break;
			
	}
}
		
	

