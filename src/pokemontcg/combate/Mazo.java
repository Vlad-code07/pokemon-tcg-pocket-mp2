package pokemontcg.combate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pokemontcg.cartas.Carta;

public class Mazo {
    private List<Carta> cartas;

    // 1. Constructor vacío (Resuelve: The constructor Mazo() is undefined)
    public Mazo() {
        this.cartas = new ArrayList<>();
    }

    // 2. Constructor que recibe una lista inicial
    public Mazo(List<Carta> cartas) {
        this.cartas = (cartas != null) ? cartas : new ArrayList<>();
    }

    // 3. Método agregarCarta con polimorfismo (Resuelve: agregarCarta is undefined for type Mazo)
    public void agregarCarta(Carta carta) {
        if (carta != null) {
            this.cartas.add(carta);
        }
    }

    public Carta robarCarta() {
        if (!estaVacio()) {
            return cartas.remove(0);
        }
        return null;
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public List<Carta> getCartas() {
        return cartas;
    }
}