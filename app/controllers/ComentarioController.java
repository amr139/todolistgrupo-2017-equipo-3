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
import models.Comentario;
import services.ComentarioService;
import services.TareaService;
import java.util.List;
import java.util.ArrayList;


import security.ActionAuthenticator;


public class ComentarioController extends Controller {

	@Inject FormFactory formFactory;
	@Inject ComentarioService comentarioService;
	@Inject TareaService tareaService;

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

	   flash("aviso","El comentario se ha grabado correctamente");
	   return redirect(controllers.routes.ComentarioController.ListarComentarios(idUser,idTarea));
	}

	@Security.Authenticated(ActionAuthenticator.class)
	public Result accionBorrarComentario(Long idComentario,Long idUsuario,Long idTarea) {
	  	comentarioService.borrarComentario(idComentario,idUsuario);
		flash("aviso","Tarea borrada correctamente");
		return ok();
	}

/*	public Result formularioEditarComentario() {

	}

	public Result accionNuevoComentario() {

	}

	public Result accionEditarComentario() {

	}

	public Result accionBorrarComentario() {

	}
*/


}
