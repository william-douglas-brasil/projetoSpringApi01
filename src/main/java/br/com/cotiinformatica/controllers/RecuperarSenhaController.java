package br.com.cotiinformatica.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cotiinformatica.dtos.RecuperarSenhaPostDTO;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.helpers.PasswordHelper;
import br.com.cotiinformatica.interfaces.IUsuarioRepository;
import br.com.cotiinformatica.security.Criptografia;
import br.com.cotiinformatica.senders.MailSender;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@Transactional
public class RecuperarSenhaController {

	// atributo para armazenar o endereço do serviço
	private static final String ENDPOINT = "/api/recuperarsenha";

	@Autowired // Injeção de dependência (inicialização)
	private IUsuarioRepository usuarioRepository;

	@Autowired // Injeção de dependência (inicialização)
	private MailSender mailSender;

	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> post(@RequestBody RecuperarSenhaPostDTO dto) {



		try {
			
			//consultando o usuario no banco de dados atraves do email
			Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());
			
			//verificar se o email esta cadastrado na base de dados..
			if(usuario != null) {
				
				//gerar uma nova senha para o usuario..
				String novaSenha = PasswordHelper.generateRandomPassword();
				
				//alterar a senha do usuario no banco de dados
				usuario.setSenha(Criptografia.criptografar(novaSenha));
				usuarioRepository.save(usuario);
				
				//enviar a senha para o email do usuario
				enviarNovaSenha(usuario, novaSenha);
				
				return ResponseEntity
						.status(HttpStatus.OK) //HTTP 200
						.body("Uma nova senha foi gerada com sucesso. Verifique seu email.");
			}
			else {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST) //HTTP 400
						.body("O email informado não foi encontrado.");
			}
		}
		catch(Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR) //HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// método para realizar o envio da mensagem..
	private void enviarNovaSenha(Usuario usuario, String novaSenha) throws Exception {
				
		String assunto = "Nova senha gerada com sucesso - COTI Informática API de Produtos";
		String mensagem = "Olá, " + usuario.getNome() + "\n\n"
						+ "Sua nova senha foi gerada com sucesso, para acessar o sistema utilize a senha: " + novaSenha
						+ "\n\n"
						+ "Att, \n"
						+ "Equipe COTI Informática";
		
		//enviando o email..
		mailSender.sendMessage(usuario.getEmail(), assunto, mensagem);
	}
}
