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

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.text.ParseException;

import models.Usuario;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;

import models.Notificacion;
import models.NotificacionRepository;
import models.JPANotificacionRepository;
import services.NotificacionService;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;


public class NotificacionServiceTest {
   static private Injector injector;

   	@BeforeClass
   	static public void initApplication() {
       	GuiceApplicationBuilder guiceApplicationBuilder = new GuiceApplicationBuilder().in(Environment.simple());
       	injector = guiceApplicationBuilder.injector();
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

   	private NotificacionService newNotificacionService() {
     	return injector.instanceOf(NotificacionService.class);
   	}

	@Test
    public void TESTfindAllNoteByUserId() {
       NotificacionService notificacionService = newNotificacionService();
       List<Notificacion> lista = notificacionService.findAllNoteByUserId(1000L);

       assertEquals(lista.size(), 1);
       lista = notificacionService.findAllNoteByUserId(1001L);
	   assertEquals(lista.size(), 0);
    }

	@Test
	public void TESTcrearNotificacion() {
	   NotificacionService notificacionService = newNotificacionService();
	   Notificacion note = notificacionService.crearNotificacion(1000L,"Notificacion ejemplo");
	   assertNotNull(note);
	}

	@Test
	public void TESTreadNotificacion() {
	   NotificacionService notificacionService = newNotificacionService();
	   Notificacion note = notificacionService.marcarLeidaNotificacion(101L);
	   assertEquals(note.getEstado(),true);
	}

	@Test
	public void TESTgetNotificacion() {
	   NotificacionService notificacionService = newNotificacionService();
	   Notificacion note = notificacionService.obtenerNotificacion(101L);
	   assertNotNull(note);
	}

	@Test
	public void TESTdeleteNotificacion() {
	   NotificacionService notificacionService = newNotificacionService();
	   notificacionService.borrarNotificacion(101L);
	   Notificacion note = notificacionService.obtenerNotificacion(101L);
	   assertNull(note);
	}


}
