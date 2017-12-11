package models;

import javax.persistence.*;

import models.Tablero;
import models.Tarea;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Columna {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    private String nombre;
    @ManyToOne
    @JoinColumn(name="tableroId")
    private Tablero tablero;
    // relación una a muchos entre columna y tarea
    @OneToMany(mappedBy="columna", fetch=FetchType.EAGER)
    private Set<Tarea> tareas = new HashSet<Tarea>();

    public Columna() {}

    public Columna(Tablero tablero, String nombre) {
        this.tablero = tablero;
        this.nombre = nombre;
    }
    public Set<Tarea> getTareas() {
        return this.tareas;
    }
    public Tablero getTablero() {
        return this.tablero;
    }
    public String getNombre() {
        return this.nombre;
    }
    public Long getId() {
        return this.id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
    @Override
    public int hashCode() {
       final int prime = 31;
       int result = prime + ((nombre == null) ? 0 : nombre.hashCode());
       return result;
    }
    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (getClass() != obj.getClass()) return false;
       Columna other = (Columna) obj;
       // Si tenemos los ID, comparamos por ID
       if (id != null && other.id != null)
          return ((long) id == (long) other.id);
       // sino comparamos por campos obligatorios
       else {
          if (nombre == null) {
             if (other.nombre != null) return false;
          } else {
              if(!this.nombre.equals(other.nombre)) return false;
          }
       }
       return true;
    }
}