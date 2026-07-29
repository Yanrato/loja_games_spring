package com.games.lojagames.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O atributo Nome é obrigatório!")
    @Size(max = 255, message = "O atributo Nome deve conter no máximo 255 caracteres!")
    private String nome;

    @Schema(example = "email@email.com.br")
    @NotBlank(message = "O atributo Usuário é obrigatório!")
    @Email(message = "O atributo Usuário deve ser um e-mail válido!")
    @Size(max = 255, message = "O atributo Usuário deve conter no máximo 255 caracteres!")
    private String usuario;

    @NotBlank(message = "O atributo Senha é obrigatório!")
    @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres!")
    private String senha;

    @Size(max = 500, message = "O atributo Foto deve conter no máximo 500 caracteres!")
    private String foto;

    @NotNull(message = "A data de nascimento é obrigatória!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

 
}