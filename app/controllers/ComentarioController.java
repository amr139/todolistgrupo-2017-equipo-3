package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import play.data.DynamicForm;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import services.UsuarioService;
import models.Usuario;
import models.Tarea;
import models.Columna;
import models.Tablero;
import models.Comentario;
import models.Notificacion;
import services.ComentarioService;
import services.TareaService;
import services.NotificacionService;
import java.util.List;
import java.util.ArrayList;


import security.ActionAuthenticator;


public class ComentarioController extends Controller {

	@Inject FormFactory formFactory;
	@Inject ComentarioService comentarioService;
	@Inject TareaService tareaService;
	@Inject UsuarioService usuarioService;
	@Inject NotificacionService notificacionService;

	@Security.Authenticated(ActionAuthenticator.class)
	public Result ListarComentarios(Long idUsuario, Long idTarea) {
		String connectedUserStr = session("connected");
		Long connectedUser = Long.valueOf(connectedUserStr);

	  	String aviso = flash("aviso");
	  	Tarea tarea = tareaService.obtenerTarea(idTarea);
	  	List<Comentario> lista = comentarioService.findAllComentsByTareaId(idTarea);
	  	return ok(listarComentarios.render(tarea, lista, connectedUser, aviso));
	}

	@Security.Authenticated(ActionAuthenticator.class)
	public Result accionNuevoComentario(Long idUser,Long idTarea) {
		String connectedUserStr = session("connected");
        Long connectedUser =  Long.valueOf(connectedUserStr);

       	Form<Comentario> comentarioForm = formFactory.form(Comentario.class).bindFromRequest();
       	if (comentarioForm.hasErrors()) {
			Tarea tarea = tareaService.obtenerTarea(idTarea);
			List<Comentario> vacia = new ArrayList<Comentario>();
          	return badRequest(listarComentarios.render(tarea,vacia,idUser, "Hay errores en el formulario"));
       	}
	   	DynamicForm requestData = formFactory.form().bindFromRequest();
	   	String mensaje = requestData.get("comment");
	   	Comentario c = comentarioService.crearComentario(idTarea,idUser,mensaje,"none");

		Usuario crea = usuarioService.findUsuarioPorId(idUser);

	   	Tarea t  = tareaService.obtenerTarea(idTarea);
	   	Columna col = t.getColumna();
	   	Tablero ta = col.getTablero();
	   	List<Usuario> participa = new ArrayList<Usuario>();
		participa.addAll(ta.getParticipantes());

	   	for(Usuario user : participa) {
			if(user.getId()!=idUser){
				Notificacion n = notificacionService.crearNotificacion(user.getId(), "Ha comentado el usuario "+crea.getLogin()+" en la tarea: " + t.getTitulo());
			}
	   	}
		Usuario admin = ta.getAdministrador();
		if(admin.getId()!=idUser){
			Notificacion n = notificacionService.crearNotificacion(admin.getId(), "Ha comentado el usuario "+crea.getLogin()+" en la tarea: " + t.getTitulo());
		}

	   flash("aviso","El comentario se ha grabado correctamente");
	   return redirect(controllers.routes.ComentarioController.ListarComentarios(idUser,idTarea));
	}

	@Security.Authenticated(ActionAuthenticator.class)
	public Result accionBorrarComentario(Long idComentario,Long idUsuario,Long idTarea) {
	  	comentarioService.borrarComentario(idComentario,idUsuario);
		flash("aviso","Tarea borrada correctamente");
		return ok();
	}

}
