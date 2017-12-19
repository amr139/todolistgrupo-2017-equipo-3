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

import models.Usuario;

import models.UsuarioRepository;
import models.JPAUsuarioRepository;

import models.Notificacion;
import models.NotificacionRepository;
import models.JPANotificacionRepository;

public class NotificacionTest {
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

	private NotificacionRepository newNotificacionRepository() {
		return injector.instanceOf(NotificacionRepository.class);
	}

	@Test
	public void testCrearNotificacion(){
		Usuario usuario = new Usuario("pepe","pepe@gmail.com");
		Notificacion notificacion = new Notificacion(usuario, "Es una notificacion de prueba");
		assertEquals("pepe",notificacion.getUsuario().getLogin());
		assertEquals("Es una notificacion de prueba",notificacion.getMensaje());
	}

	@Test
	public void JPAaddNotificacion(){
		assertNotNull(injector);
		UsuarioRepository usuarioRepository = newUsuarioRepository();
        NotificacionRepository notificacionRepository =newNotificacionRepository();

        Usuario usuario = new Usuario("juangutierrez","juangutierrez@gmail.com");
        usuario = usuarioRepository.add(usuario);

        Notificacion notificacion = new Notificacion(usuario, "Es un notificacion de prueba");
		notificacion = notificacionRepository.create(notificacion);

		assertNotNull(notificacion.getId());
		assertEquals(notificacion.getMensaje(),"Es un notificacion de prueba");
	}

	@Test
	public void JPAfindNotificacion(){
		NotificacionRepository notificacionRepository =newNotificacionRepository();
		Notificacion notificacion = notificacionRepository.findById(101L);
		assertNotNull(notificacion);
		assertEquals(notificacion.getUsuario().getLogin(),"juangutierrez");
	}

}
