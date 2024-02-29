package com.example.modulo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="alumnos")
public class Alumno{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "dni", unique = true, length = 9)
	private String DNI;
	@Column(name = "telefono", length = 9)
	private String tLF;
	@Column(name = "edad")
	private int edad;
	@Column(name="curso")
	private String curso;

	public Alumno(String nombre, String curso, String dNI, String tLF, int edad) {
		this.nombre=nombre;
		this.curso=curso;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
	}

	public Alumno(long id, String nombre, String curso, String dNI, String tLF, int edad) {
		this.nombre=nombre;
		this.curso=curso;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
		setId(id);
		this.curso = curso;
	}

	public Alumno(long id) {
		this.nombre=null;
		this.curso=null;
		this.DNI=null;
		this.tLF=null;
		this.edad=0;
		setId(id);
	}

	public Alumno(long id, String nombre, String dNI, int edad, String tLF, String curso, List<Double> notas) {
		this.nombre=nombre;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
		setCurso(curso);
		setId(id);
	}

	public Alumno() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String DNI) {
		this.DNI = DNI;
	}

	public String gettLF() {
		return tLF;
	}

	public void settLF(String tLF) {
		this.tLF = tLF;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	@Override
	public String toString() {
		return "Alumno [curso=" + curso + ", getNombre()=" + getNombre() + ", getDNI()="
				+ getDNI() + ", gettLF()=" + gettLF() + ", getEdad()=" + getEdad() + "]";
	}

}
