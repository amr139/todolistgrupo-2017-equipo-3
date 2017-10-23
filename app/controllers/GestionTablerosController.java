package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import services.UsuarioService;
import services.TareaService;
import services.TableroService;
import models.Usuario;
import models.Tarea;
import models.Tablero;

import security.ActionAuthenticator;



public class GestionTablerosController extends Controller {
  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TableroService tableroService;

  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioNuevoTablero(Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
       Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
       return ok(formNuevoTablero.render(usuario, formFactory.form(Tablero.class),""));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result creaNuevoTablero(Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
       Form<Tablero> tableroForm = formFactory.form(Tablero.class).bindFromRequest();
       if (tableroForm.hasErrors()) {
          Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
          return badRequest(formNuevoTablero.render(usuario, formFactory.form(Tablero.class), "Hay errores en el formulario"));
       }
       Tablero tablero = tableroForm.get();
       tableroService.nuevoTablero(idUsuario, tablero.getNombre());
       int numTableros = tableroService.allTablerosAdministradosUser(idUsuario).size();
       return ok(saludo.render("Total tableros administrados: " + numTableros));
    }
  }

}
