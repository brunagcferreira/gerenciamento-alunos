package br.com.gerenciamento.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;
import org.junit.Before;
import java.util.List;
import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void setUp() {
        alunoRepository.deleteAll();

        Aluno a1 = new Aluno();
        a1.setNome("Vinicius");
        a1.setTurno(Turno.NOTURNO);
        a1.setCurso(Curso.ADMINISTRACAO);
        a1.setStatus(Status.ATIVO);
        a1.setMatricula("123456");

        Aluno a2 = new Aluno();
        a2.setNome("Maria");
        a2.setTurno(Turno.NOTURNO);
        a2.setCurso(Curso.ADMINISTRACAO);
        a2.setStatus(Status.ATIVO);
        a2.setMatricula("582014");


        Aluno a3 = new Aluno();
        a3.setNome("Maria Joana");
        a3.setTurno(Turno.NOTURNO);
        a3.setCurso(Curso.ADMINISTRACAO);
        a3.setStatus(Status.INATIVO);
        a3.setMatricula("463078");


        alunoRepository.save(a1);
        alunoRepository.save(a2);
        alunoRepository.save(a3);
    }

    @Test
    public void testFindByStatusAtivo() {
        List<Aluno> ativos = alunoRepository.findByStatusAtivo();
        
        //Verifica a quantidade de alunos retornados com o status ATIVO, nesse caso deveriam ser 2
        Assert.assertEquals(2, ativos.size());

        //Verifica se, de fato, todos tem satus ATIVO
        for(Aluno aluno : ativos){
            Assert.assertEquals(Status.ATIVO, aluno.getStatus());
        }
    }

    @Test
    public void testFindByStatusInativo() {
        List<Aluno> inativos = alunoRepository.findByStatusInativo();
        
        //Verifica a quantidade de alunos retornados com o status INATIVO, nesse caso deveriam ser 1
        Assert.assertEquals(1, inativos.size());

        //Verifica se, de fato, todos tem satus INATIVO
        for(Aluno aluno : inativos){
            Assert.assertEquals(Status.INATIVO, aluno.getStatus());
        }
    }

    @Test
    public void testFindByNomeContainingIgnoreCaseSucess() {
        List<Aluno> resultado = alunoRepository.findByNomeContainingIgnoreCase("maria");
        
        //Verifica a quantidade de alunos retornados com o nome "maria", nesse caso deveriam ser 2
        Assert.assertEquals(2, resultado.size());
    }

    @Test
    public void testFindByNomeContainingIgnoreCaseFail() {
        List<Aluno> resultado = alunoRepository.findByNomeContainingIgnoreCase("luanna");
        
        //Verifica a quantidade de alunos retornados com o nome "luanna", nesse caso deveriam ser 0
        Assert.assertTrue(resultado.isEmpty());
    }
}
