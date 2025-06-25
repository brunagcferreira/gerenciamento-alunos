package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void setup() {
        this.alunoRepository.deleteAll();
    }

    @Test
    public void testGetInserirAlunosForm() throws Exception {
        mockMvc.perform(get("/inserirAlunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"));
    }

    @Test
    public void testInserirAlunoComSucesso() throws Exception {
        mockMvc.perform(post("/InsertAlunos")
                .param("nome", "Maria Oliveira")
                .param("matricula", "123456")
                .param("curso", "INFORMATICA")
                .param("status", "ATIVO")
                .param("turno", "MATUTINO")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));

        List<Aluno> alunos = this.alunoRepository.findAll();
        Assert.assertEquals(1, alunos.size());
        Assert.assertEquals("Maria Oliveira", alunos.get(0).getNome());
    }

    @Test
    public void testListagemAlunos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("João Pedro");
        aluno.setMatricula("78910");
        aluno.setCurso(Curso.DIREITO);
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.NOTURNO);
        this.alunoRepository.save(aluno);

        mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"));
    }

    @Test
    public void testRemoverAluno() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Excluir");
        aluno.setMatricula("9999");
        aluno.setCurso(Curso.CONTABILIDADE);
        aluno.setStatus(Status.INATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno = this.alunoRepository.save(aluno);

        mockMvc.perform(get("/remover/" + aluno.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));

        Assert.assertFalse(alunoRepository.findById(aluno.getId()).isPresent());
    }
}
