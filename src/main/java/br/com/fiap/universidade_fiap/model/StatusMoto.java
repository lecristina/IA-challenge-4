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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "status_motos")
public class StatusMoto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_status_motos")
	@SequenceGenerator(name = "seq_status_motos", sequenceName = "seq_status_motos", allocationSize = 1)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "moto_id")
	private Moto moto;
	
	@Column(nullable = false)
	private String status;
	
	@NotEmpty(message = "Área é obrigatória")
	@Size(max = 50, message = "Área deve ter no máximo 50 caracteres")
	private String area;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataCriacao;

	public StatusMoto() {
		this.dataCriacao = LocalDateTime.now();
	}

	public StatusMoto(Long id, Moto moto, String status, String area, Usuario usuario) {
		super();
		this.id = id;
		this.moto = moto;
		this.status = status;
		this.area = area;
		this.usuario = usuario;
		this.dataCriacao = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Moto getMoto() {
		return moto;
	}

	public void setMoto(Moto moto) {
		this.moto = moto;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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
