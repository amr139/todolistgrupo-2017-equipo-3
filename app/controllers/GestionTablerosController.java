package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import java.util.List;

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
       flash("aviso","El tablero se ha grabado correctamente");
       return redirect(controllers.routes.GestionTablerosController.listarTableros(idUsuario));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listarTableros(Long idUsuario){
    String connectedUserStr = session("connected");
    Long connectedUser = Long.valueOf(connectedUserStr);
    if(connectedUser != idUsuario) {
      return unauthorized("Lo siento, no estas autorizado");
    } else {
      String aviso = flash("aviso");
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      List<Tablero> tablerosJOINED = tableroService.allTablerosNoApuntadosUser(idUsuario);
      List<Tablero> tablerosADMIN = tableroService.allTablerosAdministradosUser(idUsuario);
      List<Tablero> tablerosFREE = tableroService.allTablerosNoApuntadosUser(idUsuario);
      return ok(listadoTableros.render(tablerosADMIN, tablerosJOINED, tablerosFREE, usuario, aviso));
    }
  }

}
