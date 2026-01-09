package es.deusto.sd.auctions.client.data;

public enum NivelLlenado {
    VERDE(0, 25),
    NARANJA(26, 75),
    ROJO(76, 100);

    private final int minimo;
    private  final int maximo;

    NivelLlenado(int i, int i1) {
        this.minimo = i;
        this.maximo = i1;
    }

    public int getMinimo() {
        return minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    public static NivelLlenado getNivelLlenado(int capacidadActual, int capacidadMaxima) {
        int porcentajeLlenado = (capacidadActual * 100) / capacidadMaxima;

        for (NivelLlenado nivel : NivelLlenado.values()) {
            if (porcentajeLlenado >= nivel.getMinimo() && porcentajeLlenado <= nivel.getMaximo()) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nivel de llenado no válido");
    }

}
