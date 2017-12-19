package models;

import com.google.inject.ImplementedBy;

@ImplementedBy(JPANotificacionRepository.class)
public interface NotificacionRepository {
  Notificacion create(Notificacion notificacion);
  Notificacion findById(Long idNotificacion);
}
