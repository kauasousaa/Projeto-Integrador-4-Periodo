# **SOS-Rota**

Sistema acadêmico desenvolvido para gerenciamento e despacho de atendimentos de emergência, seguindo as especificações definidas na documentação oficial do projeto. O objetivo é apoiar a regulação, o gerenciamento de recursos e o cálculo otimizado de rotas para atendimentos rápidos e eficientes.

---

## **Descrição Geral**

O SOS-Rota permite:

* Cadastro e gerenciamento de ambulâncias, equipes, profissionais e bases
* Registro e acompanhamento de ocorrências
* Despacho otimizado utilizando cálculo de menor caminho em um grafo
* Importação da topologia urbana (bairros e conexões) por arquivos CSV
* Integração completa com banco de dados relacional
* Interface desktop conforme requisitos do projeto acadêmico

---

## **Tecnologias Utilizadas**

### **Backend**

* Java
* PostgreSQL
* Processamento de arquivos CSV
* Algoritmos de menor rota (grafo)

### **Frontend**

* Interface desktop
* Java Swing ou JavaFX (dependendo da implementação escolhida)

---

## **Banco de Dados**

O sistema utiliza **PostgreSQL** como banco de dados principal, conforme definido nos requisitos do projeto.

Principais pontos sobre o banco:

* Banco relacional utilizado para garantir integridade e consistência
* Armazena todos os cadastros e dados operacionais
* Estruturas principais:

  * Ambulâncias
  * Bases
  * Profissionais
  * Equipes
  * Ocorrências
  * Atendimentos
  * Bairros (vértices)
  * Conexões entre bairros (arestas e distâncias em km)
* Topologia urbana carregada via CSV e persistida para uso no cálculo de rotas
* Suporte a consultas operacionais e relatórios

---

## **Principais Funcionalidades**

* Cadastro de ambulâncias, equipes, profissionais, bases e ocorrências
* Despacho baseado em:

  * menor rota no grafo
  * tempo estimado
  * disponibilidade da ambulância
  * tipo do veículo
* Importação de bairros e conexões via arquivos CSV
* Histórico de atendimentos
* Relatórios operacionais
* Controle de status e disponibilidade de recursos

---

## **Arquitetura**

* Estrutura modular seguindo boas práticas de organização
* Separação clara entre:

  * Domínio
  * Aplicação
  * Persistência
  * Interface gráfica
* Integração com PostgreSQL para armazenamento persistente

---
