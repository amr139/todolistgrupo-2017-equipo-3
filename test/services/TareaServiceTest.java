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

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;

public class TareaServiceTest {
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

   private TareaService newTareaService() {
     return injector.instanceOf(TareaService.class);
   }

   // Test #19: allTareasUsuarioEstanOrdenadas
   @Test
   public void allTareasUsuarioEstanOrdenadas() {
      TareaService tareaService = newTareaService();
      List<Tarea> tareas = tareaService.allTareasUsuario(1000L);
      assertEquals("Renovar DNI", tareas.get(0).getTitulo());
      assertEquals("Práctica 1 MADS", tareas.get(1).getTitulo());
   }

   // Test #20: exceptionSiUsuarioNoExisteRecuperandoSusTareas
   @Test(expected = TareaServiceException.class)
   public void crearNuevoUsuarioLoginRepetidoLanzaExcepcion(){
      TareaService tareaService = newTareaService();
      List<Tarea> tareas = tareaService.allTareasUsuario(1005L);
   }

   //Test #21: nuevaTareaUsuario
   @Test
   public void nuevaTareaUsuario(){
     TareaService tareaService = newTareaService();
     long idUsuario = 1000L;
     tareaService.nuevaTarea(idUsuario,"Pagar el alquiler", null);
     assertEquals(3, tareaService.allTareasUsuario(1000L).size());
   }

   //Test #22: modificacion de tareas
   @Test
   public void modificacionTarea() throws ParseException{
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
     tareaService.modificaTarea(idTarea,"Pagar el alquiler", sdf.parse("26-05-2018"));
     Tarea tarea = tareaService.obtenerTarea(idTarea);
     assertEquals("Pagar el alquiler",tarea.getTitulo());
     assertTrue(tarea.getFechaLimite().compareTo(sdf.parse("26-05-2018")) == 0);
   }

   //Test #23: borrado tareaBD
   @Test
   public void borradoTarea() {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     tareaService.borraTarea(idTarea);
     assertNull(tareaService.obtenerTarea(idTarea));
   }

   @Test
   public void marcarTareaComoTerminada() {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     Tarea res = tareaService.marcarTareaComoTerminada(idTarea);
     assertTrue(res.getTerminado());
   }


}
