@echo off
echo ============================================
echo Iniciando aplicacao Spring Boot
echo ============================================
echo.

echo Compilando projeto...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERRO na compilacao!
    pause
    exit /b 1
)

echo Compilacao concluida com sucesso!
echo.
echo ============================================
echo Iniciando servidor na porta 8081...
echo ============================================
echo.
echo Aguarde alguns segundos para inicializacao...
echo.
echo Acesse: http://localhost:8081
echo H2 Console: http://localhost:8081/h2-console
echo Chatbot: http://localhost:8081/ai/chat
echo.
echo Para parar o servidor, pressione CTRL+C
echo.

call mvn spring-boot:run
pause


