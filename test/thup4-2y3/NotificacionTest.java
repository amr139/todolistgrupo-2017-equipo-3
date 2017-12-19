import org.junit.*;
import static org.junit.Assert.*;

import models.Usuario;
import models.Tarea;
import models.Notificacion;

public class NotificacionTest {

	@Test
	public void testCrearNotificacion(){
		Usuario usuario = new Usuario("pepe","pepe@gmail.com");
		Notificacion notificacion = new Notificacion(usuario, "Es una notificacion de prueba");
		assertEquals("pepe",notificacion.getUsuario().getLogin());
		assertEquals("Es una notificacion de prueba",notificacion.getMensaje());
	}

}
