
import java.util.Scanner;

import swiftbot.*;

public class CommandChecker extends SwiftBotNavigation {

 public void checkCommands(String decodemessage,Scanner scanner) {
     if (decodemessage == null) {
         System.out.println("Decoded message is null.");
         StartOver(5,scanner);
     }
     
     // Remove quotes and ensure the message ends with a semicolon.
     decodemessage = decodemessage.replace("\"", "");
     if (!decodemessage.endsWith(";")) {
         decodemessage += ";";
     }
     
     value = decodemessage.split(";"); // Separates commands by spliting message by ";"
     
     if (value.length > 10) { //checks & ensure that if commands length are below 10
         System.out.println("Commands are greater than 10. Try again with fewer values.");
         StartOver(5,scanner);
     }
     
     try {
         for (String cmd : value) { 
             cmd = cmd.trim();
             if (cmd.isEmpty()) continue;
             
             String[] parts = cmd.split(",");
             String type = parts[0].trim().toUpperCase();
             
             // checks the command based on its type using a switch-case.
             switch (type) {
                 case "F":
                 case "B":
                 case "R":
                 case "L":
                     if (parts.length != 3) {
                         System.out.println("Illegal command: " + cmd + ". Please try again with a new command.");
                         StartOver(5,scanner);
                     }
                     int speed = Integer.parseInt(parts[1].trim());
                     int duration = Integer.parseInt(parts[2].trim()) * 100;
                     if (speed <= 0 || speed > 100 || duration <= 0 || duration > 600) {
                         System.out.println("Error: Invalid speed or duration for " + type + ". Please try again with a new command.");
                         StartOver(5,scanner);
                     }
                     Answer = true;
                     Trace.add(cmd + ";");
                     break;
                     
                 case "T":
                     if (parts.length != 2) {
                         System.out.println("Illegal command: " + cmd + ". Please try again with a new command.");
                         StartOver(5,scanner);
                     }
                     int count = Integer.parseInt(parts[1].trim());
                     if (count > Trace.size()) {
                         System.out.println("Trace does not have that many values. Please try again with a new command.");
                         StartOver(5,scanner);
                     }
                     Answer = true;
                     break;
                     
                 case "W":
                 case "X":
                     if (parts.length != 1) {
                         System.out.println("Illegal command: " + cmd + ". Please try again with a new command.");
                         StartOver(5,scanner);
                     }
                     Answer = true;
                     break;
                     
                 default:
                     System.out.println("Command not found in acceptable list: " + cmd + ". Please try again with a new command.");
                     StartOver(5,scanner);
             }
         }
     } catch (Exception e) {
         System.out.println("Error in CommandChecker: " + e.getMessage());
         e.printStackTrace();
         StartOver(5,scanner);
     }
 }
 public static void StartOver(int num, Scanner scanner){ // start over if problem in checking
	    System.out.println("Do you wanna start over by pressing any button on the keyboard to scan a new QR code? or press X to Exit");
	    
	    // Disable buttons and enable the physical X button for exit.
	    CommandChecker.swiftBot.disableAllButtons();
	    swiftBot.enableButton(Button.X, () -> {
	        System.out.println("Physical button X pressed. Exiting");
	        System.exit(num);
	    });
	    
	    String input = scanner.nextLine().trim().toUpperCase();
	    if (input.equals("X")) {
	        System.out.println("Keyboard button X pressed. Exiting");
	       
	        System.exit(num);
	    } else {
	        
	        SwiftBotNavigation.resetGlobals();
	        
	        SwiftBotNavigation.startprogram();
	    }
	}	 
	 
}

