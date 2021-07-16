package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cotiinformatica.dtos.FornecedorGetDTO;
import br.com.cotiinformatica.dtos.FornecedorPostDTO;
import br.com.cotiinformatica.dtos.FornecedorPutDTO;
import br.com.cotiinformatica.entities.Fornecedor;
import br.com.cotiinformatica.interfaces.IFornecedorRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@Transactional
public class FornecedoresController {
	// criando metodos no padrao REST...
	// atributo para armazenar o endereço do serviço
	private static final String ENDPOINT = "/api/fornecedores";

	// inicializaçao do repositorio
	@Autowired
	private IFornecedorRepository fornecedorRepository;

	// metodo para cadastrar fornecedor
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.POST)
	@ResponseBody // define que o método retorna dados
	public ResponseEntity<String> post(@RequestBody FornecedorPostDTO dto) {

		try {

			if (fornecedorRepository.findByCnpj(dto.getCnpj()) != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)// HTTP 400
						.body("O CNPJ informado já encontra-se cadastrado.");
			}

			// resgatar os dados do DTO e transferi-los para a entidade
			Fornecedor fornecedor = new Fornecedor();
			fornecedor.setNome(dto.getNome());
			fornecedor.setCnpj(dto.getCnpj());

			// gravar no banco de dados
			fornecedorRepository.save(fornecedor);

			return ResponseEntity.status(HttpStatus.OK)// HTTP 200
					.body("Fornecedor " + fornecedor.getNome() + ", cadastrado com sucesso.");

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// metodo para Atualizar fornecedor
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.PUT)
	@ResponseBody // define que o método retorna dados
	public ResponseEntity<String> put(@RequestBody FornecedorPutDTO dto) {
		try {
			// procurar o fornecedor no banco de dados atraves do id..
			Optional<Fornecedor> result = fornecedorRepository.findById(dto.getIdFornecedor());
			// verificar se o fornecedor não foi encontrado..
			if (result == null || result.isEmpty()) {
				// retorna erro...
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)// HTTP 422
						.body("Fornecedor não encontrado.");
			}
			Fornecedor fornecedor = result.get();
			fornecedor.setNome(dto.getNome());

			fornecedorRepository.save(fornecedor);

			return ResponseEntity.status(HttpStatus.OK)// HTTP 200
					.body("Fornecedor atualizado com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// metodo para deletar fornecedor
	@CrossOrigin
	@RequestMapping(value = ENDPOINT + "/{idFornecedor}", method = RequestMethod.DELETE)
	@ResponseBody // define que o método retorna dados
	public ResponseEntity<String> delete(@PathVariable("idFornecedor") Integer idFornecedor) {
		try {
			// procurar o fornecedor no banco de dados atraves do id..
			Optional<Fornecedor> result = fornecedorRepository.findById(idFornecedor);
			// verificar se o fornecedor não foi encontrado..
			if (result == null || result.isEmpty()) {
				// retorna erro...
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)// HTTP 422
						.body("Fornecedor não encontrado.");
			}
			// obtendo os dados do fornecedor
			Fornecedor fornecedor = result.get();
			// excluindo no banco de dados
			fornecedorRepository.delete(fornecedor);
			return ResponseEntity.status(HttpStatus.OK)// HTTP 200
					.body("Fornecedor atualizado com sucesso.");

		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// metodo para consultar fornecedor
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.GET)
	@ResponseBody // define que o método retorna dados
	public ResponseEntity<List<FornecedorGetDTO>> get() {

		try {
			// declarar uma lista da classe fornecedor getDTO.java
			List<FornecedorGetDTO> result = new ArrayList<FornecedorGetDTO>();
			// consultar e pecorrer os fornecedores obtidos no bancop de dados
			for (Fornecedor fornecedor : fornecedorRepository.findAll()) {
				FornecedorGetDTO dto = new FornecedorGetDTO();

				dto.setIdFornecedor(fornecedor.getIdFornecedor());
				dto.setNome(fornecedor.getNome());
				dto.setCnpj(fornecedor.getCnpj());

				result.add(dto);// adicionar na lista
			}
			return ResponseEntity.status(HttpStatus.OK)// HTTP 200
					.body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// HTTP 500
					.body(null);
		}

	}

	// metodo para consultar 1 fornecedor por ID
	@CrossOrigin
	@RequestMapping(value = ENDPOINT + "/{idFornecedor}", method = RequestMethod.GET)
	@ResponseBody // define que o método retorna dados
	public ResponseEntity<FornecedorGetDTO> getById(@PathVariable("idFornecedor") Integer idFornecedor) {
		try {
			Optional<Fornecedor> result = fornecedorRepository.findById(idFornecedor);
			// verificar se o fornecedor não foi encontrado..
			if (result == null || result.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT)// HTTP 204(sucesso dizendo vazio)
						.body(null);
			} else {
				// obter os dados do fornecedor
				Fornecedor fornecedor = result.get();

				FornecedorGetDTO dto = new FornecedorGetDTO();// data transfer object

				dto.setIdFornecedor(fornecedor.getIdFornecedor());
				dto.setNome(fornecedor.getNome());
				dto.setCnpj(fornecedor.getCnpj());

				return ResponseEntity.status(HttpStatus.OK).body(dto);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)// HTTP 500
					.body(null);
		}
	}
}
