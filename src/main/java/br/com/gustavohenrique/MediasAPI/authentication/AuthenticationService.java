package br.com.gustavohenrique.MediasAPI.service;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String authenticate(){
        return "Authenticated";
    }
}
