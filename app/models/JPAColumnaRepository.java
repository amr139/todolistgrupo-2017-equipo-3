package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;

public class JPAColumnaRepository implements ColumnaRepository {
   JPAApi jpaApi;

   @Inject
   public JPAColumnaRepository(JPAApi api) 
   {
      this.jpaApi = api;
   }

   public Columna add(Columna columna) 
   {
        return jpaApi.withTransaction(entityManager -> {
        entityManager.persist(columna);
        entityManager.flush();
        entityManager.refresh(columna);
        return columna;
        });
    }
    public Columna findById(Long id) {
        return jpaApi.withTransaction(entityManager -> {
            return entityManager.find(Columna.class, id);
         });
    }
    public Columna update(Columna columna) {
        return jpaApi.withTransaction(entityManager -> {
            Columna columnaBD = entityManager.find(Columna.class,columna.getId());
            columnaBD.setNombre(columna.getNombre());
            columnaBD.setTablero(columna.getTablero());
            columnaBD.setTareas(columna.getTareas());
            return columnaBD;
        });
    }
    public void delete(Columna columna) {
        jpaApi.withTransaction(() -> {
            EntityManager entityManager = jpaApi.em();
            Columna columnaBD = entityManager.find(Columna.class,columna.getId());
            entityManager.remove(columnaBD);
        });
    }
}