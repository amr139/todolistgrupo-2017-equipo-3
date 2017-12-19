package services;

import javax.inject.*;

import models.Usuario;
import models.UsuarioRepository;

import models.Notificacion;
import models.NotificacionRepository;
import java.util.List;


public class NotificacionService {
	UsuarioRepository usuarioRepository;
	NotificacionRepository notificacionRepository;

	@Inject
	public NotificacionService(UsuarioRepository usuarioRepository, NotificacionRepository notificacionRepository){
		this.usuarioRepository = usuarioRepository;
		this.notificacionRepository = notificacionRepository;
	}

	public List<Notificacion> findAllNoteByUserId(Long idUser){
		return notificacionRepository.findAllNoteByUser(idUser);
	}

	public Notificacion crearNotificacion(Long idUsuario,String notificacion){
		Usuario usuario = usuarioRepository.findById(idUsuario);
		if(usuario==null)
			throw new NotificacionServiceException("No existe el usuario");
		Notificacion note = new Notificacion(usuario, notificacion);
		return notificacionRepository.create(note);
	}

	public Notificacion marcarLeidaNotificacion(Long idNote){

		Notificacion notificacion = notificacionRepository.findById(idNote);
		if(notificacion==null)
			throw new NotificacionServiceException("No existe la notificacion");
		notificacion.setEstado(true);
		return notificacionRepository.create(notificacion);
	}

	public Notificacion obtenerNotificacion(Long idNote){
		Notificacion notificacion = notificacionRepository.findById(idNote);
		if(notificacion==null)
			throw new NotificacionServiceException("No existe la notificacion");
		return notificacion;
	}

	public void borrarNotificacion(Long idNote){
		Notificacion notificacion = notificacionRepository.findById(idNote);
		if(comentario==null)
			throw new ComentarioServiceException("No existe la tarea");
		notificacionRepository.delete(idNote);
	}

}
