package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

import org.junit.*;
import org.junit.experimental.theories.suppliers.TestedOn;
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
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    private Aluno a1;
    private Aluno a2;

    @Before
    public void setUp() {

        a1 = new Aluno();
        a1.setNome("João Henrique");
        a1.setTurno(Turno.NOTURNO);
        a1.setCurso(Curso.ADMINISTRACAO);
        a1.setStatus(Status.ATIVO);
        a1.setMatricula("925879");

        a2 = new Aluno();
        a2.setNome("Maria de Fátima");
        a2.setTurno(Turno.NOTURNO);
        a2.setCurso(Curso.ADMINISTRACAO);
        a2.setStatus(Status.INATIVO);
        a2.setMatricula("582014");

        this.serviceAluno.save(a1);
        this.serviceAluno.save(a2);
    }

    @Test
    public void getById() {
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        Long idGerado = aluno.getId(); //Fiz apenas uma alteração pois o ID é gerado automaticamente pelo Spring

        Aluno alunoRetorno = this.serviceAluno.getById(idGerado);
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        Assert.assertThrows(ConstraintViolationException.class, () -> {
                this.serviceAluno.save(aluno);});
    }
//testes implememtados por mim com base nos exemplos fornecidos
    @Test
    public void salvarSemMatricula(){
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        Assert.assertThrows(ConstraintViolationException.class, () -> {
                this.serviceAluno.save(aluno);});
        
    }

    @Test
    public void testFindAll(){
        List<Aluno> lista = this.serviceAluno.findAll();
        Assert.assertEquals(2, lista.size());
    }

    @Test
    public void testDeletById(){
        this.serviceAluno.deleteById(a1.getId());
        List<Aluno> lista = this.serviceAluno.findAll();
        Assert.assertEquals(1, lista.size());
    }

    @Test
    public void testFindByStatusAtivo() {
        List<Aluno> ativos = this.serviceAluno.findByStatusAtivo();
        Assert.assertEquals(1, ativos.size());
        Assert.assertEquals(Status.ATIVO, ativos.get(0).getStatus());
    }

    @Test
    public void testFindByStatusInativo() {
        List<Aluno> inativos = serviceAluno.findByStatusInativo();
        Assert.assertEquals(1, inativos.size());
        Assert.assertEquals(Status.INATIVO, inativos.get(0).getStatus());
    }
}