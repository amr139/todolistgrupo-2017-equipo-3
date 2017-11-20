package services;

import javax.inject.*;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;


public class TareaService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;

  @Inject
  public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository){
    this.usuarioRepository = usuarioRepository;
    this.tareaRepository = tareaRepository;
  }

  //Devuelve la lista de tareas de un usuario ordenadas por su id
  // (equivalente al orden de creacion)
  public List<Tarea> allTareasUsuario(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario==null){
      throw new TareaServiceException("Usuario no existente");
    }
    List<Tarea> tareas = new ArrayList<Tarea>();
    tareas.addAll(usuario.getTareas());
    Collections.sort(tareas, (a,b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tareas;
  }

  public Tarea nuevaTarea(Long idUsuario,String titulo, Date fLimite){
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if(usuario == null){
      throw new TareaServiceException("Usuario no existente");
    }
    Tarea tarea = new Tarea(usuario, titulo, fLimite);
    return tareaRepository.add(tarea);
  }

  public Tarea obtenerTarea(Long idTarea){
    return tareaRepository.findById(idTarea);
  }

  public Tarea modificaTarea(Long idTarea, String nuevoTitulo, Date nuevaFechaLimite){
    Tarea tarea = tareaRepository.findById(idTarea);
    if(tarea == null){
      throw new TareaServiceException("No existe tarea");
    }
    tarea.setTitulo(nuevoTitulo);
    tarea.setFechaLimite(nuevaFechaLimite);
    tarea = tareaRepository.update(tarea);
    return tarea;
  }

  public void borraTarea(Long idTarea){
    Tarea tarea = tareaRepository.findById(idTarea);
    if (tarea == null )
      throw new TareaServiceException("No existe tarea");
    tareaRepository.delete(idTarea);
  }

  public Tarea marcarTareaComoTerminada(Long idTarea) {
    Tarea tarea = tareaRepository.findById(idTarea);
    if (tarea == null) {
      throw new TareaServiceException("No existe tarea");
    }
    tarea.setTerminado(true);
    System.out.println();
    System.out.println("Esoty en el servicio de Tarea "+tarea.getTerminado());
    System.out.println();
    tarea = tareaRepository.update(tarea);
    System.out.println();
    System.out.println("Esoty en el servicio de Tarea "+tarea.getTerminado());
    System.out.println();
    return tarea;
  }


}
