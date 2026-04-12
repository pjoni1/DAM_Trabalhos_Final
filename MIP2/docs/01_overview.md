# Overview

## Purpose of the Application
O principal propósito desta aplicação é atuar como uma galeria remota intuitiva para leitura, pesquisa e visualização em grelha de imagens. O projeto existe em contexto estrito do desafio MIP-2, servindo de elemento de prova na construção robusta de uma aplicação Android fundamentada nos princípios nativos de linguagem Kotlin.

## Target Users
O público-alvo inicial (Target Users) contempla:
- **Avaliadores e Docentes**, focados na verificação rigorosa da conformidade com a estrutura de arquitetura *Clean Architecture* e *MVVM*.
- **Entusiastas Visuais e Finais**, que desejem explorar acervos de fotografias provindenciados dinamicamente com uma experiência responsiva e sem descontinuidades na interface móvel.

## General Idea of How the System Works
O sistema inicia o seu percurso na interação do ecrã principal suportado por **XML Views** alojadas numa *Activity* nativa. Quando o ecrã se inicializa, ele notifica a estrutura **ViewModel**, encarregue de isolar regras de navegação visual do código lógico.

O modelo logístico encaminhará através do *Repository* uma chamada de rede para a APIREST pública de imagens suportada assincronamente por cliente de arquitetura HTTP (**Retrofit**) acompanhado do sistema de **Coroutines**. Consoante a resposta do servidor, o interface comutará os seus observadores visuais refletindo ativamente três panoramas base:
1. Apresenta o sinal de carregamento processual;
2. Monta cada fotografia num painel infinito otimizado com a **RecyclerView** e uma ferramenta eficaz de download individual de pixels;
3. Exibirá em alternativa uma mensagem textual de erro/falha por obstrução do servidor ou rede externa.
