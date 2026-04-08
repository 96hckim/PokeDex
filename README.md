# 📱 PokeDex-MVI
> **PokeAPI를 활용한 현대적인 안드로이드 기술 스택 학습 및 아키텍처 설계 프로젝트**

단순한 데이터 조회를 넘어, 대규모 앱에서도 유지보수 가능한 **MVI(Model-View-Intent)** 아키텍처와 **Jetpack Compose**의 선언형 UI를 깊이 있게 다루는 것을 목표로 합니다.

---

## 🛠 Tech Stacks
* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** MVI (Model-View-Intent)
* **DI:** Hilt
* **Async:** Coroutines & Flow
* **Network:** Retrofit2 & OkHttp3
* **Local Storage:** Room
* **Image Loading:** Coil

---

## 🚀 Key Features
* **Paging List:** `Paging3`를 활용한 대용량 포켓몬 데이터의 효율적 로딩
* **Detail View:** 포켓몬 타입별 동적 테마 적용 및 스탯 시각화
* **Offline Mode:** `Room`을 활용한 데이터 로컬 캐싱 및 오프라인 접근 지원 (예정)
* **Search & Filter:** 포켓몬 이름 검색 및 타입별 필터링 기능

---

## 🏗 Architecture (MVI)
이 프로젝트는 예측 가능한 상태(State) 관리와 단방향 데이터 흐름(UDF)을 위해 **MVI** 패턴을 채택했습니다.
* **Intent:** 사용자 액션을 캡슐화하여 상태 변화의 의도를 명확히 함
* **State:** UI의 모든 상태를 하나의 불변 객체로 관리하여 사이드 이펙트 최소화
* **SideEffect:** 네비게이션, 토스트 메시지 등 일회성 액션을 효율적으로 처리

---

## 📅 Roadmap & Progress
- [x] 프로젝트 기초 설계 및 Hilt 세팅
- [ ] PokeAPI 연동 및 데이터 레이어 구현
- [ ] Paging3 기반 포켓몬 리스트 UI
- [ ] 멀티 모듈 리팩터링 및 build-logic 도입 (진행 예정)
