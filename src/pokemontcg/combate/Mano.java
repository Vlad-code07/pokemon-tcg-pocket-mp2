package pokemontcg.combate;

import java.util.ArrayList;
import java.util.List;

import pokemontcg.cartas.Carta;

public class Mano {
    private List<Carta> cartas;

    public Mano(){
        this.cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta carta) {
        if (carta != null) {
            this.cartas.add(carta);
        }
    }

    public Carta removerCarta(int indice){
        if (indice >= 0 && indice < cartas.size()){
            return cartas.remove(indice);
        }
        return null;
    }

    public void mostrarMano(){
        System.out.println("Tu mano (" + cartas.size() + " cartas):");
        for (int i = 0; i < cartas.size(); i++){
            System.out.println((i + 1) + ". " + cartas.get(i).getNombre());
        }
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public int getCantidadCartas() {
        return cartas.size();
    }
}
