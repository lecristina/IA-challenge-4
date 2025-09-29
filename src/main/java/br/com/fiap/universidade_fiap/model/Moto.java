package br.com.fiap.universidade_fiap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "motos")
public class Moto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_motos")
	@SequenceGenerator(name = "seq_motos", sequenceName = "seq_motos", allocationSize = 1)
	private Long id;
	
	@NotBlank(message = "Placa é obrigatória")
	@Pattern(regexp = "^[A-Z]{3}[-]?[0-9]{4}$", message = "Placa deve estar no formato ABC-1234 ou ABC1234")
	@Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
	@Column(unique = true)
	private String placa;
	
	@NotBlank(message = "Chassi é obrigatório")
	@Size(min = 17, max = 50, message = "Chassi deve ter entre 17 e 50 caracteres")
	@Column(unique = true)
	private String chassi;
	
	@Size(max = 50, message = "Motor deve ter no máximo 50 caracteres")
	private String motor;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataCriacao;

	public Moto() {
		this.dataCriacao = LocalDateTime.now();
	}

	public Moto(Long id, String placa, String chassi, String motor, Usuario usuario) {
		super();
		this.id = id;
		this.placa = placa;
		this.chassi = chassi;
		this.motor = motor;
		this.usuario = usuario;
		this.dataCriacao = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}
