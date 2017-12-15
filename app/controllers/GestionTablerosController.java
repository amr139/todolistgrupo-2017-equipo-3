package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import java.util.List;
import java.util.ArrayList;

import services.UsuarioService;
import services.TareaService;
import services.TableroService;
import models.Usuario;
import models.Tarea;
import models.Tablero;
import models.Columna;

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
            return redirect(controllers.routes.GestionTablerosController.listarMisTableros(idUsuario));
        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result listarMisTableros(Long idUsuario){
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            String aviso = flash("aviso");
            Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
            List<Tablero> tablerosADMIN = tableroService.allTablerosAdministradosUser(idUsuario);
            List<Tablero> tablerosJOINED = tableroService.allTablerosParticipadosUser(idUsuario);
            return ok(listadoMisTableros.render(tablerosADMIN, tablerosJOINED, usuario, aviso));
        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result listarOtrosTableros(Long idUsuario){
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            String aviso = flash("aviso");
            Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
            List<Tablero> tablerosFREE = tableroService.allTablerosNoApuntadosUser(idUsuario);
            return ok(listadoOtrosTableros.render(tablerosFREE, usuario, aviso));
        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result añadirParticipante(Long idUsuario,Long idTablero){
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            Tablero tablero = tableroService.encontrarTablero(idTablero);
            if(tablero==null) return notFound("Tablero no encontrado");
            tableroService.añadirParticipante(idUsuario,idTablero);
            return redirect(controllers.routes.GestionTablerosController.listarMisTableros(idUsuario));

        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result mostrarDetalleTablero(Long idUsuario, Long idTablero){
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            Tablero tablero = tableroService.encontrarTablero(idTablero);
            if(tablero==null) return notFound("Tablero no encontrado");
            List<Usuario> lista = new ArrayList<Usuario>();
            lista.addAll(tablero.getParticipantes());
            return ok(desgloseTablero.render(tablero,lista, idUsuario));
        }
    }
    @Security.Authenticated(ActionAuthenticator.class)
    public Result formAñadirColumna(Long idTablero,Long idUsuario) {
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            return ok(formAnyadirColumna.render(formFactory.form(Columna.class),idTablero,idUsuario));
        }
    }
    @Security.Authenticated(ActionAuthenticator.class)
    public Result añadirColumna(Long idTablero ,Long idUsuario){
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != idUsuario) {
            return unauthorized("Lo siento, no estas autorizado");
        } else {
            Form<Columna> form = formFactory.form(Columna.class).bindFromRequest();
            Columna datos = form.get();
            String nombreTablro = datos.getNombre();
            tableroService.anyadirColumna(idTablero,nombreTablro);
            return redirect(controllers.routes.GestionTablerosController.mostrarDetalleTablero(idUsuario,idTablero));
        }
    }
}
