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


import security.ActionAuthenticator;


public class ComentarioController extends Controller {

	@Inject FormFactory formFactory;
	@Inject ComentarioService comentarioService;
	@Inject TareaService tareaService;

	@Security.Authenticated(ActionAuthenticator.class)
	public Result ListarComentarios(Long idUsuario, Long idTarea) {
		String connectedUserStr = session("connected");
		Long connectedUser = Long.valueOf(connectedUserStr);
		if(connectedUser != idUsuario) {
		  return unauthorized("Lo siento, no estas autorizado");
		} else {
		  String aviso = flash("aviso");
		  Tarea tarea = tareaService.obtenerTarea(idTarea);
		  List<Comentario> lista = comentarioService.findAllComentsByTareaId(idTarea);
		  return ok(listarComentarios.render(tarea, lista, connectedUser, aviso));
		}
	}



/*	public Result formularioNuevoComentario() {

	}

	public Result formularioEditarComentario() {

	}

	public Result accionNuevoComentario() {

	}

	public Result accionEditarComentario() {

	}

	public Result accionBorrarComentario() {

	}
*/


}
