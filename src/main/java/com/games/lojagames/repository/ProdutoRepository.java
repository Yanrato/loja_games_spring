package com.games.lojagames.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.games.lojagames.model.ProdutoModel;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long>{

	List<ProdutoModel>findByPrecoLessThanEqual(BigDecimal preco);
	
	List<ProdutoModel>findByPrecoGreaterThanEqual(BigDecimal preco);

}
