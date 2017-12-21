package services;

import javax.inject.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;
import models.Tarea;
import models.TareaRepository;
import models.Columna;
import models.ColumnaRepository;


public class TableroService {
  UsuarioRepository usuarioRepository;
  TableroRepository tableroRepository;
  ColumnaRepository columnaRepository;
  TareaRepository tareaRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository,
                        TableroRepository tableroRepository,
                        ColumnaRepository columnaRepository,
                        TareaRepository tareaRepository){
    this.usuarioRepository = usuarioRepository;
    this.tableroRepository = tableroRepository;
    this.columnaRepository = columnaRepository;
    this.tareaRepository   = tareaRepository;
  }

  public Tablero encontrarTablero(Long idTablero){
    return tableroRepository.findById(idTablero);
  }

  public Tablero nuevoTablero(Long idUsuario,String titulo){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario == null){
      throw new TableroServiceException("Usuario no existente");
    }
    Tablero tablero = new Tablero(usuario, titulo);
    Tablero tableroBD = tableroRepository.add(tablero);
    // Devuelvo el tablero guardado en la BD
    return tableroBD;
  }

  public List<Tablero> allTablerosAdministradosUser(long idUsuario){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tablerosAdmin = new ArrayList<Tablero>();
    tablerosAdmin.addAll(usuario.getAdministrados());
    Collections.sort(tablerosAdmin, (a,b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tablerosAdmin;
  }

  public List<Tablero> allTablerosParticipadosUser(long idUsuario){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tablerosParticipados = new ArrayList<Tablero>();
    tablerosParticipados.addAll(usuario.getTableros());
    Collections.sort(tablerosParticipados, (a,b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tablerosParticipados;
  }

  public List<Tablero> allTablerosNoApuntadosUser(Long idUsuario){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tablerosNoParticipados = new ArrayList<Tablero>();
    tablerosNoParticipados.addAll(tableroRepository.findAllTablerosNoParticipa(idUsuario));
    List<Tablero> listaFinal = new ArrayList<Tablero>();
    for(Tablero tablerotemp : tablerosNoParticipados){
      if(!tablerotemp.getAdministrador().getId().equals(idUsuario)){
        listaFinal.add(tablerotemp);
      }
    }
    Collections.sort(listaFinal, (a,b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return listaFinal;
  }

  public Tablero añadirParticipante(Long idUsuario, Long idTablero){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TableroServiceException("Usuario no existente");
    }
    Tablero tablero = tableroRepository.findById(idTablero);
    if(tablero==null){
      throw new TableroServiceException("Tablero no existente");
    }

    List<Usuario> listaParticipantes = new ArrayList<Usuario>();
    listaParticipantes.addAll(tablero.getParticipantes());

    if(listaParticipantes.contains(usuario)){
      throw new TableroServiceException("Usuario ya participa");
    }
    listaParticipantes.add(usuario);
    Set<Usuario> foo = new HashSet<Usuario>(listaParticipantes);
    tablero.setParticipantes(foo);
    tablero = tableroRepository.update(tablero);
    return tablero;

  }

  public Columna anyadirColumna(Long idTablero,String nombreColumna) {
    
    Tablero tablero = tableroRepository.findById(idTablero);

    Columna columna = new Columna(tablero,nombreColumna);
    
    List<Columna> listaColumnas = new ArrayList<Columna>();
    
    listaColumnas.addAll(tablero.getColumnas());

    if(listaColumnas.contains(columna)) {
      throw new TableroServiceException("Esta columna ya esta en el tablero");
    }
    listaColumnas.add(columna);
    Set<Columna> foo = new HashSet<Columna>(listaColumnas);
    
    tablero.setColumnas(foo);

    // guardo la columna en la BD
    return this.columnaRepository.add(columna);
  }

  public void anyadirTareaColumna(Long idColumna,Long idTarea) {
      Columna columna = columnaRepository.findById(idColumna);
      Tarea tarea = this.tareaRepository.findById(idTarea);

      List<Tarea> listaTareas = new ArrayList<Tarea>();
      listaTareas.addAll(columna.getTareas());
      if(listaTareas.contains(tarea)) {
        throw new TableroServiceException("Esta tarea ya esta en la columna");
      }
      tarea.setColumna(columna);
      this.tareaRepository.update(tarea);
  }




}
