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
import models.Notificacion;
import services.NotificacionService;

import java.util.List;
import java.util.ArrayList;


import security.ActionAuthenticator;


public class NotificacionController extends Controller {

	@Inject FormFactory formFactory;
	@Inject NotificacionService notificacionService;

	@Security.Authenticated(ActionAuthenticator.class)
	public Result ListarNotificaciones(Long idUsuario) {
		String connectedUserStr = session("connected");
        Long connectedUser =  Long.valueOf(connectedUserStr);
        if (connectedUser != idUsuario) {
           return unauthorized("Lo siento, no estás autorizado");
	   } else {
		   	String aviso = flash("aviso");
			List<Notificacion> lista = notificacionService.findAllNoteByUserId(idUsuario);
			return ok(notificaciones.render(lista, idUsuario, aviso));
		}
	}


}
