package services;

import javax.inject.*;

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
}
