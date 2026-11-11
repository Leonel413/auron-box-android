# Auron Box — Loja de Eletrônicos

E-commerce de eletrônicos com **backend Spring Boot + MySQL**, **site web** (HTML/CSS/JS) e **aplicativo Android nativo** (Java). O site e o app usam a mesma API REST, com autenticação JWT.

```
 ┌────────────────┐        HTTP :8080        ┌─────────────────────┐        :3306        ┌─────────┐
 │  App Android   │ ───────────────────────► │  Backend Spring Boot │ ──────────────────► │  MySQL  │
 │  Site (browser)│ ◄─────────────────────── │  (roda no PC)        │ ◄────────────────── │         │
 └────────────────┘                          └─────────────────────┘                     └─────────┘
```

O app **nunca** acessa o banco diretamente: tudo passa pelo backend. Se o backend estiver desligado, o app mostra "Não foi possível conectar ao servidor".

---

## 1. Pré-requisitos (instalar uma vez)

| Item | Versão | Para quê |
|---|---|---|
| JDK | 17 ou superior (testado com 21) | rodar o backend |
| Maven | 3.9.x | compilar e rodar o backend |
| MySQL | 8.x | banco de dados |
| Android Studio | versão recente | compilar e rodar o app |

> Conferir o Java: `java -version` (precisa mostrar 17 ou superior). Java 8 **não** funciona.

### Maven no Windows
O projeto não traz a pasta `.mvn` (Maven Wrapper), então use o Maven instalado no PC:

1. Baixe `apache-maven-3.9.9-bin.zip` em <https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/>.
2. Extraia em `C:\maven` (o resultado é `C:\maven\apache-maven-3.9.9`).

---

## 2. Banco de dados (MySQL)

1. Deixe o MySQL ligado (serviço do Windows ou MySQL Workbench aberto).
2. Abra `src/main/resources/application.properties` e ajuste a conexão:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bddlojatst?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_DO_MYSQL

senha.sistema=SENHA_QUE_VOCE_ESCOLHER
upload.caminho=src/main/resources/static/uploads
```

- `createDatabaseIfNotExist=true` cria o banco `bddlojatst` sozinho na primeira execução, e o Hibernate cria as tabelas (`ddl-auto=update`).
- `senha.sistema` é a senha que o admin digita na tela **Usuários** para promover ou remover admins.

### (Opcional) Carregar dados de exemplo
O arquivo `database/database.sql` tem produtos, um usuário admin e pedidos de exemplo. No MySQL Workbench:
**Server → Data Import → Import from Self-Contained File** → escolha `database/database.sql` → em **Default Target Schema** selecione `bddlojatst` → **Start Import**.

> O script apaga e recria as tabelas que contém. Rode a importação **depois** de a primeira execução do backend ter criado o banco, ou crie o schema antes.

---

## 3. Ligar o servidor (backend) no PC

O backend escuta na **porta 8080** (padrão do Spring Boot, não está alterada no projeto).

### Opção A — script (mais fácil)
1. Abra `iniciar-backend.bat` no Bloco de Notas e confira se os três caminhos batem com o seu PC:
   - `JAVA_HOME` (JDK 17+; o padrão aponta para o JDK embutido do Android Studio),
   - a pasta do Maven (`C:\maven\apache-maven-3.9.9\bin`),
   - a pasta do projeto (`cd /d ...`).
2. Dê dois cliques em `iniciar-backend.bat`.
3. **Deixe a janela aberta.** Fechar a janela desliga o servidor.

### Opção B — PowerShell
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:Path = "$env:JAVA_HOME\bin;C:\maven\apache-maven-3.9.9\bin;$env:Path"
cd C:\caminho\do\projeto\auron-box-androidtst
mvn spring-boot:run
```
(Os dois primeiros comandos valem só para a janela atual; repita ao abrir outra.)

### Como saber que subiu
O console mostra, no final:
```
Tomcat started on port 8080 (http)
Started AuronboxApplication in X seconds
```

### Testar
| O que | Endereço |
|---|---|
| API (deve devolver JSON) | <http://localhost:8080/produtos> |
| Site web | <http://localhost:8080> |
| Painel admin web | <http://localhost:8080/menuGestao.html> |

Para parar o servidor: **Ctrl+C** na janela dele.

---

## 4. Portas e rede

| Porta | Quem usa | Observação |
|---|---|---|
| **8080** | Backend (API + site) | Precisa estar livre e liberada no firewall se for usada por outro dispositivo |
| **3306** | MySQL | Só o backend acessa; não precisa abrir para os celulares |

### Trocar a porta do backend
Em `application.properties`, adicione `server.port=8081` (e use 8081 nos endereços e na URL do app).

### Porta 8080 já em uso
```powershell
netstat -ano | findstr :8080
taskkill /PID NUMERO_DO_PID /F
```
(ou mude a porta como acima).

### Liberar a porta 8080 no firewall do Windows
Só é necessário para **celular físico** ou outro PC acessarem. PowerShell **como administrador**:
```powershell
netsh advfirewall firewall add rule name="Auron Box 8080" dir=in action=allow protocol=TCP localport=8080
```

### Descobrir o IP do PC
```powershell
ipconfig
```
Use o **Endereço IPv4** da placa Wi-Fi/Ethernet (ex.: `192.168.0.10`).

---

## 5. Aplicativo Android

O projeto do app está na pasta `android/`.

1. No Android Studio: **File → Open** e escolha a pasta **`android/`** (não a raiz).
2. Aguarde o **Gradle sync** terminar. Se pedir o caminho do SDK, crie `android/local.properties` (veja `android/local.properties.example`):
   ```properties
   sdk.dir=C\:\\Users\\SEU_USUARIO\\AppData\\Local\\Android\\Sdk
   ```
3. **Backend ligado** (seção 3).
4. Escolha um emulador (Device Manager) ou um celular e clique em **▶ Run** com o módulo `app`.

### URL da API (qual endereço o app usa)

| Onde o app roda | URL | Configuração |
|---|---|---|
| **Emulador** no mesmo PC | `http://10.0.2.2:8080/` | Nenhuma (é o padrão). `10.0.2.2` é o "localhost do PC" visto de dentro do emulador |
| **Celular físico** (mesmo Wi-Fi do PC) | `http://IP_DO_PC:8080/` | Adicione no `android/local.properties`: `API_BASE_URL=http://192.168.0.10:8080/` e rode o app de novo. Libere a porta 8080 no firewall |
| Backend hospedado na internet | `https://seu-dominio/` | Mesma propriedade `API_BASE_URL`, com HTTPS |

- A URL sempre termina com `/`.
- Build **debug** aceita HTTP; build **release** exige HTTPS.
- Se mudar `API_BASE_URL`, faça **Build → Rebuild Project** ou rode o app de novo.

### Testar a conexão antes do app
No navegador do emulador/celular, abra `http://10.0.2.2:8080/produtos` (emulador) ou `http://IP_DO_PC:8080/produtos` (celular). Se mostrar JSON, o app consegue conectar.

---

## 6. Acesso de administrador

- **No site:** <http://localhost:8080/loginAdmin.html>.
- **No app:** faça login com uma conta admin, abra a aba **Perfil** e toque em **Área administrativa** (o botão só aparece para admins). Lá ficam **Produtos**, **Pedidos** e **Usuários**.

Para tornar um usuário admin:
- pela tela **Usuários** (site ou app), buscando o e-mail e informando a `senha.sistema`; ou
- direto no MySQL: `UPDATE usuarios SET admin = 1 WHERE email = 'seu@gmail.com';` (saia e entre de novo no app depois).

---

## 7. Problemas comuns

| Sintoma | Causa provável | Solução |
|---|---|---|
| App: "Não foi possível conectar ao servidor" | Backend desligado ou URL errada | Ligue o backend (seção 3); confira a URL (seção 5); teste `/produtos` no navegador |
| Navegador do emulador: `ERR_CONNECTION_REFUSED` | Nada escutando na 8080 | Backend não subiu; veja o console |
| Celular físico não conecta (timeout) | Firewall ou rede diferente | Libere a porta 8080; PC e celular no mesmo Wi-Fi; use o IP do PC |
| `JAVA_HOME not found` | Java não configurado | Configure `JAVA_HOME` (seção 3) |
| `NoSuchFieldError ... JCTree$JCImport` | Lombok antigo com Java 21 | O `pom.xml` já define `<lombok.version>1.18.34</lombok.version>`; confira se está lá |
| `Access denied for user 'root'` | Senha do MySQL errada | Corrija `spring.datasource.password` |
| `Communications link failure` / `Connection refused` (banco) | MySQL desligado ou porta 3306 bloqueada | Ligue o MySQL; confira host e porta |
| `Port 8080 was already in use` | Outro programa na porta | Seção 4 |
| Android Studio: muitos erros vermelhos no manifest | Projeto não sincronizado, ou aberto na pasta errada | Abra a pasta `android/` e aguarde o Gradle sync |
| Sessão expirada no app | Token JWT dura 1 hora | Entre de novo |

---

## 8. Estrutura do projeto

```
auron-box-androidtst/
├── pom.xml
├── iniciar-backend.bat
├── database/database.sql            # dump com dados de exemplo
├── src/main/
│   ├── java/com/auronbox/auronbox/
│   │   ├── config/                  # Security, ModelMapper, WebConfig
│   │   ├── controller/              # endpoints REST
│   │   ├── dto/  exception/  model/  repository/  security/  service/
│   └── resources/
│       ├── application.properties
│       └── static/                  # site web (HTML, CSS, JS) e uploads/ (imagens)
└── android/                         # app Android (Java, MVVM)
    └── app/src/main/
        ├── java/com/auronbox/app/
        │   ├── data/                # model, remote (Retrofit/OkHttp), repository, local (sessão)
        │   ├── viewmodel/
        │   └── ui/                  # main, home, detail, cart, orders, profile, admin
        └── res/                     # layouts, navegação, ícones, temas
```

### Principais endpoints
| Recurso | Rotas |
|---|---|
| Produtos | `GET /produtos`, `/produtos/{id}`, `/produtos/categoria/{c}`, `/produtos/busca/{texto}`, `/produtos/valorAsc`, `/produtos/valorDesc`; `POST /produtos`, `/produtos/img/{id}`; `DELETE /produtos/{id}` |
| Usuários | `POST /usuarios`, `/usuarios/email/{email}/senha/{senha}`; `GET /usuarios/{email}`; `PUT /usuarios/{id}`, `/usuarios/{id}/admin` |
| Carrinho | `GET /carrinhos/{id}`; `POST/PUT/DELETE /carrinhos/produto/{id}/usuario/{id}` |
| Pedidos | `GET /pedidos`, `/pedidos/cliente/{id}`, `/pedidos/pedido/{id}`; `POST /pedidos/cliente/{id}/formaPagamento/{f}/quantidadeParcelas/{n}`; `PUT /pedidos/pedido/{id}/acao/{a}/motivo/{m}` |

---

## 9. Segurança (repositório privado)

- `application.properties` contém a senha do MySQL e a `senha.sistema`. Mantenha o repositório **privado** e nunca publique esse arquivo em um repositório público.
- `android/local.properties` (caminho do SDK e URL da sua máquina) já está no `.gitignore` e não vai para o GitHub.
- O login do backend recebe e-mail e senha no caminho da URL (`/usuarios/email/{email}/senha/{senha}`). Em produção, use HTTPS.

---

## Autor

Desenvolvido por **Ian** — Estudante de Sistemas de Informação, Centro Universitário Projeção.
