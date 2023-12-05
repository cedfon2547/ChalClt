package ca.ulaval.glo2004.domaine;

public enum TypeSensToit {
    Nord,
    Est,
    Sud,
    Ouest;

    public static String[] getNames() {
        TypeSensToit[] types = values();
        String[] names = new String[types.length];

        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].name();
        }

        return names;
    }
}
