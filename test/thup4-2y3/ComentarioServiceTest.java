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

import models.Tarea;
import models.TareaRepository;
import models.JPATareaRepository;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import services.ComentarioService;
import models.Comentario;

public class ComentarioServiceTest {
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

   	private ComentarioService newComentarioService() {
     	return injector.instanceOf(ComentarioService.class);
   	}

	@Test
    public void TESTfindAllComentsByTareaId() {
       ComentarioService comentarioService = newComentarioService();
       List<Comentario> lista = comentarioService.findAllComentsByTareaId(1000L);

       assertEquals(lista.size(), 1);
       lista = comentarioService.findAllComentsByTareaId(1001L);
	   assertEquals(lista.size(), 0);
    }

	@Test
    public void TESTcrearComentario() {
       ComentarioService comentarioService = newComentarioService();
	   Comentario coment = comentarioService.crearComentario(1000L,1000L,"Mensaje de prueba2","none");
	   assertNotNull(coment);
    }

	@Test
	public void TESTobtenerComentario(){
		ComentarioService comentarioService = newComentarioService();
		Comentario coment = comentarioService.obtenerComentario(101L);
		assertNotNull(coment);
	}

	@Test
	public void TESTmodificaComentario(){
		ComentarioService comentarioService = newComentarioService();
		Comentario coment = comentarioService.obtenerComentario(101L);
		assertNotNull(coment);
		coment = comentarioService.modificaComentario(coment.getId(),"Nuevo contenido","none");
		assertNotNull(coment);
		assertEquals(coment.getMensaje(),"Nuevo contenido");
	}


}
