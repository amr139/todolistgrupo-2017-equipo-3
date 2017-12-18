package services;

import javax.inject.*;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;
import models.Comentario;
import models.ComentarioRepository;


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
		return tareaRepository.findAllComentsByTarea(idTarea);
	}

	public Comentario crearComentario(Long idTarea,Long idUsuario,String comentario, String url){
		Usuario usuario = usuarioRepository.findById(idUsuario);
		if(usuario==null)
			throw new ComentarioServiceException("No existe el usuario");
		Tarea tarea = tareaRepository.findById(idTarea);
		if(tarea==null)
			throw new ComentarioServiceException("No existe la tarea");
		Comentario comentario = new Comentario(tarea,usuario,comentario,url);
		return comentarioRepository.create(comentario)
	}

}
