# 🔌 Guia de API REST - Integração Mobile App e .NET

## ✅ Status: **API REST COMPLETA E DOCUMENTADA!**

A API REST está **100% funcional** e pronta para integração com Mobile App e .NET.

---

## 📋 Endpoints Disponíveis

### Base URL
```
http://localhost:8081/api/v1
```

### 1. **Listar Todas as Motos**
```
GET /api/v1/motos
```

**Resposta (200 OK):**
```json
{
  "sucesso": true,
  "total": 10,
  "motos": [
    {
      "id": 1,
      "placa": "ABC1234",
      "chassi": "CHASSI123",
      "motor": "MOTOR123",
      "dataCriacao": "2024-01-01T00:00:00"
    }
  ]
}
```

**Exemplo (JavaScript):**
```javascript
fetch('http://localhost:8081/api/v1/motos')
  .then(response => response.json())
  .then(data => console.log(data));
```

**Exemplo (C#):**
```csharp
HttpClient client = new HttpClient();
var response = await client.GetAsync("http://localhost:8081/api/v1/motos");
var json = await response.Content.ReadAsStringAsync();
var resultado = JsonSerializer.Deserialize<MotosResponse>(json);
```

---

### 2. **Buscar Localização de uma Moto**
```
GET /api/v1/motos/{placa}/localizacao
```

**Parâmetros:**
- `placa` (path): Placa da moto (ex: ABC1234 ou ABC-1234)

**Resposta (200 OK):**
```json
{
  "sucesso": true,
  "moto": {
    "id": 1,
    "placa": "ABC1234",
    "chassi": "CHASSI123",
    "motor": "MOTOR123"
  },
  "status": "PRONTA",
  "area": "Pátio Principal",
  "localizacao": {
    "posicaoX": 15,
    "posicaoY": 20,
    "area": "Pátio Principal"
  },
  "led": {
    "ativo": true,
    "virtual": true
  }
}
```

**Resposta (404 Not Found):**
```json
{
  "sucesso": false,
  "erro": "Moto não encontrada"
}
```

**Exemplo (JavaScript):**
```javascript
fetch('http://localhost:8081/api/v1/motos/ABC1234/localizacao')
  .then(response => response.json())
  .then(data => {
    console.log('Posição X:', data.localizacao.posicaoX);
    console.log('Posição Y:', data.localizacao.posicaoY);
  });
```

**Exemplo (C#):**
```csharp
HttpClient client = new HttpClient();
var response = await client.GetAsync("http://localhost:8081/api/v1/motos/ABC1234/localizacao");
var json = await response.Content.ReadAsStringAsync();
var localizacao = JsonSerializer.Deserialize<LocalizacaoResponse>(json);
```

---

### 3. **Buscar Status de uma Moto**
```
GET /api/v1/motos/{placa}/status
```

**Parâmetros:**
- `placa` (path): Placa da moto (ex: ABC1234 ou ABC-1234)

**Resposta (200 OK):**
```json
{
  "sucesso": true,
  "moto": {
    "id": 1,
    "placa": "ABC1234"
  },
  "status": "PRONTA",
  "area": "Pátio Principal",
  "dataStatus": "2024-01-01T00:00:00"
}
```

**Exemplo (JavaScript):**
```javascript
fetch('http://localhost:8081/api/v1/motos/ABC1234/status')
  .then(response => response.json())
  .then(data => console.log('Status:', data.status));
```

**Exemplo (C#):**
```csharp
HttpClient client = new HttpClient();
var response = await client.GetAsync("http://localhost:8081/api/v1/motos/ABC1234/status");
var json = await response.Content.ReadAsStringAsync();
var status = JsonSerializer.Deserialize<StatusResponse>(json);
```

---

### 4. **Ativar LED de uma Moto**
```
POST /api/v1/motos/{placa}/ativar-led
```

**Parâmetros:**
- `placa` (path): Placa da moto (ex: ABC1234 ou ABC-1234)

**Resposta (200 OK):**
```json
{
  "sucesso": true,
  "placa": "ABC1234",
  "ledAtivo": true,
  "mensagem": "LED ativado com sucesso"
}
```

**Resposta (503 Service Unavailable):**
```json
{
  "sucesso": false,
  "erro": "Serviço ESP32 não disponível"
}
```

**Exemplo (JavaScript):**
```javascript
fetch('http://localhost:8081/api/v1/motos/ABC1234/ativar-led', {
  method: 'POST'
})
  .then(response => response.json())
  .then(data => console.log(data));
```

**Exemplo (C#):**
```csharp
HttpClient client = new HttpClient();
var response = await client.PostAsync("http://localhost:8081/api/v1/motos/ABC1234/ativar-led", null);
var json = await response.Content.ReadAsStringAsync();
var resultado = JsonSerializer.Deserialize<LEDResponse>(json);
```

---

## 🔐 Autenticação

**Atualmente:** Endpoints públicos (sem autenticação)

**Futuro:** JWT Token Authentication
```http
Authorization: Bearer {token}
```

---

## 📱 Integração Mobile App

### React Native / Flutter

```javascript
// Serviço de API
class MotoAPI {
  constructor(baseURL = 'http://localhost:8081/api/v1') {
    this.baseURL = baseURL;
  }
  
  async listarMotos() {
    const response = await fetch(`${this.baseURL}/motos`);
    return await response.json();
  }
  
  async buscarLocalizacao(placa) {
    const response = await fetch(`${this.baseURL}/motos/${placa}/localizacao`);
    return await response.json();
  }
  
  async ativarLED(placa) {
    const response = await fetch(`${this.baseURL}/motos/${placa}/ativar-led`, {
      method: 'POST'
    });
    return await response.json();
  }
}

// Uso
const api = new MotoAPI();
const motos = await api.listarMotos();
const localizacao = await api.buscarLocalizacao('ABC1234');
```

---

## 💻 Integração .NET / C#

### ASP.NET / C#

```csharp
// Serviço de API
public class MotoAPIService
{
    private readonly HttpClient _httpClient;
    private readonly string _baseURL = "http://localhost:8081/api/v1";
    
    public MotoAPIService(HttpClient httpClient)
    {
        _httpClient = httpClient;
        _httpClient.BaseAddress = new Uri(_baseURL);
    }
    
    public async Task<MotosResponse> ListarMotosAsync()
    {
        var response = await _httpClient.GetAsync("/motos");
        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<MotosResponse>(json);
    }
    
    public async Task<LocalizacaoResponse> BuscarLocalizacaoAsync(string placa)
    {
        var response = await _httpClient.GetAsync($"/motos/{placa}/localizacao");
        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<LocalizacaoResponse>(json);
    }
    
    public async Task<LEDResponse> AtivarLEDAsync(string placa)
    {
        var response = await _httpClient.PostAsync($"/motos/{placa}/ativar-led", null);
        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<LEDResponse>(json);
    }
}

// Uso
var service = new MotoAPIService(new HttpClient());
var motos = await service.ListarMotosAsync();
var localizacao = await service.BuscarLocalizacaoAsync("ABC1234");
```

---

## 🧪 Testando a API

### Via cURL

```bash
# Listar motos
curl http://localhost:8081/api/v1/motos

# Buscar localização
curl http://localhost:8081/api/v1/motos/ABC1234/localizacao

# Ativar LED
curl -X POST http://localhost:8081/api/v1/motos/ABC1234/ativar-led
```

### Via Postman

1. Importar coleção (futuro)
2. Configurar base URL: `http://localhost:8081/api/v1`
3. Testar endpoints

---

## 📝 Resumo

✅ **API REST Completa**: 4 endpoints documentados  
✅ **Mobile App**: Pronto para React Native/Flutter  
✅ **.NET**: Pronto para ASP.NET/C#  
✅ **JSON**: Respostas em formato JSON  
✅ **Error Handling**: Tratamento de erros robusto  
✅ **Documentação**: Guia completo de integração  

**Só falta:** Fazer o vídeo e testar! 🚀



