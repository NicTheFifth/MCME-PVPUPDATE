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
package com.mcmiddleearth.mcme.pvpplugin.Util;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
public class DBmanager {
    
    public static boolean saveObj(Object obj, File loc, String name){

        return false;
    }
    
    public static Object loadObj(Class type, File loc){
        return null;
    }
    
    public static HashMap<String, Object> loadAllObj(Class Type, File loc){
        if(!loc.exists()){
            loc.mkdirs();
            return null;
        }
        HashMap<String, Object> rtn = new HashMap<String, Object>();
        for(File f : loc.listFiles()){
            rtn.put(f.getName(), loadObj(Type, f));
        }
        return rtn;
    }
}
