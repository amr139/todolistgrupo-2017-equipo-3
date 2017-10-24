import org.junit.*;
import static org.junit.Assert.*;

import play.db.jpa.*;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.List;

import models.Tablero;
import models.TableroRepository;
import services.TableroService;

public class TableroServiceTest {
  static private Injector injector;

  @BeforeClass
  static public void initApplication() {
      GuiceApplicationBuilder guiceApplicationBuilder =
         new GuiceApplicationBuilder().in(Environment.simple());
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

  private TableroService newTableroService() {
    return injector.instanceOf(TableroService.class);
  }

  //Test 1
  @Test
  public void crearNuevoTableroCorrectoTest(){
     TableroService tableroService = newTableroService();
     long idUsuario = 1000L;
     Tablero tablero = tableroService.nuevoTablero(idUsuario, "Tablero de prueba");
     assertNotNull(tablero);
     assertEquals("Tablero de prueba", tablero.getNombre());
     assertEquals("juan.gutierrez@gmail.com", tablero.getAdministrador().getEmail());
   }

   //Test 2
   @Test
   public void obtenerListadoTablerosAdministradosByUserTest(){
      TableroService tableroService = newTableroService();
      long idUsuario = 1000L;
      List<Tablero> tablerosAdministrados = tableroService.allTablerosAdministradosUser(idUsuario);
      assertEquals(tablerosAdministrados.size(),2);
    }

    //Test 3
    @Test
    public void obtenerListadoTablerosParticipaByUserTest(){
       TableroService tableroService = newTableroService();
       long idUsuario = 1000L;
       List<Tablero> tablerosParticipados = tableroService.allTablerosParticipadosUser(idUsuario);
       assertEquals(tablerosParticipados.size(),0);
       idUsuario = 1001L;
       tablerosParticipados = tableroService.allTablerosParticipadosUser(idUsuario);
       assertEquals(tablerosParticipados.size(),1);
     }

     //Test 4
     @Test
     public void obtenerListadoTablerosNoApuntadosTest(){
        TableroService tableroService = newTableroService();
        long idUsuario = 1001L;
        List<Tablero> tablerosNoInside = tableroService.allTablerosNoApuntadosUser(idUsuario);
        assertEquals(tablerosNoInside.size(),1);
      }

      //Test 5
      @Test
      public void añadirParticipanteTest(){
         TableroService tableroService = newTableroService();
         Long idTablero = 1001L;
         Long idUsuario = 1001L;
         Tablero tablero = tableroService.añadirParticipante(idUsuario, idTablero);
         assertEquals(tablero.getParticipantes().size(),1);
       }

}
