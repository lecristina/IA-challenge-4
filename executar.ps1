Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Iniciando aplicacao Spring Boot" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se Maven está instalado
$mvnCheck = & mvn -version 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Maven nao encontrado!" -ForegroundColor Red
    Write-Host "Por favor, instale o Maven e adicione ao PATH" -ForegroundColor Red
    exit 1
}

Write-Host "Compilando projeto..." -ForegroundColor Yellow
& mvn clean compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "Erro na compilacao!" -ForegroundColor Red
    exit 1
}

Write-Host "Compilacao concluida com sucesso!" -ForegroundColor Green
Write-Host ""

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Iniciando servidor na porta 8081..." -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Aguarde alguns segundos para inicializacao..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Acesse: http://localhost:8081" -ForegroundColor Green
Write-Host "H2 Console: http://localhost:8081/h2-console" -ForegroundColor Green
Write-Host "Chatbot: http://localhost:8081/ai/chat" -ForegroundColor Green
Write-Host ""
Write-Host "Para parar o servidor, pressione CTRL+C" -ForegroundColor Yellow
Write-Host ""

# Executar Spring Boot usando Maven (mais confiável)
& mvn spring-boot:run


