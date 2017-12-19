import org.junit.*;
import static org.junit.Assert.*;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import java.util.List;

import models.Usuario;
import models.Tarea;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.TareaRepository;
import models.JPATareaRepository;
import models.Comentario;
import models.ComentarioRepository;
import models.JPAComentarioRepository;

public class ComentarioTest {
	static Database db;
	static private Injector injector;


	//Se ejecuta solo una vez, al principio de todos los testCrearTarea
	@BeforeClass
	static public void initApplication() {
	  GuiceApplicationBuilder guiceApplicationBuilder =
		new GuiceApplicationBuilder().in(Environment.simple());
	  injector = guiceApplicationBuilder.injector();
	  db = injector.instanceOf(Database.class);
	  // Necesario para inicializar JPA
	  injector.instanceOf(JPAApi.class);
	 }

	@Before
	public void initData() throws Exception {
	  JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
	  IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/usuarios_dataset.xml"));
	  databaseTester.setDataSet(initialDataSet);
	  databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
	  databaseTester.onSetup();
	}

	private UsuarioRepository newUsuarioRepository() {
	   return injector.instanceOf(UsuarioRepository.class);
	 }

	private TareaRepository newTareaRepository() {
		return injector.instanceOf(TareaRepository.class);
  	}

	private ComentarioRepository newComentarioRepository() {
	   return injector.instanceOf(ComentarioRepository.class);
	}

	@Test
	public void testCrearComentario(){
		Usuario usuario = new Usuario("pepe","pepe@gmail.com");
		Tarea tarea = new Tarea(usuario,"Tarea de test",null);
		Comentario comentario = new Comentario(tarea, usuario, "Es un comentario de prueba","none");
		assertEquals("pepe",comentario.getUsuario().getLogin());
		assertEquals("Tarea de test",comentario.getTarea().getTitulo());
		assertEquals("Es un comentario de prueba",comentario.getMensaje());
	}

	@Test
	public void JPAaddComentario(){
		assertNotNull(injector);
		UsuarioRepository usuarioRepository = newUsuarioRepository();
        TareaRepository tareaRepository = newTareaRepository();
		ComentarioRepository comentarioRepository =newComentarioRepository();

        Usuario usuario = new Usuario("juangutierrez","juangutierrez@gmail.com");
        usuario = usuarioRepository.add(usuario);

        Tarea tarea = new Tarea(usuario,"Renovar DNI", null);
        tarea = tareaRepository.add(tarea);

        Comentario comentario = new Comentario(tarea, usuario, "Es un comentario de prueba","none");
		comentario = comentarioRepository.create(comentario);
		assertNotNull(comentario.getId());
		assertEquals(comentario.getMensaje(),"Es un comentario de prueba");
	}

	@Test
	public void JPAfindComentario(){
		ComentarioRepository comentarioRepository =newComentarioRepository();
		Comentario comentario = comentarioRepository.findById(101L);
		assertNotNull(comentario);
		assertEquals(comentario.getUsuario().getLogin(),"juangutierrez");
	}

	@Test
	public void JPAupdateComentario(){
		ComentarioRepository comentarioRepository =newComentarioRepository();
		Comentario comentario = comentarioRepository.findById(101L);
		assertNotNull(comentario);
		assertEquals(comentario.getUsuario().getLogin(),"juangutierrez");
		comentario.setMensaje("Mensaje updated");
		comentario = comentarioRepository.update(comentario);
		assertEquals(comentario.getMensaje(),"Mensaje updated");

	}

	@Test
	public void JPAdeleteComentario(){
		ComentarioRepository comentarioRepository =newComentarioRepository();
		comentarioRepository.delete(101L);
		Comentario comentario = comentarioRepository.findById(101L);
		assertNull(comentario);
	}

	@Test
	public void JPAfindComentariosByTareaID(){
		ComentarioRepository comentarioRepository =newComentarioRepository();
		List<Comentario> lista = comentarioRepository.findAllComentsByTarea(1000L);
		assertEquals(lista.size(),1);

	}

}
