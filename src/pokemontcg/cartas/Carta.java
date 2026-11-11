package pokemontcg.cartas;

public abstract class Carta {

    protected String nombre;
    protected String categoria;

    public Carta(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    
    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

   
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
