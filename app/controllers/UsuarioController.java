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
import security.ActionAuthenticator;

public class UsuarioController extends Controller {

   @Inject FormFactory formFactory;

   // Play injecta un usuarioService junto con todas las dependencias necesarias:
   // UsuarioRepository y JPAApi
   @Inject UsuarioService usuarioService;

   public Result saludo(String mensaje) {
      return ok(saludo.render("El mensaje que he recibido es: " + mensaje));
   }

   public Result formularioRegistro() {
      return ok(formRegistro.render(formFactory.form(Registro.class),""));
   }

   public Result registroUsuario() {
      Form<Registro> form = formFactory.form(Registro.class).bindFromRequest();
      if (form.hasErrors()) {
         return badRequest(formRegistro.render(form, "Hay errores en el formulario"));
      }
      Registro datosRegistro = form.get();

      if (usuarioService.findUsuarioPorLogin(datosRegistro.login) != null) {
         return badRequest(formRegistro.render(form, "Login ya existente: escoge otro"));
      }

      if (!datosRegistro.password.equals(datosRegistro.confirmacion)) {
         return badRequest(formRegistro.render(form, "No coinciden la contraseña y la confirmación"));
      }
      Usuario usuario = usuarioService.creaUsuario(datosRegistro.login, datosRegistro.email, datosRegistro.password);
      return redirect(controllers.routes.UsuarioController.formularioLogin());
   }

   public Result formularioLogin() {
      return ok(formLogin.render(formFactory.form(Login.class),""));
   }

   public Result loginUsuario() {
      Form<Login> form = formFactory.form(Login.class).bindFromRequest();
      if (form.hasErrors()) {
         return badRequest(formLogin.render(form, "Hay errores en el formulario"));
      }
      Login login = form.get();
      Usuario usuario = usuarioService.login(login.username, login.password);
      if (usuario == null) {
         return notFound(formLogin.render(form, "Login y contraseña no existentes"));
      } else {
         // Añadimos el id del usuario a la clave `connected` de
         // la sesión de Play
         // https://www.playframework.com/documentation/2.5.x/JavaSessionFlash
         session("connected", usuario.getId().toString());
         return redirect(controllers.routes.GestionTareasController.listaTareas(usuario.getId()));
      }
   }

   // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
   // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
   @Security.Authenticated(ActionAuthenticator.class)
   public Result logout() {
      String connectedUserStr = session("connected");
      session().remove("connected");
      return redirect(controllers.routes.UsuarioController.loginUsuario());
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result detalleUsuario(Long id) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != id) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Usuario usuario = usuarioService.findUsuarioPorId(id);
         if (usuario == null) {
            return notFound("Usuario no encontrado");
         } else {
            Logger.debug("Encontrado usuario " + usuario.getId() + ": " + usuario.getLogin());
            return ok(detalleUsuario.render(usuario));
         }
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioEditarPerfil(Long idUsuario){
     Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
     if(usuario == null) {
       return notFound("Usuario no encontrada");
     }else {
       String connectedUserStr = session("connected");
       Long connectedUser = Long.valueOf(connectedUserStr);
       if(connectedUser != usuario.getId()){
         return unauthorized("Lo siento, no estas autorizado");
       } else {
         String fechaStr = "";
         if (usuario.getFechaNacimiento() != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            fechaStr = formateador.format(usuario.getFechaNacimiento());
         }

        return ok(modificaPerfil.render(usuario.getId(), usuario.getLogin(), usuario.getEmail(),usuario.getNombre(),usuario.getApellidos(),fechaStr, ""));
       }
     }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result grabarUsuarioActualizado(Long idUsuario) throws java.text.ParseException{
     DynamicForm requestData = formFactory.form().bindFromRequest();
     String nEmail = requestData.get("email");
     String nNombre = requestData.get("nombre");
     String nApellidos = requestData.get("apellidos");
     String entrada = requestData.get("fecha");

     SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
     Date nFecha = formateador.parse(entrada);

     Usuario usuario = usuarioService.actualizarUsuario(idUsuario,nEmail,nNombre,nApellidos,nFecha);
     return redirect(controllers.routes.UsuarioController.detalleUsuario(idUsuario));
   }

}
