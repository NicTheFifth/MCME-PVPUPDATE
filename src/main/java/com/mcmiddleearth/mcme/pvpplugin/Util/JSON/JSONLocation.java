package com.mcmiddleearth.mcme.pvpplugin.Util.JSON;

/*
 * This file is part of MCME-pvp.
 *
 * MCME-pvp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MCME-pvp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MCME-pvp.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
public class JSONLocation {

    @Getter @Setter
    private int x;

    @Getter @Setter
    private int y;

    @Getter @Setter
    private int z;

    @Getter @Setter
    private String world;

    public JSONLocation(){}

    public JSONLocation toJSONLocation(Location location){
        JSONLocation returnLoc = new JSONLocation();
        returnLoc.x = location.getBlockX();
        returnLoc.y = location.getBlockY();
        returnLoc.z = location.getBlockZ();
        returnLoc.world = location.getWorld().getName();
        return returnLoc;
    }

    public Location toBukkitLoc(){
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
