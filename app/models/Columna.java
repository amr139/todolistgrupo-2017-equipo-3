package models;

import javax.persistence.*;

import models.Tablero;

@Entity
public class Columna {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    private String nombre;
    @ManyToOne
    @JoinColumn(name="tablero")
    private Tablero tablero;

    public Columna() {}

    public Columna(Tablero tablero, String nombre) {
        this.tablero = tablero;
        this.nombre = nombre;
    }

    public Tablero getTablero() {
        return this.tablero;
    }
    public String getNombre() {
        return this.nombre;
    }
}