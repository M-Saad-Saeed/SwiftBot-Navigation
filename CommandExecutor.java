import swiftbot.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandExecutor extends SwiftBotNavigation {

    public void executeCommands(String decodemessage, Scanner scanner) {
       
        value = decodemessage.split(";");
        
        try {
            for (String cmd : value) {
                cmd = cmd.trim();
                if (cmd.isEmpty()) continue;
                
                // Record the command in history.
                CommandHistory.add(cmd + ";");
                String[] parts = cmd.split(",");
                String type = parts[0].trim().toUpperCase();
                // execute the command based on its type using a switch-case.
                switch (type) {
                    case "F":
                    case "B":
                    case "R":
                    case "L":
                        executeMovement(type, parts);
                        askContinue(scanner);
                        break;
                        
                    case "T":
                        // Updated T command handling to match test file.
                        int count = Integer.parseInt(parts[1].trim());
                        System.out.println("Executing " + type + " for " + count + " commands.");
                        int startIndex = Trace.size() - count;
                        if (startIndex < 0) {
                            System.out.println("Trace does not have that many values. Please try again with new command");
                            System.exit(5);
                        }
                        // Iterate through the Trace list in reverse order.
                        for (int i = Trace.size() - 1; i >= startIndex; i--) {
                            String tracedCommand = Trace.get(i).trim();
                            // Remove trailing semicolon if present.
                            if (tracedCommand.endsWith(";")) {
                                tracedCommand = tracedCommand.substring(0, tracedCommand.length() - 1);
                            }
                            if (tracedCommand.isEmpty()) continue;
                            
                            String[] cmdParts = tracedCommand.split(",");
                            String cmdType = cmdParts[0].trim().toUpperCase();
                            System.out.println("Tracing command: " + tracedCommand);
                            if (cmdType.matches("F|B|R|L")) {
                                executeMovement(cmdType, cmdParts);
                            }
                        }
                        askContinue(scanner);
                        break;
                        
                    case "W":
                        System.out.println("Writing log file...");
                        writeLog();
                        System.out.println("Reading and displaying log file contents:");
                        readLog();
                        askContinue(scanner);
                        break;
                        
                    case "X":
                        // Execute the termination command.
                        executeX();
                        break;
                        
                    default:
                        // This default case should not occur if checking was complete.
                        System.out.println("Unrecognized command type: " + type);
                        System.exit(5);
                }
            }
            
            // If the command limit is reached, execute the exit command.
            if (CommandHistory.size() == 10 || CommandHistory.size() == value.length) {
                System.out.println("Command limit reached. Executing X...");
                executeX();
            }
        } catch (Exception e) {
            System.out.println("Error in execution method: " + e.getMessage());
            e.printStackTrace();
            System.exit(5);
        }
    }
    
    public static void executeMovement(String type, String[] parts) {
        int speed = Integer.parseInt(parts[1]);
        int duration = Integer.parseInt(parts[2]) * 100;
        double seconds = duration / 100.0;
        System.out.println("Executing " + type + " at speed " + speed + " for " + seconds + " seconds.");

        switch (type) {
            case "F":
                try {
                    int[] blue = {0, 0, 255};  // blue light for forward movement.
                    swiftBot.fillUnderlights(blue);
                    swiftBot.move(speed, speed, duration);
                    Thread.sleep(duration);    
                } catch (InterruptedException e) {
                    System.out.println("Failed to set light");
                    e.printStackTrace();
                }        
                break;
            case "B":
                int[] green = {0, 255, 0};  // Green light for backward movement.
                try {
                    swiftBot.fillUnderlights(green);
                    swiftBot.move(-speed, -speed, duration);
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    System.out.println("Failed to set light");
                    e.printStackTrace();
                }      
                break;
            case "R":
                int[] red = {255, 0, 0};  //  red light for right movement.
                try {
                    swiftBot.fillUnderlights(red);
                    swiftBot.move(speed, -speed, duration);
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    System.out.println("Failed to set light");
                    e.printStackTrace();
                }  
                break;
            case "L":
                int[] yellow = {255, 255, 0};  // Yellow light for left movement.
                try {
                    swiftBot.fillUnderlights(yellow);
                    swiftBot.move(-speed, speed, duration);
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    System.out.println("Failed to set light");
                    e.printStackTrace();
                }  
                break;
        }
    }
    
    public static void executeX() {
        System.out.println("Executing X command: Code executed successfully");
        try {
            Integer[][] colorObjects = {
                {255, 0, 0},
                {0, 0, 255},
                {0, 255, 0},
                {255, 255, 255}
            };
            // Convert the array to a list for shuffling.
            List<Integer[]> colorList = Arrays.asList(colorObjects);
            Collections.shuffle(colorList);
            
            for (Integer[] colorObj : colorList) {
                // Convert Integer[] to int[]
                int[] color = new int[colorObj.length];
                for (int i = 0; i < colorObj.length; i++) {
                    color[i] = colorObj[i];
                }
                swiftBot.fillUnderlights(color);
                Thread.sleep(1000);
            }
            swiftBot.disableUnderlights();
            System.out.println("Program terminated successfully");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to set all under lights");
            System.exit(5);
        }
    }

    public static void askContinue(Scanner scanner) {
        // Register the physical button X so that if pressed, it immediately triggers executeX().
        CommandExecutor.swiftBot.disableAllButtons();
        swiftBot.enableButton(Button.X, () -> {
            System.out.println("Physical button X pressed. Executing termination command.");
            executeX();
        });
        
        System.out.println("Do you want to continue? (Press X on keyboard or on swift bot to execute the X command, any other key on keyboard to continue)");
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.equals("X")) {
            scanner.close();
            executeX();
        }
    }

    public static void writeLog() {
        try {
            long endTime = System.currentTimeMillis(); // records current time
            long totalExecutionTime = (endTime - startTime) / 1000; // records total execution time
            LocalTime currentTime = LocalTime.now();
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); // time formatter

            String logFileName = "SwiftBotLog.txt";
            FileWriter logFile = new FileWriter(logFileName);

            logFile.write("SwiftBot Command File\n");
            logFile.write("---------------------\n");
            logFile.write("Commands Executed:\n");
            for (String cmd : CommandHistory) {
                logFile.write(cmd + "\n");
            }
            logFile.write("\nTotal Execution Time: " + totalExecutionTime + " seconds\n");
            logFile.write("Log Written At: " + formattedTime + "\n");

            logFile.close();

            File logFileObj = new File(logFileName);
            System.out.println("Log file created successfully: " + logFileObj.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing log file: " + e.getMessage());
        }
    }

    public static void readLog() {
        try {
            String logFileName = "SwiftBotLog.txt";
            BufferedReader logReader = new BufferedReader(new FileReader(logFileName));
            String line;
            while ((line = logReader.readLine()) != null) {
                System.out.println(line);
            }
            logReader.close();
        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
        }
    }
}
