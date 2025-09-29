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
@Table(name = "operacoes")
public class Operacao {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operacoes")
	@SequenceGenerator(name = "seq_operacoes", sequenceName = "seq_operacoes", allocationSize = 1)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "moto_id")
	private Moto moto;
	
	@Column(name = "tipo_operacao", nullable = false)
	private String tipoOperacao;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@Column(name = "observacoes")
	@Size(max = 4000, message = "Observações deve ter no máximo 4000 caracteres")
	private String observacoes;
	
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataCriacao;

	public Operacao() {
		this.dataCriacao = LocalDateTime.now();
	}

	public Operacao(Long id, Moto moto, String tipoOperacao, Usuario usuario, String observacoes) {
		super();
		this.id = id;
		this.moto = moto;
		this.tipoOperacao = tipoOperacao;
		this.usuario = usuario;
		this.observacoes = observacoes;
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

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
}
