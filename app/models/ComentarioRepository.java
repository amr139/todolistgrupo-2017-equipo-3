package models;

import com.google.inject.ImplementedBy;
import java.util.List;


@ImplementedBy(JPAComentarioRepository.class)
public interface ComentarioRepository {
  Comentario create(Comentario comentario);
  Comentario findById(Long idComentario);
  Comentario update(Comentario comentario);
  void delete(Long idComentario);
  List<Comentario> findAllComentsByTarea(Long iTarea);
}
