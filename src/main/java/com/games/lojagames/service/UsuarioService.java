package com.games.lojagames.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.games.lojagames.model.UsuarioLogin;
import com.games.lojagames.model.UsuarioModel;
import com.games.lojagames.repository.UsuarioRepository;
import com.games.lojagames.security.JwtService;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<UsuarioModel> getAll() {
		return usuarioRepository.findAll();
	}

	public Optional<UsuarioModel> getById(Long id) {
		return usuarioRepository.findById(id);
	}

	public Optional<UsuarioModel> cadastrarUsuario(UsuarioModel usuario) {

		validarIdade(usuario);
		
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setId(null);

		return Optional.of(usuarioRepository.save(usuario));
	}

	public Optional<UsuarioModel> atualizarUsuario(UsuarioModel usuario) {

		if (usuarioRepository.findById(usuario.getId()).isEmpty()) {
			return Optional.empty();
		}

		Optional<UsuarioModel> usuarioExistente = usuarioRepository.findByUsuario(usuario.getUsuario());

		if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O E-mail ja esta em uso!");
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		return Optional.of(usuarioRepository.save(usuario));
	}

	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		if (usuarioLogin.isEmpty()) {
			return Optional.empty();
		}

		UsuarioLogin login = usuarioLogin.get();

		try {

			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getUsuario(), login.getSenha()));

			return usuarioRepository.findByUsuario(login.getUsuario())
					.map(usuario -> construirRespostaLogin(login, usuario));

		} catch (Exception e) {
			return Optional.empty();
		}

	}

	private UsuarioLogin construirRespostaLogin(UsuarioLogin usuarioLogin, UsuarioModel usuario) {
		usuarioLogin.setId(usuario.getId());
		usuarioLogin.setNome(usuario.getNome());
		usuarioLogin.setFoto(usuario.getFoto());
		usuarioLogin.setSenha("");
		usuarioLogin.setToken(gerarToken(usuario.getUsuario()));

		return usuarioLogin;
	}

	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
	
	private void validarIdade(UsuarioModel usuario) {
		
		int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();

		if (idade < 18) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"O usuário deve ter 18 anos ou mais!"
			);
		}
	
	}
}
