package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;
import java.util.List;

import javax.persistence.EntityManager;

public class JPANotificacionRepository implements NotificacionRepository {
    JPAApi jpaApi;

    @Inject
    public JPANotificacionRepository(JPAApi api){
        this.jpaApi = api;
    }

    public Notificacion create(Notificacion notificacion) {
        return jpaApi.withTransaction(entityManager -> {
            entityManager.persist(notificacion);
            entityManager.flush();
            entityManager.refresh(notificacion);
            return notificacion;
        });
    }

    public Notificacion findById(Long idNotificacion){
        return jpaApi.withTransaction(entityManager -> {
            return entityManager.find(Notificacion.class, idNotificacion);
        });
    }

	public void delete(Long idNotificacion) {
        jpaApi.withTransaction(() -> {
            EntityManager entityManager = jpaApi.em();
            Notificacion notificacionbd = entityManager.find(Notificacion.class,idNotificacion);
            entityManager.remove(notificacionbd);
        });
    }

	public List<Notificacion> findAllNoteByUser(Long idUsuario){
      return jpaApi.withTransaction(entityManager -> {
         return entityManager.createNativeQuery("SELECT n.* FROM Notificacion n WHERE n.usuarioId='"+idUsuario+"';" ,Notificacion.class).getResultList();
      });
    }

}
