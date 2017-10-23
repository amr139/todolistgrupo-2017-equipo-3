package services;

import javax.inject.*;

import java.util.List;
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

  public Tablero nuevoTablero(Long idUsuario,String titulo){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario == null){
      throw new TareaServiceException("Usuario no existente");
    }
    Tablero tablero = new Tablero(usuario, titulo);
    return tableroRepository.add(tablero);
  }

  public List<Tablero> allTablerosAdministradosUser(long idUsuario){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TareaServiceException("Usuario no existente");
    }
    List<Tablero> tablerosAdmin = new ArrayList<Tablero>();
    tablerosAdmin.addAll(usuario.getAdministrados());
    Collections.sort(tablerosAdmin, (a,b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tablerosAdmin;
  }

}
