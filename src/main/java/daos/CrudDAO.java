package daos;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, ID> {
  T crear(T entidad);

  Optional<T> buscarPorId(ID id); // Optional evita los NullPointerException

  List<T> listarTodos();

  boolean actualizar(T entidad);

  boolean eliminar(ID id);

}
