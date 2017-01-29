package com.massivecraft.vampire.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.vampire.BloodFlaskUtil;
import com.massivecraft.vampire.Perm;
import com.massivecraft.vampire.entity.MLang;
import com.massivecraft.vampire.type.TypeLimitedDouble;

public class CmdVampireFlask extends VCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdVampireFlask()
	{
		// Aliases
		this.addAliases("flask");
		
		// Parameters
		this.addParameter(TypeLimitedDouble.get(0D, 20D), "amount", "4.0").setDefaultValue(4D);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.FLASK));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Parameters
		double amount = this.readArg();
		
		// If the player is holding a glass bottle ...
		if (BloodFlaskUtil.playerHoldsGlassBottle(vme.getPlayer()))
		{
			// ... and has the required amount ...
			if ((vme.isVampire() && amount>vme.getFood().get()) || ( ! vme.isVampire() && amount>vme.getPlayer().getHealth()))
			{
				vme.msg(MLang.get().flaskInsufficient);
				return;
			}
			
			// ... create a blood flask!
			if (vme.isVampire())
			{
				vme.getFood().add(-amount);
			}
			else
			{
				vme.getPlayer().setHealth(vme.getPlayer().getHealth()-amount);
			}
			BloodFlaskUtil.fillBottle(amount, vme);
			vme.msg(MLang.get().flaskSuccess);
		}
		else
		{
			vme.msg(MLang.get().flaskNoBottle);
		}
	}
	
}
