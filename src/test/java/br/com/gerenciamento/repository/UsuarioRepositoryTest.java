package br.com.gerenciamento.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void setUp(){
        usuarioRepository.deleteAll();

        Usuario u1 = new Usuario();
        u1.setEmail("u1@example.com");
        u1.setUser("u1_user_example");
        u1.setSenha("1234u1");

        Usuario u2 = new Usuario();
        u2.setEmail("u2@example.com");
        u2.setUser("u2_user_example");
        u2.setSenha("1234u2");

        Usuario u3 = new Usuario();
        u3.setEmail("u3@example.com");
        u3.setUser("u3_user_example");
        u3.setSenha("1234u3");

        usuarioRepository.save(u1);
        usuarioRepository.save(u2);
        usuarioRepository.save(u3);
    }

    @Test
    public void testFindByEmailSucess(){

        Usuario usuario = usuarioRepository.findByEmail("u3@example.com");

        Assert.assertEquals("u3_user_example", usuario.getUser());

    }

    @Test
    public void testFindByEmailFail(){

        Usuario usuario = usuarioRepository.findByEmail("exampe@email.com");

        Assert.assertNull(usuario);
    }

    @Test
    public void testBuscarLoginSucess(){
        Usuario usuario = usuarioRepository.buscarLogin("u3_user_example", "1234u3");
        Assert.assertEquals("u3@example.com", usuario.getEmail());
    }

    @Test
    public void testBuscarLoginFail(){
        Usuario usuario = usuarioRepository.buscarLogin("teste", "1234");
        Assert.assertNull(usuario);
    }
}
