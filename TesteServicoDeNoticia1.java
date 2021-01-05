package com.redcompany.testes;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.redcompany.wm3.Wm3Application;
import com.redcompany.wm3.domain.AnexoNoticia;
import com.redcompany.wm3.domain.Categoria;
import com.redcompany.wm3.domain.Noticia;
import com.redcompany.wm3.domain.Usuario;
import com.redcompany.wm3.domain.servico.ServicoNoticia;

@SpringBootTest(classes = Wm3Application.class)
@RunWith(SpringRunner.class)
public class TesteServicoDeNoticia {

	@Autowired
	private ServicoNoticia servicoNoticia;
	
	private static String idUsuarioTeste = "5c94ffcde4b03d74e12c56a5";
	
	@Test
	@Order(0)
	public void buscarNoticias() {
		List<String> perfis = new ArrayList<String>();
		perfis.add("ADMINISTRADOR");
		
		Assert.assertNotNull(servicoNoticia.obterTodas(perfis));
		
	}
	
	@Test
	@Order(1)
	public void SimularCRUD() {
		List<String> perfis = new ArrayList<String>();
		perfis.add("ADMINISTRADOR");
		
		Usuario usuario = new Usuario(idUsuarioTeste);
		usuario.setNome("Gunter Mingato");
		usuario.setEmail("guntermingato@gmail.com");
		usuario.setCodigosPerfil(perfis);
		
		Noticia noticia = new Noticia();
		noticia.setTitulo("Teste");
		noticia.setTexto("Teste");
		noticia.setResumo("Teste");
		noticia.setAutor("Teste");
		noticia.setPublicador(usuario);
		noticia.setUrlCapa("http://site.imagem.com");
		noticia.setCategoria(new Categoria("5dbb4f29b90d423c503f0d23", "News"));
		noticia.setAnexos(new ArrayList<AnexoNoticia>());
		noticia.setIdGrupos(new ArrayList<String>());
		
		
		//ADICIONAR
		noticia = servicoNoticia.salvar(noticia);
		//se foia dicionado o id é diferente de null ou de ""
		noticia = servicoNoticia.buscar(noticia.get_id());
		Assert.assertNotNull(noticia.get_id());
		Assert.assertNotEquals("", noticia.get_id());
		
		
		//ALTERAR
		noticia.setTitulo("Titulo alterado");
		servicoNoticia.alterar(noticia.get_id(), noticia);
		noticia = servicoNoticia.buscar(noticia.get_id());
		Assert.assertEquals("Titulo alterado", noticia.getTitulo());
		
		
		//REMOVER
		servicoNoticia.remover2(noticia.get_id());
		Assert.assertNull(servicoNoticia.buscar2(noticia.get_id()));
	}
	
	@Test
	@Order(2)
	public void SimularCRUD_com_valores_invalidos() {
		List<String> perfis = new ArrayList<String>();
		perfis.add("ADMINISTRADOR");
		
		Usuario usuario = new Usuario(idUsuarioTeste);
		usuario.setNome("Gunter Mingato");
		usuario.setEmail("guntermingat");
		usuario.setCodigosPerfil(perfis);
		
		Noticia noticia = new Noticia();
		noticia.setTitulo("");
		noticia.setTexto("");
		noticia.setResumo("");
		noticia.setAutor("");
		noticia.setPublicador(usuario);
		noticia.setUrlCapa("");
		noticia.setCategoria(new Categoria("5dbb4f29b90d423c503f0d23", "News"));
		noticia.setAnexos(new ArrayList<AnexoNoticia>());
		noticia.setIdGrupos(new ArrayList<String>());
		
		
		//ADICIONAR
		noticia = servicoNoticia.salvar(noticia);
		//se foi adicionado o id é diferente de null ou de ""
		noticia = servicoNoticia.buscar2(noticia.get_id());
		Assert.assertNull(noticia);
		
		
		//ALTERAR
		noticia.setTitulo("Titulo alterado");
		servicoNoticia.alterar(noticia.get_id(), noticia);
		noticia = servicoNoticia.buscar(noticia.get_id());
		Assert.assertEquals("Titulo alterado", noticia.getTitulo());
		
		
		//REMOVER
		servicoNoticia.remover2(noticia.get_id());
		Assert.assertNull(servicoNoticia.buscar2(noticia.get_id()));
	}

}
