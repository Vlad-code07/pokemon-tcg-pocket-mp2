package pokemontcg.combate;

import pokemontcg.cartas.Carta;
import pokemontcg.cartas.Pokemon;

public class Jugador {
    private String nombre;
    private Mano mano;
    private Mazo mazo;
    private Banco banco;
    private Pokemon pokemonActivo;
    private int zonaEnergia;
    private int puntosPremio;

    // Constructor que recibe solo el nombre (Resuelve el error en App.java)
    public Jugador(String nombre) {
        this(nombre, new Mazo());
    }

    // Constructor que recibe nombre y mazo
    public Jugador(String nombre, Mazo mazo) {
        this.nombre = nombre;
        this.mano = new Mano();
        this.mazo = mazo;
        this.banco = new Banco();
        this.pokemonActivo = null;
        this.zonaEnergia = 0;
        this.puntosPremio = 0;
    }

    public void robarCarta() {
        if (!mazo.estaVacio()) {
            Carta cartaRobada = mazo.robarCarta();
            mano.agregarCarta(cartaRobada);
            System.out.println("[ROBO] ¡" + nombre + " robó la carta " + cartaRobada.getNombre() + "!");
        } else {
            System.out.println("[ROBO] " + nombre + " intentó robar carta pero el mazo está vacío.");
        }
    }

    public String getNombre(){
        return nombre;
    }

    public Mano getMano() {
        return mano;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public Banco getBanco() {
        return banco;
    }

    public Pokemon getPokemonActivo() {
        return pokemonActivo;
    }

    public void setPokemonActivo(Pokemon pokemonActivo) {
        this.pokemonActivo = pokemonActivo;
    }

    public int getZonaEnergia() {
        return zonaEnergia;
    }

    public void agregarEnergiaZona(){
        this.zonaEnergia++;
    }

    public void gastarEnergiaZona(){
        if (this.zonaEnergia > 0){
            this.zonaEnergia--;
        }
    }

    public int getPuntosPremio() {
        return puntosPremio;
    }

    public void agregarPuntosPremio(int puntos) {
        this.puntosPremio += puntos;
    }

    public boolean ganoJuego(){
        return this.puntosPremio >= 3;
    }
}
