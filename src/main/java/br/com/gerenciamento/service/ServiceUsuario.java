package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.CriptoExistsException;
import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class ServiceUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void salvarUsuario(Usuario user) throws Exception {
        try {
            if (usuarioRepository.findByEmail(user.getEmail()) != null) {
                throw new EmailExistsException("Este email já esta cadastrado: " + user.getEmail());
            }
            user.setSenha(Util.md5(user.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new CriptoExistsException("Error na criptografia da senha");
        }
        usuarioRepository.save(user);
    }

    public Usuario loginUser(String user, String senha) {
        return usuarioRepository.buscarLogin(user, senha);
    }

    public Usuario getById(Long id) {
        return this.usuarioRepository.findById(id).get();
    }
}
