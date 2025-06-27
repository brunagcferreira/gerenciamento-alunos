package br.com.gerenciamento.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void CadastrarUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/salvarUsuario")
                .param("email", "teste@email.com")
                .param("user", "usuarioTeste")
                .param("senha", "senha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Assert.assertNotNull(usuarioRepository.findByEmail("teste@email.com"));
    }

    @Test
    public void RetornarIndexQuandoLogarComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@email.com");
        usuario.setUser("usuario123");
        usuario.setSenha(Util.md5("senha123"));
        usuarioRepository.save(usuario);

        mockMvc.perform(post("/login")
                .param("user", "usuario123")
                .param("senha", "senha123"))
                .andExpect(status().isOk());
    }

    @Test
    public void ExibirTelaDeCadastroComUsuarioNoModelo() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void InvalidarSessaoNoLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogado", new Usuario());

        mockMvc.perform(post("/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"));
    }
}

