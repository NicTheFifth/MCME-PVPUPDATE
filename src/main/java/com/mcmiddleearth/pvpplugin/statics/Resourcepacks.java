package com.mcmiddleearth.pvpplugin.statics;

import java.util.HashSet;
import java.util.Set;

public class Resourcepacks {

    public static final String DWARVEN = "dwarven";
    public static final String HUMAN = "human";
    public static final String ERIADOR = "eriador";
    public static final String ROHAN = "rohan";
    public static final String PATHSOFTHEDEAD = "pathsofthedead";

    public static final HashSet<String> getAll = new HashSet<>(
        Set.of(DWARVEN,HUMAN,ERIADOR,ROHAN,PATHSOFTHEDEAD));
}
