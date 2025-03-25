This code is to be used with swiftbot api and swiftbot robot to run the program.
This code used basic oop (inheritance) to call all classes in main Navigation class
This code fullfills all of the following requirements: 
                       Functional Requirements
1)	Program should start with the scanning the QR Code and decoding commands.
2)	Command should follow a format:
-	F, <speed>, <Duration>:
-	 B, <speed>, <Duration>: 
-	R, <speed>, <Duration>: 
-	L, <speed>, <Duration>:   
-	T, <count>: 
-	W: 
3)	Commands should be in valid input range which is.
-	<speed> can not be exceeding 100 
-	<duration> can not be more than 6
-	<count> can not exceed the number of executed commands
4)	If the command is F, <speed>, <Duration>: then Swiftbot move forward at this <speed>,  for this much time <duration>
5)	If the command is B, <speed>, <Duration>: then Swiftbot move Backward at this <speed>,  for this much time <duration>
6)	If the command is R, <speed>, <Duration>: then Swiftbot turn Right at this <speed>,  for this much time <duration>
7)	If the command is L, <speed>, <Duration>: then Swiftbot turn Left at this <speed>,  for this much time <duration>
8)	If the command is T, <count>: then Swiftbot Retrace the command <count> many times and do not include the previous T
9)	If the command is W: then Swiftbot  should  create  a  log  file.  The  log  file  should  include  all  the commands (including the ‘T’ commands) that the Swiftbot has received, the total time of execution of the Program and the time (i.e. current time) the log was written. The current time should be written in HH:MM:SS format and should clearly indicate that it is the time the log was written to the file. The program should then inform the user where the log file has been saved by displaying the complete file 

10)	  For a single command, the additional inputs should be separated with commas (“ ,”)
11)	 Program should be able to accept multiple commands separated by (“;”) and execute them sequentially up to 10 commands
12)	 Before moving Swiftbot starts moving it should check that all commands are correct
13)	 Before Swiftbot start moving it should display the commands received from user
14)	 Program should be terminated when X button is used.

Non Functional Requirements
1)	It is reliable and handle errors correctly 
2)	It is swift and execute functions in minimum time
3)	It does provide clear feedback via command-line interface
4)	It does not take at most  15 seconds to scan all of  QR code process


Additional Functionalities
1)	When command is finished it display random light in four colours
2)	If QR code is empty then it gives user three tries to scan the QR
3)	When Executing Forward command Blue Light is displayed
4)	When Executing Backward command Green Light is displayed
5)	When Executing Left command Yellow Light is displayed
6)	When Executing Right command Red Light is displayed
7)	When Executing W Command, it also read the file
8)	When X is pressed it display random light in four colours
9)	After Each Command Execution  User is asked to press enter to move to next command
10)	 When there is an error during checking method, it ask user to startover or  to terminate
