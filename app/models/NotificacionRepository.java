package models;

import com.google.inject.ImplementedBy;
import java.util.List;

@ImplementedBy(JPANotificacionRepository.class)
public interface NotificacionRepository {
  Notificacion create(Notificacion notificacion);
  Notificacion findById(Long idNotificacion);
  void delete(Long idNotificacion);
  Notificacion readNote(Long idNote);
  List<Notificacion> findAllNoteByUser(Long idUser);

}
