# Auron Box – App Android (Java, MVVM)

Consome o backend Spring Boot existente (pasta raiz do repositório). Não acessa o banco diretamente.

## Backend
Na raiz do projeto: `./mvnw spring-boot:run` (porta 8080).

## URL da API
- Emulador: `http://10.0.2.2:8080/` (padrão, nada a configurar).
- Celular físico (mesmo Wi-Fi do PC): crie `android/local.properties` com `API_BASE_URL=http://IP_DO_PC:8080/` e libere a porta 8080 no firewall.
- O build debug permite HTTP (cleartext); o release exige HTTPS.

## Executar
Abra a pasta `android/` no Android Studio (JDK 17+), aguarde o Gradle sync e execute o módulo `app`.

## Estrutura
`data/model` DTOs · `data/remote` Retrofit/OkHttp · `data/repository` repositórios · `viewmodel` ViewModels · `ui/*` Fragments/Activities · `res/navigation` grafos.
