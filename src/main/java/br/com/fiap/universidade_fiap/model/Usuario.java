package br.com.fiap.universidade_fiap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario{ 

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuarios")
	@SequenceGenerator(name = "seq_usuarios", sequenceName = "seq_usuarios", allocationSize = 1)
	private Long id;
	
	@NotBlank(message = "Nome da filial é obrigatório")
	@Size(min = 2, max = 100, message = "Nome da filial deve ter entre 2 e 100 caracteres")
	@Column(name = "nome_filial")
	private String nomeFilial;
	
	@Email(message = "Email deve ser válido")
	@NotBlank(message = "Email é obrigatório")
	@Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
	@Column(name = "email", unique = true)
	private String email;
	
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, max = 255, message = "Senha deve ter entre 6 e 255 caracteres")
	@Column(name = "senha_hash")
	private String senhaHash;
	
	@NotBlank(message = "CNPJ é obrigatório")
	@Pattern(regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$", 
			 message = "CNPJ deve estar no formato 00.000.000/0000-00")
	@Size(max = 18, message = "CNPJ deve ter no máximo 18 caracteres")
	@Column(name = "cnpj", unique = true, length = 18)
	private String cnpj;
	
	@Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
	private String endereco;
	
	@Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
	private String telefone;
	
	@NotNull(message = "Perfil é obrigatório")
	@Column(nullable = false)
	private String perfil;
	
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataCriacao;

	public Usuario() {
		this.dataCriacao = LocalDateTime.now();
	}

	public Usuario(Long id, String nomeFilial, String email, String senhaHash, String cnpj, 
			String endereco, String telefone, String perfil) {
		super();
		this.id = id;
		this.nomeFilial = nomeFilial;
		this.email = email;
		this.senhaHash = senhaHash;
		this.cnpj = cnpj;
		this.endereco = endereco;
		this.telefone = telefone;
		this.perfil = perfil;
		this.dataCriacao = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeFilial() {
		return nomeFilial;
	}

	public void setNomeFilial(String nomeFilial) {
		this.nomeFilial = nomeFilial;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        if (this.cnpj != null) {
            this.cnpj = this.cnpj.replaceAll("[^0-9]", "");
        }
        if (this.telefone != null) {
            this.telefone = this.telefone.replaceAll("[^0-9]", "");
        }
    }

}
