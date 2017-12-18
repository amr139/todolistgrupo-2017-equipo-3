package models;

import com.google.inject.ImplementedBy;

@ImplementedBy(JPAComentarioRepository.class)
public interface ComentarioRepository {
  Comentario create(Comentario comentario);
  Comentario findById(Long idComentario);
  //Comentario update(Comentario comentario);
  //void delete(Long idComentario);
}
