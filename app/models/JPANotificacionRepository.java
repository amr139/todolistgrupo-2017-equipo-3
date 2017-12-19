package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

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


}
