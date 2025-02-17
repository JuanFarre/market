package com.tiendacarrito.tienda.controller;

import jakarta.security.auth.message.AuthStatus;

public record AuthResponseDto (
        String token,
        AuthenticationStatus authStatus,
        String message
){

}

