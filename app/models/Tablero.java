package models;

public class Tablero {

  private String nombre;
  private Usuario Administrador;

  public Tablero() {}

  public Tablero(Usuario usuario, String titulo) {
     this.Administrador = usuario;
     this.nombre = titulo;
  }

  public Usuario getAdministrador() {
    return this.Administrador;
  }

  public String getNombre() {
    return this.nombre;
  }


}
