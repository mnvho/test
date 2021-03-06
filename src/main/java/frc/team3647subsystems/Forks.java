package frc.team3647subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.team3647subsystems.*;
import frc.team3647inputs.*;
import frc.robot.*;

public class Forks
{
	public static DoubleSolenoid piston = new DoubleSolenoid(Constants.forksPinSourceA, Constants.forksPinSourceB);
	
	public static void deployForks() 
	{
		piston.set(DoubleSolenoid.Value.kForward);
	}
	
	public static void lockTheForks() 
	{
		piston.set(DoubleSolenoid.Value.kReverse);
	}
	
	public static void runPiston(boolean joyvalue) 
	{
		if(joyvalue)
		{
			if(piston.get() == DoubleSolenoid.Value.kReverse)
			{
				deployForks();
				Timer.delay(.75);
			}
			else
			{
				lockTheForks();
				Timer.delay(.75);
			}
		}
	}
}