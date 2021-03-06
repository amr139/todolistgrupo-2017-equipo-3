package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;

import models.Columna;
import models.Comentario;

@Entity
public class Tarea {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private Long id;
   private String titulo;
   private boolean terminado;
   @Temporal(TemporalType.DATE)
   private Date fechaCreacion;
   @Temporal(TemporalType.DATE)
   private Date fechaLimite;
   // Relación muchos-a-uno entre tareas y usuario
   @ManyToOne
   // Nombre de la columna en la BD que guarda físicamente
   // el ID del usuario con el que está asociado una tarea
   @JoinColumn(name="usuarioId")
   public Usuario usuario;

   @ManyToOne
   @JoinColumn(name="columnaId")
   private Columna columna;

   @OneToMany(mappedBy="tarea", fetch=FetchType.EAGER,cascade = CascadeType.REMOVE)
   private Set<Comentario> comentarios = new HashSet<Comentario>();

   public Tarea() {
     this.terminado = false;
   }

   public Tarea(Usuario usuario, String titulo, Date fechaLimite) {
      this.usuario = usuario;
      this.titulo = titulo;
      this.fechaLimite = fechaLimite;
      this.terminado = false;
   }

   public void setColumna(Columna columna) {
     this.columna=columna;
   }
   public Columna getColumna() {
     return this.columna;
   }

   // Getters y setters necesarios para JPA

    public boolean getTerminado(){
        return this.terminado;
    }
    public void setTerminado(boolean terminado){
        this.terminado = terminado;
    }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }

   public Date getFechaCreacion(){
     return fechaCreacion;
   }

   public void setFechaCreacion(Date fechaCreacion){
     this.fechaCreacion = fechaCreacion;
   }

   public Date getFechaLimite(){
     return fechaLimite;
   }

   public void setFechaLimite(Date fechaLimite){
     this.fechaLimite = fechaLimite;
   }

   public String toString() {
      return String.format("Tarea id: %s titulo: %s fechaLimite: %s usuario: %s",
                      id, titulo, fechaLimite, usuario.toString());
   }

   public Set<Comentario> getComentarios() {
     return this.comentarios;
   }

   public void setComentario(Set<Comentario> comentarios) {
     this.comentarios = comentarios;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((titulo == null) ? 0 : titulo.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Tarea other = (Tarea) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
      return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (titulo == null) {
            if (other.titulo != null) return false;
         } else if (!titulo.equals(other.titulo)) return false;
         if (usuario == null) {
            if (other.usuario != null) return false;
            else if (!usuario.equals(other.usuario)) return false;
         }
      }
      return true;
   }
}
