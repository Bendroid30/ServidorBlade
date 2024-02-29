package com.example.controlador;

import com.example.controlador.dao.AlumnoJPADAO;
import com.example.modulo.Alumno;
import com.example.modulo.AlumnoExisteException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Path
public class ControladorAlumnos implements ILista<Alumno> {
    private List<Alumno> listaAlumnos = new ArrayList<Alumno>();

    //    private DAOSQLAlumnos DAOSQLAlumnos =new DAOSQLAlumnos();
    private AlumnoJPADAO alumnoJPADAO = new AlumnoJPADAO();

    public void crearTabla() {
        alumnoJPADAO.crearTabla();
    }


    @Override
    public void agregar(Alumno alumno) throws AlumnoExisteException {
        boolean copia = false;
        for (Alumno alumnoEscogido : listaAlumnos) {
            if (alumnoEscogido.getDNI().equalsIgnoreCase(alumno.getDNI())) {
                copia = true;
                break;
            }
        }
        if (!copia) {
//            DAOSQLAlumnos.insertarenTabla(alumno);
//            for (Double nota : alumno.getListaNotas()) {
//                DAOSQLAlumnos.insertNota(alumno,nota);
//            }
//            listaAlumnos.clear();
//            DAOSQLAlumnos.anyadirLista(listaAlumnos);
            alumnoJPADAO.insertarenTabla(alumno);
            listaAlumnos.clear();
            alumnoJPADAO.anyadirLista(listaAlumnos);


        } else {
            //Lanzar excepción AlumnoExiste
            throw new AlumnoExisteException("EL ALUMNO YA SE ENCUENTRA EN LA LISTA");
        }

    }

    @Override
    public Alumno buscar(Alumno alumno) {
        return alumnoJPADAO.buscarTabla(alumno);

    }

    @Override
    public void eliminar(long id) {
        boolean copiaNoExiste = false;
        for (Alumno alumnoEscogido : listaAlumnos) {
            if (alumnoEscogido.getId() == id) {
                alumnoJPADAO.eliminardeTabla(id);
                listaAlumnos.remove(alumnoEscogido);
                copiaNoExiste = true;
                break;

            }
        }
        if (copiaNoExiste) {
            System.out.println("ALUMNO ELIMINADO CON EXITO");
        } else {
            System.out.println("ALUMNO NO SE ENCUENTRA EN LA BASE DE DATOS");
        }
    }

    @Override
    public void listar(List<Alumno> lista) {
//        System.out.println("Lista de alumnos");
//        for (Alumno alumnoEscogido : listaAlumnos) {
//            System.out.println(alumnoEscogido.toString());
//        }
        listaAlumnos.clear();
        alumnoJPADAO.anyadirLista(listaAlumnos);
    }

    public List<Alumno> getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    public List<Alumno> coincidenciaExactaNombre(String nombreRecibido) {
        return alumnoJPADAO.coincidenciaExactaNombre(nombreRecibido);
    }

    public List<Alumno> contienePalabraClaveNombre(String nombreRecibido) {
        return alumnoJPADAO.contienePalabraClaveNombre(nombreRecibido);
    }

    public List<Alumno> empiezaPorNombre(String nombreRecibido) {
        return alumnoJPADAO.empiezaPorNombre(nombreRecibido);
    }

    public List<Alumno> terminaEnNombre(String nombreRecibido) {
        return alumnoJPADAO.terminaEnNombre(nombreRecibido);
    }

    public List<Alumno> coincidenciaExactaDni(String dniRecibido) {
        return alumnoJPADAO.coincidenciaExactaDni(dniRecibido);
    }

    public List<Alumno> contienePalabraClaveDni(String dniRecibido) {
        return alumnoJPADAO.contienePalabraClaveDni(dniRecibido);
    }

    public List<Alumno> empiezaPorDni(String dniRecibido) {
        return alumnoJPADAO.empiezaPorDni(dniRecibido);
    }

    public List<Alumno> terminaEnDni(String dniRecibido) {
        return alumnoJPADAO.terminaEnDni(dniRecibido);
    }

    @GET("/api/alumnos")
    public void apiListaAlumnos(Response response) {
        response.json(alumnoJPADAO.obtenerTodosAlumnos());
    }

    @GET("/api/alumno/busca/:id")
    public void apiGetAlumnoId(Response response, @PathParam long id) {
        Optional<Alumno> alumno = alumnoJPADAO.coincidenciaExactaId(id).stream()
                .filter(a -> a.getId() == id).findAny();
        if (alumno.isPresent()) {
            response.status(200);
            response.json(alumno.get());
        } else
            response.status(400);
    }

    @POST("/api/alumno/inserta")
    public void apiAddAlumno(@Body String body, Response response) {
        try {
            Alumno alumno = new Gson().fromJson(body, new TypeToken<Alumno>() {
            }.getType());
            if (alumnoJPADAO.insertarenTabla(alumno))
                response.status(200);
            else
                response.status(400);
        } catch (Exception e) {
            e.printStackTrace();
            response.status(400);
        }
    }

    @PUT("/api/alumno/modifica/:id")
    public void editarAlumno(@PathParam long id, @Body String body, Response response) {
        Alumno alumno = new Gson().fromJson(body, new TypeToken<Alumno>() {
        }.getType());
        if (alumnoJPADAO.modificarTabla(id,alumno))
            response.status(200);
        else
            response.status(400);
    }

    @DELETE("/api/alumno/elimina/:id")
    public void eliminarAlumno(@PathParam long id, Response response) {
        if (alumnoJPADAO.eliminardeTabla(id))
            response.status(200);
        else
            response.status(400);
    }


    //JPAAAAA

    public AlumnoJPADAO getAlumnoJPADAO() {
        return alumnoJPADAO;
    }


    public void setAlumnoJPADAO(AlumnoJPADAO alumnoJPADAO) {
        this.alumnoJPADAO = alumnoJPADAO;
    }
}
