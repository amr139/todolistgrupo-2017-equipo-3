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
   static JPAApi jpaApi;

   // Se ejecuta sólo una vez, al principio de todos los tests
   @BeforeClass
   static public void initDatabase() {
      // Inicializamos la BD en memoria y su nombre JNDI
      db = Databases.inMemoryWith("jndiName", "DBTest");
      db.getConnection();
      // Se activa la compatibilidad MySQL en la BD H2
      db.withConnection(connection -> {
         connection.createStatement().execute("SET MODE MySQL;");
      });
      // Activamos en JPA la unidad de persistencia "memoryPersistenceUnit"
      // declarada en META-INF/persistence.xml y obtenemos el objeto
      // JPAApi
      jpaApi = JPA.createFor("memoryPersistenceUnit");
   }


   // Se ejecuta al antes de cada test
   // Se insertan los datos de prueba en la tabla Usuarios de
   // la BD "DBTest". La BD ya contiene una tabla de usuarios
   // porque la ha creado JPA al tener la propiedad
   // hibernate.hbm2ddl.auto (en META-INF/persistence.xml) el valor update
   // Los datos de prueba se definen en el fichero
   // test/resources/usuarios_dataset.xml
   @Before
   public void initData() throws Exception {
      JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
      IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new
      FileInputStream("test/resources/usuarios_dataset.xml"));
      databaseTester.setDataSet(initialDataSet);
      // Definimos como operación SetUp CLEAN_INSERT, que hace un
      // DELETE_ALL de todas las tablase del dataset, seguido por un
      // INSERT. (http://dbunit.sourceforge.net/components.html)
      // Es lo que hace DbUnit por defecto, pero así queda más claro.
      databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
      databaseTester.onSetup();
   }

   //Test1: testFindUsuarioPorIdReturnNULL
   @Test
   public void testFindUsuarioPorIdReturnNULL(){
     UsuarioRepository repository = new JPAUsuarioRepository(jpaApi);
     Usuario user = repository.findById(2001L);
     assertNull(user);
   }

   //Test2: testborraTareaThrowsException
   @Test(expected = java.lang.RuntimeException.class)
   public void testborraTareaThrowsException(){
     TareaRepository repository = new JPATareaRepository(jpaApi);
     repository.delete(1234L);
   }

   //Test3: testEqualsFindUserByTareaId
   @Test
   public void testEqualsFindUserByTareaId(){
      TareaRepository repository = new JPATareaRepository(jpaApi);
      Usuario us1 = repository.findById(1000L).getUsuario();
      Usuario us2 = repository.findById(1001L).getUsuario();
      assertEquals(us1.getEmail(),us2.getEmail());
   }

   //Test #17 testTareaDistintaFindByID()
   @Test
   public void testTareaDistintaFindByID(){
     TareaRepository repository = new JPATareaRepository(jpaApi);
     Tarea t1 = repository.findById(1000L);
     Tarea t2 = repository.findById(2000L);
     assertNotEquals(t1,t2);

   }

 }
