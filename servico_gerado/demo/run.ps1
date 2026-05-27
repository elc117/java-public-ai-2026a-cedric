# Compila e roda o servidor sem precisar de mvn no PATH.
# Usa as dependencias ja baixadas em ~/.m2/repository.

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$m2 = "$env:USERPROFILE\.m2\repository"
if (-not (Test-Path $m2)) {
    Write-Error "Pasta $m2 nao existe. Rode 'mvn dependency:resolve' uma vez ou abra o projeto no VS Code (Java extension)."
}

$pattern = "javalin|slf4j|sqlite-jdbc|jackson|jetty|annotations|kotlin-stdlib"
$jars = Get-ChildItem -Path $m2 -Recurse -Filter "*.jar" |
        Where-Object { $_.FullName -match $pattern } |
        Select-Object -ExpandProperty FullName

if ($jars.Count -eq 0) {
    Write-Error "Nenhum jar de dependencia encontrado em $m2. Resolva as dependencias do pom.xml primeiro."
}

New-Item -ItemType Directory -Force "build\classes" | Out-Null

Write-Host "Compilando..." -ForegroundColor Cyan
$srcFiles = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" |
            Select-Object -ExpandProperty FullName
$cp = $jars -join ";"
& javac -d "build\classes" -cp "$cp" $srcFiles
if ($LASTEXITCODE -ne 0) { Write-Error "Falha ao compilar." }

Write-Host "Iniciando servidor em http://localhost:3000 ..." -ForegroundColor Green
$runCp = "build\classes;src\main\resources;$cp"
& java -cp "$runCp" com.example.App
