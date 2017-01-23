
package org.usfirst.frc.team4085.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;

import org.usfirst.frc.team4085.robot.commands.ExampleCommand;
import org.usfirst.frc.team4085.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Encoder;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	CANTalon dmotor1 = new CANTalon(3);//Front Left Drive Motor
    CANTalon dmotor2 = new CANTalon(5);//Front Right Drive Motor
    CANTalon dmotor3 = new CANTalon(6);//Back Left Drive Motor
    CANTalon dmotor4 = new CANTalon(2);//Back Right Drive Motor
    RobotDrive rdrive = new RobotDrive(dmotor1,dmotor3,dmotor2,dmotor4);//Defines The Location of The Drive Motors
    Joystick Xbox1 = new Joystick(0);// Driver Controls
	
    CANTalon enc = new CANTalon(0);
    
    
    AnalogInput sonar = new AnalogInput(0);
    
    I2C wire = new I2C(I2C.Port.kOnboard, 4);
    

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	
	ADXRS450_Gyro gyro =  new ADXRS450_Gyro();

    Command autonomousCommand;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	

         //Inverts Right Drive Motors, Needed For Correct Mecanum Code
         rdrive.setInvertedMotor(MotorType.kFrontRight, true);
         rdrive.setInvertedMotor(MotorType.kRearRight, true);
         rdrive.setInvertedMotor(MotorType.kFrontLeft, false);
         rdrive.setInvertedMotor(MotorType.kRearLeft, false);
       
		oi = new OI();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        
        
        
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        
        
        gyro.reset();
     
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
        byte[] buffer = new byte[3];

        wire.transaction(null, 0, buffer, buffer.length);
        
        byte y = 0;
        byte[] x = new byte[2];
        
        for(int i = 0; i < buffer.length; i++) {
        	
        	if(i == 0) {
        		y = buffer[i];
        	}
        	else {
        		x[i - 1] = buffer[i];
        	}
	
        }


       int yCoord = (int) y & 0xFF;
        
		int xCoord1 = (int) (x[0] & 0xFF);
		int xCoord2 = (int) ((x[1] & 0xFF) * 256);
	//	System.out.println();
		int xCoord = xCoord1 + xCoord2;
    
		System.out.println("X: " + xCoord + " Y: " + yCoord);
		
		/*if(xCoord < 140) {
			moveLeft(0.2f);
			System.out.println("Left");
		}
		else if(xCoord > 180) {
			moveRight(0.2f);
			System.out.println("Right");
		}*/
    	
		if(xCoord < 140) {
			rdrive.mecanumDrive_Cartesian(0, 0, -0.15f, 0);
			System.out.println("Left");
		}
		else if(xCoord > 180) {
			rdrive.mecanumDrive_Cartesian(0, 0, 0.15f, 0);
			System.out.println("Right");
		}
    	
    	
    }

    public double calibrateAngle(double angle) {
    	
    	if(angle >= 0) 
    		return angle  % 360;
    	else
    		return (angle % 360) + 360;
    	
    }
    
    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();  
        
        gyro.reset();
        dmotor1.setEncPosition(0);
        encoderPrint(400);
        //comp.setClosedLoopControl(true);
        
        
        
    }
public void encoderPrint(int position) {
	double EncoderPos = dmotor1.getEncPosition();
	System.out.println("Encoder Position: " + EncoderPos);
	if (EncoderPos < position) {
		System.out.println((position-EncoderPos) + "encoder units left to go!");
	}
}
Solenoid piston = new Solenoid(7);
//Compressor comp = new Compressor(6);
public void piston() {
	if (Xbox1.getRawButton(1)){
		piston.set(true);
	}
	else {
		piston.set(false);
	}
}
public void moveForward(double speed) {
	rdrive.mecanumDrive_Cartesian(0, -speed, 0, 0);
}
public void moveBackward(double speed) {
	rdrive.mecanumDrive_Cartesian(0, speed, 0, 0);
}
public void moveLeft(double speed) {
	rdrive.mecanumDrive_Cartesian(-(speed), 0, 0, 0);
}
public void moveRight(double speed) {
	rdrive.mecanumDrive_Cartesian(speed, 0, 0, 0);
}
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    	double a = 0.0248046875f; //5f/512f;
    	double b = sonar.getVoltage();
    	double c = b/a;

    //.out.println(c);
        
        //Drive Control 
        double X,Y,Z;
        
        X = Xbox1.getRawAxis(0);
        Y = Xbox1.getRawAxis(1);
        Z = Xbox1.getRawAxis(4);
        
        //encoder
 /*(400);
        
        piston();*/
        
       /* int[] coords = getCoords();

        		int xCoord = coords[0];
        		int yCoord = coords[1];
        		System.out.println("X: " + xCoord + " Y: " + yCoord); */
        
        byte[] buffer = new byte[3];

        wire.transaction(null, 0, buffer, buffer.length);
        
        byte y = 0;
        byte[] x = new byte[2];
        
        for(int i = 0; i < buffer.length; i++) {
        	
        	if(i == 0) {
        		y = buffer[i];
        	}
        	else {
        		x[i - 1] = buffer[i];
        	}
	
        }


       int yCoord = (int) y & 0xFF;
        
		int xCoord1 = (int) (x[0] & 0xFF);
		int xCoord2 = (int) ((x[1] & 0xFF) * 256);
		System.out.println();
		int xCoord = xCoord1 + xCoord2;
		//System.out.println(buffer[2]);
		//System.out.println(xCoord2);
		
		System.out.println("X: " + xCoord + " Y: " + yCoord);
		

        
        //Mecanum Drive Statement
        rdrive.mecanumDrive_Cartesian(Db(X), Db(Y),Db(Z), 0);
        
        if(Xbox1.getRawButton(1))
        	gyro.reset();
        
      //  System.out.println("Gyro: " + calibrateAngle(gyro.getAngle()));
        
      //  System.out.println("z: "+Z);
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    double Db(double axisVal) {
    	
    	if(axisVal < -0.30)
    			return axisVal;
    	if(axisVal > + 0.30)
    		return axisVal;
    	return 0;
    	
    }
    
    public void testPeriodic() {
    	
        LiveWindow.run();
       
        
        

    }
    
    
    
}
