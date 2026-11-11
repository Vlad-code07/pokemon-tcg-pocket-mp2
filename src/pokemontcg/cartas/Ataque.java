package pokemontcg.cartas;

public class Ataque {
    private String nombre;
    private String tipoEnergia;
    private int costoEnergia;
    private int danioBase;

    public Ataque(String nombre, String tipoEnergia, int costoEnergia, int danioBase) {
        this.nombre = nombre;
        this.tipoEnergia = tipoEnergia;
        this.costoEnergia = costoEnergia;
        this.danioBase = danioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoEnergia() {
        return tipoEnergia;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }

    public int getDanioBase() {
        return danioBase;
    }
}