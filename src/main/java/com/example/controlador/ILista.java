package com.example.controlador;

import java.util.List;

public interface ILista<T> {
	 void agregar(T cosa) throws Exception;
	 T buscar(T cosa);
	 void eliminar(long id);
	 void listar(List<T> lista);
}
