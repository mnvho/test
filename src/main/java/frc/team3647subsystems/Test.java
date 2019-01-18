package frc.team3647subsystems;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;
import frc.team3647inputs.Joysticks;
import frc.team3647inputs.Limelight;
import frc.team3647inputs.NavX;
import frc.team3647subsystems.Drivetrain;
import frc.robot.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Test 
{
	// For the motors
	public static WPI_TalonSRX leftSRX = new WPI_TalonSRX(Constants.leftMaster);
	public static WPI_TalonSRX rightSRX = new WPI_TalonSRX(Constants.rightMaster);
	public static VictorSPX leftSPX1 = new VictorSPX(Constants.leftSlave1);
	public static VictorSPX rightSPX1 = new VictorSPX(Constants.rightSlave1);
	public static VictorSPX leftSPX2 = new VictorSPX(Constants.leftSlave2);
	public static VictorSPX rightSPX2 = new VictorSPX(Constants.rightSlave2);

	// For the intake motors
	public static VictorSPX rightIntakeMotor = new VictorSPX(Constants.rightIntakePin);
	public static VictorSPX leftIntakeMotor = new VictorSPX(Constants.leftIntakePin);
	
	// For the timer
	public static Timer timer = new Timer();

	// For other variables used in the function
	public static int step = 0;
	public static double time = 1;
	public static double lSpeed, rSpeed;

	// An auto checking all the motors within the robot
	// Follows the order of:
	// 1. Right wheels forward
	// 2. Right wheels back
	// 3. Left wheels forward
	// 4. Left wheels back
	// 5. Intake pick up
	// 6. Intake shoot
	public static void move()
	{
		switch(step)
		{
			case 0:
				timer.start();
				step = 1;
			break;
			// 1. Right wheels forward
			case 1:
				if(timer.get() < time)
				{
					setSpeed(0, .5);
				}
				else
				{
					timer.reset();
					step = 2;
				}
			break;
			case 2:
				timer.start();
				step = 3;
			break;
			// 2. Right wheels back
			case 3:
				if(timer.get() < time)
				{
					setSpeed(0, -.5);
				}
				else
				{
					timer.reset();
					step = 4;
				}
			break;
			case 4:
				timer.start();
				step = 5;
			break;
			// 3. Left wheels forward
			case 5:
				if(timer.get() < time)
				{
					setSpeed(.5, 0);
				}
				else
				{
					timer.reset();
					step = 6;
				}
			break;
			case 6:
				timer.start();
				step = 7;
			break;
			// 4. Left wheels back
			case 7:
				if(timer.get() < time)
				{
					setSpeed(-.5, 0);
				}
				else
				{
					timer.reset();
					step = 8;
				}
			break;
			case 8:
				timer.start();
				step = 9;
			break;
			// 5. Intake pick up
			case 9:
				if(timer.get() < time)
				{
					setSpeed(0, 0);
					shoot(.5);
				}
				else
				{
					timer.reset();
					step = 10;
				}
			break;
			case 10:
				timer.start();
				step = 11;
			break;
			// 6. Intake shoot
			case 11:
				if(timer.get() < time)
				{
					setSpeed(0, 0);
					pickUp(.5);
				}
				else
				{
					timer.reset();
					step = 12;
				}
			break;
			// Stop and reset everything
			case 12:
				setSpeed(0, 0);
				pickUp(0);
				shoot(0);
				timer.reset();
			break;
		}
	}

	// Set the speed for the motors
	public static void setSpeed(double lSpeed, double rSpeed)
	{
		double targetVelocityRight = rSpeed * Constants.velocityConstant;
		double targetVelocityLeft = lSpeed * Constants.velocityConstant;
		rightSRX.set(ControlMode.Velocity, targetVelocityRight);
		leftSRX.set(ControlMode.Velocity, targetVelocityLeft);
	}

	// Function to run the intake
	public static void runIntake(double lTrigger, double rTrigger, boolean auto, double lSpeed, double rSpeed, boolean poopyShoot)
	{
		if(!auto)
		{
			if(poopyShoot)//poopyShoot
			{
				rightIntakeMotor.set(ControlMode.PercentOutput, -Constants.poopyShoot);
				leftIntakeMotor.set(ControlMode.PercentOutput, -Constants.poopyShoot);
			}
			else if(lTrigger > 0)//shoot
			{	
				rightIntakeMotor.set(ControlMode.PercentOutput, -lTrigger *1);
				leftIntakeMotor.set(ControlMode.PercentOutput, -lTrigger *1);
			}
			else if(rTrigger > 0)//intake
			{
				rightIntakeMotor.set(ControlMode.PercentOutput, rTrigger*0.8);
				leftIntakeMotor.set(ControlMode.PercentOutput, rTrigger*.7);
			}
			else
			{
				rightIntakeMotor.set(ControlMode.PercentOutput, 0);
				leftIntakeMotor.set(ControlMode.PercentOutput, 0);
			}
		
		}
		else
		{
			rightIntakeMotor.set(ControlMode.PercentOutput, rSpeed);
			leftIntakeMotor.set(ControlMode.PercentOutput, lSpeed);
		}
	}

	// Function for the intake to shoot
	public static void shoot(double speed)
	{
		runIntake(0, 0, true, speed, speed, false);
	}
	
	// Function for the intake to pick up
	public static void pickUp(double speed)
	{
		runIntake(0, 0, true, -speed, -speed, false);
	}
}
