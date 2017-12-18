package services;

import javax.inject.*;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;
import models.Comentario;
import models.ComentarioRepository;

import java.util.List;

public class ComentarioService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;
  ComentarioRepository comentarioRepository;

  @Inject
  public ComentarioService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, ComentarioRepository comentarioRepository){
    this.usuarioRepository = usuarioRepository;
    this.tareaRepository = tareaRepository;
	this.comentarioRepository = comentarioRepository;
  }

	public List<Comentario> findAllComentsByTareaId(Long idTarea){
		return comentarioRepository.findAllComentsByTarea(idTarea);
	}

	public Comentario crearComentario(Long idTarea,Long idUsuario,String msg, String url){
		Usuario usuario = usuarioRepository.findById(idUsuario);
		if(usuario==null)
			throw new ComentarioServiceException("No existe el usuario");
		Tarea tarea = tareaRepository.findById(idTarea);
		if(tarea==null)
			throw new ComentarioServiceException("No existe la tarea");
		Comentario comentario = new Comentario(tarea,usuario,msg,url);
		return comentarioRepository.create(comentario);
	}

	public Comentario obtenerComentario(Long idComentario){
		return comentarioRepository.findById(idComentario);
	}

	public Comentario modificaComentario(Long idComentario, String nuevoMensaje, String nuevaURL){
		Comentario comentario = comentarioRepository.findById(idComentario);
		if(comentario==null)
			throw new ComentarioServiceException("No existe la tarea");
		comentario.setMensaje(nuevoMensaje);
		comentario.setURL(nuevaURL);
		comentario = comentarioRepository.update(comentario);
		return comentario;
	}

	public void borrarComentario(Long idComentario){
		Comentario comentario = comentarioRepository.findById(idComentario);
		if(comentario==null)
			throw new ComentarioServiceException("No existe la tarea");
		comentarioRepository.delete(idComentario);
	}
}
