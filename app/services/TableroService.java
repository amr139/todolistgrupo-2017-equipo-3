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


public class TableroService {
  UsuarioRepository usuarioRepository;
  TableroRepository tableroRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository, TableroRepository tableroRepository){
    this.usuarioRepository = usuarioRepository;
    this.tableroRepository = tableroRepository;
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
    return tableroRepository.add(tablero);
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

  public List<Tablero> allTablerosNoApuntadosUser(long idUsuario){
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




}
