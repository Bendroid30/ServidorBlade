package com.example.controlador.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ControladorDAOJPA {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");



    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
