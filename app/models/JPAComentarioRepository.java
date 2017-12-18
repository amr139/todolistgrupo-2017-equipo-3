package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;
import java.util.List;

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

    public Comentario update(Comentario comentario) {
        return jpaApi.withTransaction(entityManager -> {
            Comentario comentariobd = entityManager.find(Comentario.class,comentario.getId());
            comentariobd.setTarea(comentario.getTarea());
            comentariobd.setUsuario(comentario.getUsuario());
            comentariobd.setMensaje(comentario.getMensaje());
            comentariobd.setURL(comentario.getURL());
            return comentariobd;
        });
    }

    public void delete(Long comentarioId) {
        jpaApi.withTransaction(() -> {
            EntityManager entityManager = jpaApi.em();
            Comentario comentariobd = entityManager.find(Comentario.class,comentarioId);
            entityManager.remove(comentariobd);
        });
    }


    public List<Comentario> findAllComentsByTarea(Long idTarea){
      return jpaApi.withTransaction(entityManager -> {
         return entityManager.createNativeQuery("SELECT c.* FROM Comentario c WHERE c.tareaId='"+idTarea+"';" ,Comentario.class).getResultList();
      });
    }
}
