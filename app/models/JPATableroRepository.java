package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;

public class JPATableroRepository implements TableroRepository {
   JPAApi jpaApi;

   @Inject
   public JPATableroRepository(JPAApi api) {
      this.jpaApi = api;
   }

   public Tablero add(Tablero tablero) {
      return jpaApi.withTransaction(entityManager -> {
         entityManager.persist(tablero);
         entityManager.flush();
         entityManager.refresh(tablero);
         return tablero;
      });
   }

   public Tablero update(Tablero tablero) {
      return jpaApi.withTransaction(entityManager -> {
         Tablero actualizado = entityManager.merge(tablero);
         return actualizado;
      });
   }

   public Tablero findById(Long idTablero) {
      return jpaApi.withTransaction(entityManager -> {
         return entityManager.find(Tablero.class, idTablero);
      });
   }

   public List<Tablero> findAllTablerosNoParticipa(Long idUsuario){
     return jpaApi.withTransaction(entityManager -> {
        return  entityManager.createNativeQuery("SELECT t.* FROM Tablero t LEFT JOIN Persona_Tablero pt ON t.id=pt.tableros_id WHERE t.id NOT IN (Select pt.tableros_id from Persona_Tablero pt where pt.participantes_id = "+idUsuario+") OR pt.participantes_id IS NULL",Tablero.class).getResultList();
     });
   }

   /*public int addParticipanteTablero(Long idUsuario, Long idTablero){
     return jpaApi.withTransaction(entityManager -> {
       Query query = entityManager.createNativeQuery("INSERT INTO Persona_Tablero (tableros_id,participantes_id) values (1001,1001);");
        return query.executeUpdate();
     });
   }*/


}
