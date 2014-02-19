/*
 *   Copyright (C) 2014 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.pcgamingfreaks.georgh.MarriageMaster.Listener;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import at.pcgamingfreaks.georgh.MarriageMaster.MarriageMaster;

public class Death implements Listener 
{
	private MarriageMaster marriageMaster;

	public Death(MarriageMaster marriageMaster) 
	{
		this.marriageMaster = marriageMaster;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
    {
		if(this.marriageMaster.config.GetBonusXPEnabled())
		{
			if(event.getEntityType() != EntityType.PLAYER)
			{
				Player killer = event.getEntity().getKiller();
				if(killer != null)
				{
					String partner = this.marriageMaster.DB.GetPartner(killer.getName());
					if(partner != null)
					{
						Player otherPlayer = this.marriageMaster.getServer().getPlayer(partner);
						
						if(otherPlayer != null && otherPlayer.isOnline())
						{
							if(this.getRadius(killer, otherPlayer))
							{
								int amount = this.marriageMaster.config.GetBonusXPAmount();
								int droppedXp = event.getDroppedExp();
								
								int xp = (droppedXp / 2) * amount;
								
								otherPlayer.giveExp(xp);
								killer.giveExp(xp);
								
								event.setDroppedExp(0);
							}
						}
					}
				}
			}
		}
    }
	
	private boolean getRadius(Player player, Player otherPlayer) 
	{
		Location pl = player.getLocation();
		Location opl = otherPlayer.getLocation();
		
		if(pl.distance(opl) <= 10 && opl.distance(pl) <= 10)
		{
			return true;
		}
		
		return false;
	}
}
