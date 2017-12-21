package models;

import javax.persistence.*;
import models.Usuario;
import models.Tarea;

@Entity
public class Comentario {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;

	@ManyToOne
    @JoinColumn(name="tareaId")
    private Tarea tarea;

	@ManyToOne
	@JoinColumn(name="usuarioId")
	private Usuario usuario;

	private String mensaje;

	private String fileUrl;

	public Comentario() {}

	public Comentario(Tarea t, Usuario u, String msg, String uri){
		this.tarea = t;
		this.usuario = u;
		this.mensaje = msg;
		this.fileUrl = uri;
	}

	public Long getId() {
      return id;
    }

	public void setId(Long id) {
	  this.id = id;
	}

	public Tarea getTarea(){
		return this.tarea;
	}

	public void setTarea(Tarea tarea){
		this.tarea = tarea;
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

	public String getURL(){
		return this.fileUrl;
	}

	public void setURL(String url){
		this.fileUrl = url;
	}

}
