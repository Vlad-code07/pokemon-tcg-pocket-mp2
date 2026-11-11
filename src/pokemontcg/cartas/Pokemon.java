package pokemontcg.cartas;

import java.util.ArrayList;
import java.util.List;

public class Pokemon extends Carta {

    private String tipo;
    private int puntosSalud;
    private int puntosSaludMaximos;
    private int costoRetirada;
    private String debilidad;
    private String etapa; // Campo de etapa para la regla del mazo
    private List<Ataque> ataques;
    private List<String> energiasAdjuntas;
    private boolean esEx;

    // Constructor sin debilidad
    public Pokemon(String nombre, String tipo, int puntosSalud,
                   int costoRetirada, boolean esEx) {
        this(nombre, tipo, puntosSalud, costoRetirada, "", "Básico", esEx);
    }

    // Constructor con debilidad y etapa por defecto
    public Pokemon(String nombre, String tipo, int puntosSalud,
                   int costoRetirada, String debilidad, boolean esEx) {
        this(nombre, tipo, puntosSalud, costoRetirada, debilidad, "Básico", esEx);
    }

    // Constructor completo
    public Pokemon(String nombre, String tipo, int puntosSalud,
                   int costoRetirada, String debilidad, String etapa, boolean esEx) {
        super(nombre, "Pokemon");
        this.tipo = tipo;
        this.puntosSalud = puntosSalud;
        this.puntosSaludMaximos = puntosSalud;
        this.costoRetirada = costoRetirada;
        this.debilidad = debilidad;
        this.etapa = (etapa != null && !etapa.isBlank()) ? etapa : "Básico";
        this.esEx = esEx;
        this.ataques = new ArrayList<>();
        this.energiasAdjuntas = new ArrayList<>();
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String getCategoria() {
        return tipo;
    }

    public int getPuntosSalud() {
        return puntosSalud;
    }

    public int getPuntosSaludMaximos() {
        return puntosSaludMaximos;
    }

    public int getCostoRetirada() {
        return costoRetirada;
    }

    public String getDebilidad() {
        return debilidad;
    }

    public String getEtapa() {
        return etapa;
    }

    public List<Ataque> getAtaques() {
        return new ArrayList<>(ataques);
    }

    public List<String> getEnergiasAdjuntas() {
        return new ArrayList<>(energiasAdjuntas);
    }

    public boolean esEx() {
        return esEx;
    }

    public int getPuntosPremio() {
        return esEx ? 2 : 1;
    }

    public void agregarAtaque(Ataque ataque) {
        if (ataque != null) {
            ataques.add(ataque);
        }
    }

    public void recibirDanio(int danio) {
        if (danio > 0) {
            puntosSalud -= danio;
            if (puntosSalud < 0) {
                puntosSalud = 0;
            }
        }
    }

    public void curar(int cantidad) {
        if (cantidad > 0) {
            puntosSalud += cantidad;
            if (puntosSalud > puntosSaludMaximos) {
                puntosSalud = puntosSaludMaximos;
            }
        }
    }

    public void adjuntarEnergia(String tipoEnergia) {
        if (tipoEnergia != null && !tipoEnergia.isBlank()) {
            energiasAdjuntas.add(tipoEnergia);
        }
    }

    public String getTextoEnergiasAdjuntas() {
        int cantidad = energiasAdjuntas.size();
        return "Energías adjuntas: " + cantidad + " (" + getTipo() + ")";
    }

    public boolean descartarEnergia(String tipoEnergia) {
        return energiasAdjuntas.remove(tipoEnergia);
    }

    public boolean estaDebilitado() {
        return getPuntosSalud() <= 0;
    }
}