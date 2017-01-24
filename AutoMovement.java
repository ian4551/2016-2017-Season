package org.usfirst.frc.team4085.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoMovement {

	ADXRS450_Gyro gyro;
	RobotDrive drive;
	CANTalon FR;
	CANTalon FL;
	CANTalon BL;
	CANTalon BR;
	
	public AutoMovement(CANTalon FR, CANTalon FL, CANTalon BL, CANTalon BR, RobotDrive drive, ADXRS450_Gyro gyro) {
		
		this.gyro = gyro;
		this.drive = drive;
		this.FR = FR;
		this.FL = FL;
		this.BL = BL;
		this.BR = BR;
		
	}
	
	public void moveRight(double speed) {
		drive.mecanumDrive_Cartesian(speed, 0, 0, 0);
	}
	
	public void moveLeft(double speed) {
		drive.mecanumDrive_Cartesian(-speed, 0, 0, 0);
	}
	
	public void moveBackward(double speed) {
		drive.mecanumDrive_Cartesian(0, speed, 0, 0);
	}
	
	public void moveForward(double speed) {
		drive.mecanumDrive_Cartesian(0, -speed, 0, 0);
	}
	
	public void rotateLeft(double speed) {
		drive.mecanumDrive_Cartesian(0, 0, -speed, 0);
	}
	
	public void rotateRight(double speed) {
		drive.mecanumDrive_Cartesian(0, 0, speed, 0);
	}
	
	public int distanceLeft(int position) {
		
		return BL.getEncPosition() - position;
		
	}
	
	public int encPosition() {
		return BL.getEncPosition();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public void turnTo(double angle) {

			
			boolean turnRight = angle > getAngle(); //Is the number to the right or left. This does not say it is the fastest
			
			double distanceBetween = Math.abs(angle - getAngle()); //used to decide if the turnRight is the fastest
			
			if(distanceBetween > 180) //if distance is > 180, then turnRight needs to be switched to be the fastest
				turnRight = !turnRight;
			if(getAngle() < angle - 1 || getAngle() > angle + 1) { //Starts the actual code to get to the correct point
				
				if(turnRight) 
					rotateRight(0.5f);
				else
					rotateLeft(0.5f);
				
			}

			
		
	}
	
	public void turnXDegrees(double angle) {
		
		double currentAngle = gyro.getAngle();
		double newAngle = gyro.getAngle() + angle;
		
		if(newAngle > currentAngle) {
			while(gyro.getAngle() < newAngle - 0.5f) {
				rotateRight(0.5f);
			}
		}
		else {
			while(gyro.getAngle() > newAngle + 0.5f) {
				rotateLeft(0.5f);
			}
		}
		
	}
	
	public double getAngle() {
		if(gyro.getAngle() < 0)
			return (gyro.getAngle() % 360) + 360;
		else
			return gyro.getAngle() % 360;
	}
	
}
