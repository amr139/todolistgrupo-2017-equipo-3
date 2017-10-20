import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.Logger;

import java.sql.*;

import org.junit.*;
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
import models.Tarea;
import models.TareaRepository;
import models.JPATareaRepository;
import services.TareaService;
import services.TareaServiceException;

public class Practica2Test {
   static Database db;
   static private Injector injector;

   // Se ejecuta sólo una vez, al principio de todos los tests


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


   //Test1: testFindUsuarioPorIdReturnNULL
   @Test
   public void testFindUsuarioPorIdReturnNULL(){
     UsuarioRepository repository = newUsuarioRepository();
     Usuario user = repository.findById(2001L);
     assertNull(user);
   }

   //Test2: testborraTareaThrowsException
   @Test(expected = java.lang.RuntimeException.class)
   public void testborraTareaThrowsException(){
     TareaRepository repository = newTareaRepository();
     repository.delete(1234L);
   }

   //Test3: testEqualsFindUserByTareaId
   @Test
   public void testEqualsFindUserByTareaId(){
      TareaRepository repository = newTareaRepository();
      Usuario us1 = repository.findById(1000L).getUsuario();
      Usuario us2 = repository.findById(1001L).getUsuario();
      assertEquals(us1.getEmail(),us2.getEmail());
   }

   //Test #17 testTareaDistintaFindByID()
   @Test
   public void testTareaDistintaFindByID(){
     TareaRepository repository = newTareaRepository();
     Tarea t1 = repository.findById(1000L);
     Tarea t2 = repository.findById(2000L);
     assertNotEquals(t1,t2);

   }

 }
