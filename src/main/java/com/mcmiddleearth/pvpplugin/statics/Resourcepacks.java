package com.mcmiddleearth.pvpplugin.statics;

import java.util.HashSet;
import java.util.Set;

public class Resourcepacks {

    public static final String DWARVEN;
    public static final String HUMAN;
    public static final String ERIADOR;
    public static final String ROHAN;
    public static final String PATHSOFTHEDEAD;

    static{
        DWARVEN = "dwarven";
        HUMAN = "human";
        ERIADOR = "eriador";
        ROHAN = "rohan";
        PATHSOFTHEDEAD = "pathsofthedead";
    }

    public static final HashSet<String> getAll = new HashSet<>(
        Set.of(DWARVEN,HUMAN,ERIADOR,ROHAN,PATHSOFTHEDEAD));
}
