package com.cristiano.criptografarsenha.controller;

import com.cristiano.criptografarsenha.model.UsuarioModel;
import com.cristiano.criptografarsenha.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuarioController {

    public final UsuarioRepository repository;
    public final PasswordEncoder encoder;
    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarTodos(){

        return ResponseEntity.ok(repository.findAll());
    }
    public static boolean senhaForte(String senha) {
        if (senha.length() < 8) return false;

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;
        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }
        return achouNumero && achouMaiuscula && achouMinuscula && achouSimbolo;
    }
    @PostMapping
    public ResponseEntity<UsuarioModel> salvar(@RequestBody UsuarioModel usuario){
        senhaForte(usuario.getPassword());
        usuario.setPassword(encoder.encode(usuario.getPassword()));

        return ResponseEntity.status(201).body(repository.save(usuario));

    }
    @PutMapping
    public ResponseEntity<UsuarioModel> editar(@RequestBody UsuarioModel usuario){
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return ResponseEntity.ok(repository.save(usuario));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Optional<UsuarioModel>> deletar(@PathVariable Integer id){
        Optional<UsuarioModel> usuario = repository.findById(id);
        repository.deleteById(id);
        return ResponseEntity.ok(usuario);

    }
    public Boolean validar(UsuarioModel usuario) {
        String password = repository.getById(usuario.getId()).getPassword();
        Boolean valid = encoder.matches(usuario.getPassword(), password);
        return valid;
    }

}
