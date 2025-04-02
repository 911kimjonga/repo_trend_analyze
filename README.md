# 0. 네이버 데이터랩 검색어 트렌드 분석기 프로젝트

이 프로젝트는 Kotlin의 특장점(DSL, 멀티 모듈, 코루틴, 확장 함수, sealed class 등)을 활용하여 **네이버 데이터랩 검색어 트렌드 분석기**를 구현하는 것을 목표로 합니다.

## 📦 전체 아키텍처 구성

- api               # 웹 진입점, 라우터 DSL, 의존성 주입
- auth              # 인증 및 인가 로직 (JWT, OAuth2 등)
- domain            # 핵심 모델 (KeywordGroup, TrendResult 등)
- service           # 비즈니스 로직 (분석, 결과 가공)
- client-datalab    # 네이버 오픈 API 연동 (WebClient + 코루틴)
- common            # DSL, 확장 함수, 공통 응답 구조 등
- build.gradle.kts  # 루트 빌드 설정
---

## 🧩 모듈별 책임

| 모듈               | 책임                                               | 핵심 기술                   |
|--------------------|----------------------------------------------------|-----------------------------|
| **api**            | 진입점, 라우팅 설정, Controller 대신 coRouter 사용 | Kotlin DSL, WebFlux         |
| **domain**         | `KeywordGroup`, `TrendQuery`, `TrendResult` 모델 정의 | `data class`, `sealed class`|
**auth**           | 인증 및 인가 처리, JWT/OAuth2 구현 및 인증 미들웨어 제공         | Spring Security, JWT/OAuth2   |
| **service**        | 분석 로직 처리 및 client와의 연결                   | Service Layer 설계          |
| **client-datalab** | 네이버 검색어 트렌드 API 호출                      | 코루틴, WebClient           |
| **common**         | DSL 빌더, 확장 함수, API 응답 래퍼 등                | Kotlin DSL, 확장 함수       |

---


# 🛡️ 1. 인증 및 인가 모듈 (Authentication & Authorization)

이 모듈은 **사용자 인증(Authentication) 및 권한 인가(Authorization)** 기능을 제공합니다.  
Spring Security 기반으로 **JWT 토큰 인증**, **Role 기반 접근 제어**를 구현하였습니다.