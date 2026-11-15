package pokemontcg.cartas;

public class PokemonEvolucion extends Pokemon {

    private String nombrePreEvolucion;
    

    public PokemonEvolucion(String nombre, String tipo, int puntosSalud,
                            int costoRetirada, String debilidad, String etapa,
                            boolean esEx, String nombrePreEvolucion) {
        super(nombre, tipo, puntosSalud, costoRetirada, debilidad, etapa, esEx);
        this.nombrePreEvolucion = nombrePreEvolucion;
        
    }

    public String getNombrePreEvolucion() {
        return nombrePreEvolucion;
    }

}
