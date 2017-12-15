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
import models.Columna;
import models.ColumnaRepository;


public class TableroService {
  UsuarioRepository usuarioRepository;
  TableroRepository tableroRepository;
  ColumnaRepository columnaRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository,
                        TableroRepository tableroRepository,
                        ColumnaRepository columnaRepository){
    this.usuarioRepository = usuarioRepository;
    this.tableroRepository = tableroRepository;
    this.columnaRepository = columnaRepository;
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

  public Tablero anyadirColumna(Long idTablero,String nombreColumna) {
    
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
    this.columnaRepository.add(columna);

    // duevuelvo el tablero con la columna insertada
    return tablero;
  }




}
