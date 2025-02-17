package com.tiendacarrito.tienda.controller;

public record  AuthRequestDto (
        String nombre,
        String username,
        String password,
        String email
){}



