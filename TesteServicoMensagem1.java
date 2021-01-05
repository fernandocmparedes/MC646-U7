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
import com.redcompany.wm3.domain.Mensagem;
import com.redcompany.wm3.domain.MensagemVO;
import com.redcompany.wm3.domain.TipoMensagem;
import com.redcompany.wm3.domain.Usuario;
import com.redcompany.wm3.domain.servico.ServicoEnvioDeMensagem;
import com.redcompany.wm3.domain.servico.ServicoMensagem;
import com.redcompany.wm3.infra.exception.UsuarioNaoExistenteException;

@SpringBootTest(classes = Wm3Application.class)
@RunWith(SpringRunner.class)
public class TesteServicoMensagem {

	@Autowired
	private ServicoMensagem servicoMensagem;
	
	@Autowired
	private ServicoEnvioDeMensagem servicoEnvioDeMensagem;
	
	private static String idUsuarioTeste = "5c94ffcde4b03d74e12c56a5";
	private static String idUsuarioTeste2 = "5d25f8f939f8f93804e3cde9";
	
	private static String idUsuarioNaoExistente= "5c94ffcde4b03d74e12c56";
	
	@Test
	@Order(3)
	public void buscarMensagens() {
		
		Assert.assertNotEquals(0, servicoMensagem.ultimasMensagens(idUsuarioTeste, 0L).size());
		
		//Não pode exixstir mensagens qu vieram do futuro
		Assert.assertEquals(0, servicoMensagem.ultimasMensagens(idUsuarioTeste, System.currentTimeMillis()+(1000*60*60*24)).size());
	}
	
	@Test
	@Order(4)
	public void EnviarMensagens() {

		List<String> perfil = new ArrayList<String>();
		perfil.add("ADMINISTRADOR");
		
		MensagemVO mensagem = new MensagemVO(idUsuarioTeste2, "Mensagem de Teste", TipoMensagem.MENSAGEM);
		
		Usuario usuario = new Usuario(idUsuarioTeste);
		usuario.setNome("Gunter Mingato");
		usuario.setEmail("guntermingato@gmail.com");
		usuario.setCodigosPerfil(perfil);
		
		Assert.assertNotNull(servicoEnvioDeMensagem.enviar(mensagem, usuario));
		servicoMensagem.adiocionarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioTeste2);
	}
	
	@Test
	@Order(5)
	public void Simular() {
		List<Mensagem> msgs;
		int qtdMensagensNaoLidas, qtdMensagens;
		msgs = servicoMensagem.ultimasMensagens(idUsuarioTeste, idUsuarioTeste2, 0L);
		servicoMensagem.zerarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioTeste2);
		
		List<String> perfil = new ArrayList<String>();
		perfil.add("ADMINISTRADOR");
		
		Assert.assertNotEquals(0, msgs.size());
		qtdMensagens = msgs.size();
		
		qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioTeste2);
		
		Assert.assertEquals(0, qtdMensagensNaoLidas);
		
		MensagemVO mensagem = new MensagemVO(idUsuarioTeste2, "Mensagem de Teste", TipoMensagem.MENSAGEM);
		Usuario usuario = new Usuario(idUsuarioTeste);
		usuario.setNome("Gunter Mingato");
		usuario.setEmail("guntermingato@gmail.com");
		usuario.setCodigosPerfil(perfil);
		
		Assert.assertNotNull(servicoEnvioDeMensagem.enviar(mensagem, usuario));	
		servicoMensagem.adiocionarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioTeste2);
		
		qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioTeste2);
		
		Assert.assertEquals(1, qtdMensagensNaoLidas);
		
		msgs = servicoMensagem.ultimasMensagens(idUsuarioTeste, idUsuarioTeste2, 0L);
		servicoMensagem.zerarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioTeste2);
		
		Assert.assertNotEquals(0, msgs.size());
		
		qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioTeste2);
		
		Assert.assertEquals(0, qtdMensagensNaoLidas);
		Assert.assertEquals(qtdMensagens+1, msgs.size());
	}
	
	/*@Test
	@Order(6)
	public void SimularDestinatarioNaoExistente() {
		List<Mensagem> msgs;
		int qtdMensagensNaoLidas, qtdMensagens;
		msgs = servicoMensagem.ultimasMensagens(idUsuarioTeste, idUsuarioNaoExistente, 0L);
		servicoMensagem.zerarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioNaoExistente);
		
		List<String> perfil = new ArrayList<String>();
		perfil.add("ADMINISTRADOR");
		
		Assert.assertEquals(0, msgs.size());
		qtdMensagens = msgs.size();
		
		qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioNaoExistente);
		
		Assert.assertEquals(0, qtdMensagensNaoLidas);
		
		MensagemVO mensagem = new MensagemVO(idUsuarioNaoExistente, "Mensagem de Teste", TipoMensagem.MENSAGEM);
		Usuario usuario = new Usuario(idUsuarioTeste);
		usuario.setNome("Gunter Mingato");
		usuario.setEmail("guntermingato@gmail.com");
		usuario.setCodigosPerfil(perfil);
		Mensagem msg = null;
		try {
			msg = servicoEnvioDeMensagem.enviar(mensagem, usuario);
			
			servicoMensagem.adiocionarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioNaoExistente);
			
			qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioNaoExistente);
			
			Assert.assertEquals(0, qtdMensagensNaoLidas);
			
			msgs = servicoMensagem.ultimasMensagens(idUsuarioTeste, idUsuarioNaoExistente, 0L);
			servicoMensagem.zerarNumeroMensagensNaolidas(idUsuarioTeste, idUsuarioNaoExistente);
			
			Assert.assertEquals(0, msgs.size());
			
			qtdMensagensNaoLidas = servicoMensagem.quantidadeMensagensNaoLidas(idUsuarioTeste, idUsuarioNaoExistente);
			
			Assert.assertEquals(0, qtdMensagensNaoLidas);
			Assert.assertEquals(0, msgs.size());
			
		}catch(UsuarioNaoExistenteException e) {
			msg = null;
		}
		Assert.assertNull(msg);
		
		
	}*/
}
