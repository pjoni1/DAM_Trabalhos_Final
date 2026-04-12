# 📷 App de Imagens (MIP-2)

## Application Purpose
Esta aplicação é um visualizador de imagens desenvolvido em **Kotlin** e interfaces nativas baseadas em XML Views. O projeto segue os requisitos da metodologia MIP-2, adotando a arquitetura \*\*MVVM (Model-View-ViewModel)\*\* em conjunto com princípios de *Clean Architecture*. O objetivo consiste em apresentar aos utilizadores uma lista fluída de fotografias dinâmicas suportada por práticas modernas de programação assíncrona.

## API Used
A aplicação recolhe a informação através de chamadas de rede para a [Unsplash API](https://unsplash.com/developers) (ou API REST de listagem de imagens equivalente). O consumo desta API baseada em JSON é gerido internamente pelo cliente HTTP **Retrofit**.

## Screenshots
*(Substituir pelos screenshots reais no final do desenvolvimento)*
<!-- ![Screenshot Loading Visual](docs/images/screenshot_loading.png) -->
<!-- ![Screenshot Feeds de Imagem](docs/images/screenshot_feed.png) -->
- _Placeholder: Ecrã de Loading e Inicialização._
- _Placeholder: Ecrã Principal exibindo a listagem preenchida com a Grelha e as fotos respetivas._
- _Placeholder: Ecrã em caso de falha de servidor/conectividade._

## Instructions for running the project
1. Faça Clone deste repositório para a sua máquina.
2. Abra a diretoria principal do projeto utilizando o **Android Studio**.
3. Adquira a sua *API Key / Client ID* no portal de programadores da Unsplash e insira-o nas configurações da API para fins de autenticação.
4. Aguarde até o mecanismo do Gradle sincronizar todas as dependências requeridas (como *Retrofit*, *Coil/Glide*, etc).
5. Certifique-se de ter um emulador com acesso à internet em execução ou ligue um telemóvel nativo em modo de programador via USB.
6. Clique no botão de build (**Run 'app'**) do ambiente de desenvolvimento.
