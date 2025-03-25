
import swiftbot.*;
import java.util.ArrayList;
import java.util.Scanner;
import swiftbot.Button;



public class SwiftBotNavigation {
    static SwiftBotAPI swiftBot;
    static ArrayList<String> CommandHistory = new ArrayList<>(); // used in commands
    static String[] value; 
    static long startTime = System.currentTimeMillis(); // used to record current time
    static String decodedMessage;
    static boolean Answer;
    static ArrayList<String> Trace = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        try {
            swiftBot = new SwiftBotAPI(); // API is configured
        } catch (Exception e) {
            System.out.println("\nI2C disabled!");
            System.out.println("Run the following command:");
            System.out.println("sudo raspi-config nonint do_i2c 0\n");
            System.exit(5);
        }

        System.out.println("\n***************************************");
        System.out.println("*     SWIFTBOT NAVIGATION PROGRAM     *");
        System.out.println("***************************************\n");
        startprogram();
    }
    
    // This method resets static/global variables so is there is a problem in in checking it start over from scratch
    public static void resetGlobals() {
        decodedMessage = null;
        Answer = false;
        Trace.clear();
        CommandHistory.clear();
        startTime = System.currentTimeMillis();
    }
    
    public static void startprogram() { // this function is responsible for calling all of the classes 
       
        Scanner scanner = new Scanner(System.in); // this is the main scanner
        CommandExecutor executor = new CommandExecutor(); // main executor 

     
        QRCodeDetection qrDetector = new QRCodeDetection();//detects QR CODE
        qrDetector.detectQRCode();
        
        
        
        CommandChecker checker = new CommandChecker();// checks all of the commands before going forward
        swiftBot.disableAllButtons();
        checker.checkCommands(decodedMessage, scanner);

        if (Answer) {
            System.out.println("Commands are checked. Do you wish to continue? Press X to exit and any other key on the keyboard to continue");
            swiftBot.disableAllButtons();
            swiftBot.enableButton(Button.X, () -> { // X button exits program
                System.out.println("Physical button X pressed. Exiting");
                CommandExecutor.executeX();
            });
            String userInput = scanner.nextLine().trim().toUpperCase();
            if (userInput.equals("X")) { // X button exits program
                CommandExecutor.executeX();  
            } else {
                swiftBot.disableAllButtons();
                executor.executeCommands(decodedMessage, scanner);
            }
        } else {
            System.out.println("There is a problem in checking. Try again with different commands");
            System.exit(5);
        }
        
    }
}
