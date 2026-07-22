package com.games.lojagames.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.games.lojagames.model.ProdutoModel;
import com.games.lojagames.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<List<ProdutoModel>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoModel> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
						.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/menorque/{preco}")
	public ResponseEntity<List<ProdutoModel>> getByMenorQue(@PathVariable BigDecimal preco) {
	    return ResponseEntity.ok(produtoRepository.findByPrecoLessThanEqual(preco));
	}
	
	@GetMapping("maiorque/{preco}")
	public ResponseEntity<List<ProdutoModel>> getByMaiorQue(@PathVariable BigDecimal preco){
		return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThanEqual(preco));
	}
	
	@PostMapping
	public ResponseEntity<ProdutoModel> post(@Valid @RequestBody ProdutoModel produtoModel){
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produtoModel));
	}
	
	@PutMapping
	public ResponseEntity<ProdutoModel> put(@Valid @RequestBody ProdutoModel produtoModel){
		if(produtoRepository.existsById(produtoModel.getId()))
			return ResponseEntity.ok(produtoRepository.save(produtoModel));
		
		return ResponseEntity.notFound().build();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
		Optional<ProdutoModel> produto = produtoRepository.findById(id);	
		
		if(produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		produtoRepository.deleteById(id);
		}
}
