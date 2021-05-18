package cs350s21project.cli;
import java.util.ArrayList;
import cs350s21project.controller.CommandManagers;
import cs350s21project.controller.command.view.CommandViewCreateWindowTop;
import cs350s21project.datatype.AgentID;
import cs350s21project.datatype.Latitude;
import cs350s21project.datatype.Longitude;
public class CommandInterpreter {

	
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
		while(!commandText.isEmpty()) {//Splits the string based on whitespaces.
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
			String objectType = argumentList.get(1);
			switch(objectType) {
			case "window":
				AgentID id = new AgentID(argumentList.get(2));
				String viewType = argumentList.get(3);
				if(viewType.equals("top")) {
					//index 4 and 5 are skipped since they hold arbitrary values ('with','view')
					int size = Integer.parseInt(argumentList.get(6));
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
				
			}//End of switch(objectType)	
		}//End of switch(commandType)
	}//End of evaluate()
}
		
	

