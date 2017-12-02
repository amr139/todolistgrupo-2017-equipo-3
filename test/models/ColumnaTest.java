import org.junit.*;
import static org.junit.Assert.*;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.Logger;

import java.sql.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.text.ParseException;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import models.Usuario;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;

import models.Tarea;
import models.TareaRepository;
import models.JPATareaRepository;

import models.Tablero;
import models.TableroRepository;
import models.JPATableroRepository;

import models.Columna;
import models.ColumnaRepository;
import models.JPAColumnaRepository;

public class ColumnaTest {
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

  private ColumnaRepository newColumnaRepository() {
      return injector.instanceOf(ColumnaRepository.class);
  }
  private TableroRepository newTableroRepository()
  {
      return injector.instanceOf(TableroRepository.class);
  }
  private UsuarioRepository newUsuarioRepository() 
  {
      return injector.instanceOf(UsuarioRepository.class);
  }

   // Test #45 test creaColumna
   @Test
   public void testCrearColumna() {
      Usuario usuario = new Usuario("vk", "juangutierrez@gmail.com");
      Tablero tablero = new Tablero(usuario,"Tablero test");
      Columna columna = new Columna(tablero, "Primera Columna");
      assertEquals("Primera Columna",columna.getNombre());
      assertEquals("Tablero test", columna.getTablero().getNombre());
   }

   //Test #46: testAddColumnaJPARepositoryInsertsColumnaDatabase
   @Test
   public void testAddColumnaJPARepositoryInsertsColumnaDatabase(){
     assertNotNull(injector);
     UsuarioRepository usuarioRepository = newUsuarioRepository();
     TableroRepository tableroRepository = newTableroRepository();
     ColumnaRepository columnaRepository = newColumnaRepository();

     Usuario usuario = new Usuario("juangutierrez","juangutierrez@gmail.com");
     usuario = usuarioRepository.add(usuario);
     Tablero tablero = new Tablero(usuario,"Mi tablero favorito");
     tablero = tableroRepository.add(tablero);
     Columna columna = new Columna(tablero,"Pendiente");
     columna = columnaRepository.add(columna);

     assertNotNull(columna.getId());
     assertEquals("Pendiente",columna.getNombre());
     assertEquals("Mi tablero favorito",columna.getTablero().getNombre());
   }

    //Test #47 testFindColumnaById()
    @Test
    public void testFindColumnaById(){
      ColumnaRepository repository = newColumnaRepository();
      Columna columna = repository.findById(1003L);
      assertEquals("TODO",columna.getNombre());
    }

    //Test #48 testUpadateColumnaJPARepository
    @Test
    public void testUpadateColumnaJPARepository() {
      ColumnaRepository repository = newColumnaRepository();
      Columna columna = repository.findById(1003L);
      columna.setNombre("TODO-updated");
      columna = repository.update(columna);
      assertEquals("TODO-updated",columna.getNombre());
    }

}