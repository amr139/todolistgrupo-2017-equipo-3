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
import models.Tarea;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.TareaRepository;
import models.JPATareaRepository;

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

   // Test #45 test creaColumna
   @Test
   public void testCrearColumna() {
      Columna columna = new Columna(tablero, "Primera Columna");
      
   }

}