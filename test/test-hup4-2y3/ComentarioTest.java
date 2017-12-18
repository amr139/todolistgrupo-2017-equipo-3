import org.junit.*;
import static org.junit.Assert.*;

import models.Usuario;
import models.Tarea;
import models.Comentario;

public class ComentarioTest {

	@Test
	public void testCrearComentario(){
		Usuario usuario = new Usuario("pepe","pepe@gmail.com");
		Tarea tarea = new Tarea(usuario,"Tarea de test",null);
		Comentario comentario = new Comentario(tarea, usuario, "Es un comentario de prueba","none");
		assertEquals("pepe",comentario.getUsuario().getLogin());
		assertEquals("Tarea de test",comentario.getTarea().getTitulo());
		assertEquals("Es un comentario de prueba",comentario.getMensaje());
	}

}
