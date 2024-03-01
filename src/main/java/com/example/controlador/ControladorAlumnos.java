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
    private AlumnoJPADAO alumnoJPADAO = new AlumnoJPADAO();

    /**
     * Método encargado de añadir alumnos a la base de datos
     *
     * @param alumno Alumno que queremos insertar
     * @throws AlumnoExisteException excepción lanzada al detectar alumno duplicado
     */
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
            alumnoJPADAO.insertarenTabla(alumno);
            listaAlumnos.clear();
            alumnoJPADAO.anyadirLista(listaAlumnos);


        } else {
            //Lanzar excepción AlumnoExiste
            throw new AlumnoExisteException("EL ALUMNO YA SE ENCUENTRA EN LA LISTA");
        }

    }

    /**
     * Método encargado de buscar alumnos en la base de datos
     *
     * @param alumno Alumno que queremos buscar
     * @return Alumno encontrado en la base de datos
     */
    @Override
    public Alumno buscar(Alumno alumno) {
        return alumnoJPADAO.buscarTabla(alumno);

    }

    /**
     * ELimina al alumno recibido de la base de datos
     *
     * @param id ID del alumno que queremos eliminar
     */
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

    /**
     * Actualizar lista de alumnos con los datos de la base de datos
     *
     * @param lista Lista que queremos actualizar
     */
    @Override
    public void listar(List<Alumno> lista) {
        listaAlumnos.clear();
        alumnoJPADAO.anyadirLista(listaAlumnos);
    }

    /**
     * Lista a todos los alumnos encontrados en la base de datos
     *
     * @param response Respuesta con la informacion de los alumnos
     */
    @GET("/api/alumnos")
    public void apiListaAlumnos(Response response) {
        response.json(alumnoJPADAO.obtenerTodosAlumnos());
    }

    /**
     * Busca y enseña al alumno que buscamos por su Id
     *
     * @param response Respuesta del comando
     * @param id       Id del alumno que queremos buscar
     */
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

    /**
     * Inserta a un nuevo alumno a la base de datos
     *
     * @param body     Información del Json que recibe
     * @param response Respuesta del comando
     */
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

    /**
     * Modifica los atributos del alumno
     *
     * @param id       Id del alumno que deseamos modificar
     * @param body     Información del Json que recibe
     * @param response Respuesta del comando
     */
    @PUT("/api/alumno/modifica/:id")
    public void editarAlumno(@PathParam long id, @Body String body, Response response) {
        Alumno alumno = new Gson().fromJson(body, new TypeToken<Alumno>() {
        }.getType());
        if (alumnoJPADAO.modificarTabla(id, alumno))
            response.status(200);
        else
            response.status(400);
    }

    /**
     * Elimina al alumno con dicha id
     *
     * @param id       Id del alumno que deseamos eliminar
     * @param response Respuesta del comando
     */
    @DELETE("/api/alumno/elimina/:id")
    public void eliminarAlumno(@PathParam long id, Response response) {
        if (alumnoJPADAO.eliminardeTabla(id))
            response.status(200);
        else
            response.status(400);
    }

}
