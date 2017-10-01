package models;

import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(JPATareaRepository.class)
public interface TareaRepository {
  Tarea add(Tarea tarea);
  Tarea update(Tarea tarea);
  Tarea findById(Long idTarea);
  List<Tarea> findAllTareas(Long idUsuario);
}
