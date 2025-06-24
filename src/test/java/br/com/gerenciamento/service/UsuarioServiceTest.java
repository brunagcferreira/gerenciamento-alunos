package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import java.security.NoSuchAlgorithmException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    private Usuario usuario1;

    @Before
    public void setUp() throws Exception {

        usuario1 = new Usuario();
        usuario1.setEmail("usuario@example.com");
        usuario1.setUser("usuario123");
        usuario1.setSenha("senha123");

        this.serviceUsuario.salvarUsuario(usuario1);
    }

    @Test
    public void testSalvarUsuarioComSucesso() throws Exception {
        Usuario novo = new Usuario();
        novo.setEmail("novo@email.com");
        novo.setUser("novo_user");
        novo.setSenha("novaSenha");
        this.serviceUsuario.salvarUsuario(novo);

        Usuario salvo = this.serviceUsuario.getById(novo.getId());
        Assert.assertNotNull(salvo);
        Assert.assertEquals("novo_user", salvo.getUser());
    }

    @Test
    public void testSalvarUsuarioEmailDuplicado() {
        Usuario duplicado = new Usuario();
        duplicado.setEmail("usuario@example.com");
        duplicado.setUser("usuario456");
        duplicado.setSenha("senha456");

        Assert.assertThrows(EmailExistsException.class, () -> {
            this.serviceUsuario.salvarUsuario(duplicado);
        });
    }

    @Test
    public void testLoginUserComSucesso() throws NoSuchAlgorithmException {
        String senhaCriptografada = br.com.gerenciamento.util.Util.md5("senha123");
        Usuario user = this.serviceUsuario.loginUser("usuario123", senhaCriptografada);
        Assert.assertNotNull(user);
        Assert.assertEquals("usuario@example.com", user.getEmail());
    }


    @Test
    public void testLoginUserFalha() {
        Usuario user = this.serviceUsuario.loginUser("usuarionaoexiste", "senha123");
        Assert.assertNull(user);
    }
}
