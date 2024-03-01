package com.example.controlador.dao;

import com.example.modulo.Alumno;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.mvc.http.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;


public class AlumnoJPADAO implements DAOAlumnosInterface<Alumno> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void crearTabla() {

    }

    @Override
    public boolean modificarTabla(long id,Alumno alumno) {

        Optional<Alumno> alumno1 = obtenerTodosAlumnos().stream().filter(a -> a.getId() == alumno.getId()).findAny();
        abrirEntityManager();
        if (alumno1.isPresent() && alumno.getId() != 0) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(alumno);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new RuntimeException(e);
            }
            cerrarEntityManager();
            return true;
        } else {
            cerrarEntityManager();
            return false;
        }
    }

    public List<Alumno> obtenerTodosAlumnos() {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a ", Alumno.class
        );
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public void anyadirLista(List lista) {
        abrirEntityManager();
        try {
            TypedQuery<Alumno> query = entityManager.createQuery("SELECT a FROM Alumno a", Alumno.class);
            for (Alumno alumno : query.getResultList()) {
                Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
            }
            List<Alumno> resultados = query.getResultList();

            // Agregar los resultados a la lista dada
            lista.addAll(resultados);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cerrarEntityManager();
    }

    @Override
    public boolean insertarenTabla(Alumno alumno) {
        Optional<Alumno> alumno1 = obtenerTodosAlumnos().stream().filter(a -> a.getId() == alumno.getId()).findAny();
        if (!alumno1.isPresent()) {
            abrirEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(alumno);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new RuntimeException(e);
            }
            cerrarEntityManager();
            return true;
        } else {
            cerrarEntityManager();
            return false;
        }
    }

    @Override
    public boolean eliminardeTabla(long id) {

        Optional<Alumno> alumno1 = obtenerTodosAlumnos().stream().filter(a -> a.getId() == id).findAny();
        if (alumno1.isPresent() && id != 0) {
            abrirEntityManager();
            try {
                Alumno persona = entityManager.find(Alumno.class, id);
                if (persona != null) {
                    entityManager.getTransaction().begin();
                    entityManager.remove(persona);
                    entityManager.getTransaction().commit();
                }
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw new RuntimeException(e);
            }
            cerrarEntityManager();
            return true;
        }else {
            cerrarEntityManager();
            return false;
        }
    }

    @Override
    public Alumno buscarTabla(Alumno alumno) {
        abrirEntityManager();
        try {
            Alumno persona = entityManager.find(Alumno.class, alumno.getId());
            entityManager.getTransaction().commit();
            cerrarEntityManager();
            return persona;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Alumno> coincidenciaExactaId(long id) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.id = :id", Alumno.class
        );
        query.setParameter("id", id);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }


    @Override
    public List<Alumno> coincidenciaExactaNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre = :nombre", Alumno.class
        );
        query.setParameter("nombre", nombreRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> contienePalabraClaveNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre", Alumno.class
        );
        query.setParameter("nombre", "%" + nombreRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> empiezaPorNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre", Alumno.class
        );
        query.setParameter("nombre", nombreRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> terminaEnNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre", Alumno.class
        );
        query.setParameter("nombre", "%" + nombreRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> coincidenciaExactaDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI = :dni", Alumno.class
        );
        query.setParameter("dni", dniRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> contienePalabraClaveDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", "%" + dniRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> empiezaPorDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", dniRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;

    }

    @Override
    public List<Alumno> terminaEnDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", "%" + dniRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }


    public void cerrarEntityManager() {
        entityManager.close();
    }

    public void abrirEntityManager() {
        entityManager = ControladorDAOJPA.getEntityManagerFactory().createEntityManager();
    }
}

