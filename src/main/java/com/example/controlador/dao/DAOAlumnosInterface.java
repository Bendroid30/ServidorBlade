package com.example.controlador.dao;

import java.util.List;

public interface DAOAlumnosInterface<T> {
    void crearTabla();
    void anyadirLista(List<T> lista);
    boolean modificarTabla(long id,T alumno);
    boolean insertarenTabla(T alumno);
    boolean eliminardeTabla(long id);
    T buscarTabla(T alumno);

    List<T> coincidenciaExactaId(long id);

    List<T> coincidenciaExactaNombre(String nombreRecibido);
    List<T> contienePalabraClaveNombre(String nombreRecibido);
    List<T> empiezaPorNombre(String nombreRecibido);
    List<T> terminaEnNombre(String nombreRecibido);
    List<T> coincidenciaExactaDni(String dniRecibido);
    List<T> contienePalabraClaveDni(String dniRecibido);
    List<T> empiezaPorDni(String dniRecibido);
    List<T> terminaEnDni(String dniRecibido);
//    List<T> notaMediaAlum(double mediaRecibida);
//    List<T> profesorTutorAlum(String nombreTutor);

}
