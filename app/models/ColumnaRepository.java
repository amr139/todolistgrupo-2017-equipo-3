package models;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPAColumnaRepository.class)
public interface ColumnaRepository {
   public Columna add(Columna columna);
   public Columna findById(Long id);
   public Columna update(Columna columna);
   public void delete(Columna columna);
}