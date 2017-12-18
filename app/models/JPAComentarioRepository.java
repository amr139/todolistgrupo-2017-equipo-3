package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;

public class JPAComentarioRepository implements ComentarioRepository {
  JPAApi jpaApi;

  @Inject
  public JPAComentarioRepository(JPAApi api){
    this.jpaApi = api;
  }

  public Comentario create(Comentario comentario) {
    return jpaApi.withTransaction(entityManager -> {
  	entityManager.persist(comentario);
  	entityManager.flush();
  	entityManager.refresh(comentario);
  	return comentario;
    });
  }

  public Comentario findById(Long idComentario){
	return jpaApi.withTransaction(entityManager -> {
	  return entityManager.find(Comentario.class, idComentario);
	});
  }


}
