package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import services.UsuarioService;
import services.TareaService;
import models.Usuario;
import models.Tarea;
import security.ActionAuthenticator;

public class GestionTareasController extends Controller {

   @Inject FormFactory formFactory;
   @Inject UsuarioService usuarioService;
   @Inject TareaService tareaService;

   // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
   // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         return ok(formNuevaTarea.render(usuario, formFactory.form(Tarea.class),""));
    }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result creaNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
         if (tareaForm.hasErrors()) {
            Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
            return badRequest(formNuevaTarea.render(usuario, formFactory.form(Tarea.class), "Hay errores en el formulario"));
         }
         Tarea tarea = tareaForm.get();
         tareaService.nuevaTarea(idUsuario, tarea.getTitulo(), tarea.getFechaLimite());
         flash("aviso","La tarea se ha grabado correctamente");
         return redirect(controllers.routes.GestionTareasController.listaTareas(idUsuario));
      }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result listaTareas(Long idUsuario){
      String connectedUserStr = session("connected");
      Long connectedUser = Long.valueOf(connectedUserStr);
      if(connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estas autorizado");
      } else {
        String aviso = flash("aviso");
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);
        return ok(listaTareas.render(tareas, usuario, aviso));
      }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result formularioEditaTarea(Long idTarea){
      Tarea tarea = tareaService.obtenerTarea(idTarea);
      if(tarea == null) {
        return notFound("Tarea no encontrada");
      }else {
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if(connectedUser != tarea.getUsuario().getId()){
          return unauthorized("Lo siento, no estas autorizado");
        } else {
          return ok(formModificacionTarea.render(tarea.getUsuario().getId(), tarea.getId(),tarea.getTitulo(),""));
        }

      }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result grabaTareaModificada(Long idTarea) throws java.text.ParseException{
      DynamicForm requestData = formFactory.form().bindFromRequest();
      String nuevoTitulo = requestData.get("titulo");
      String inputFechaLimite = requestData.get("fecha");

      SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
      Date nuevaFechaLimite = formateador.parse(inputFechaLimite);

      Tarea tarea = tareaService.modificaTarea(idTarea,nuevoTitulo,nuevaFechaLimite);
      return redirect(controllers.routes.GestionTareasController.listaTareas(tarea.getUsuario().getId()));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result borraTarea(Long idTarea) {
      tareaService.borraTarea(idTarea);
        flash("aviso","Tarea borrada correctamente");
        return ok();

    }
 }
