package models;

import javax.persistence.*;
import models.Usuario;

@Entity
public class Notificacion {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;

	@ManyToOne
	@JoinColumn(name="usuarioId")
	private Usuario usuario;

	private String mensaje;

	private Boolean leida;

	public Notificacion() {}

	public Notificacion(Usuario u, String msg){
		this.usuario = u;
		this.mensaje = msg;
		this.leida = false;
	}

	public Long getId() {
      return id;
    }

	public void setId(Long id) {
	  this.id = id;
	}

	public Usuario getUsuario(){
		return this.usuario;
	}

	public void setUsuario(Usuario user){
		this.usuario = user;
	}

	public String getMensaje(){
		return this.mensaje;
	}

	public void setMensaje(String msg){
		this.mensaje = msg;
	}

	public Boolean getEstado(){
		return this.leida;
	}

	public void setEstado(Boolean estado){
		this.leida = estado;
	}

}
