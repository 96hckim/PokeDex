# 📱 PokeDex-MVI
> **PokeAPI를 활용한 오프라인 우선(Offline-first) 포켓몬 도감 안드로이드 프로젝트**

이 프로젝트는 대규모 앱에서도 유지보수와 테스트가 용이하도록 설계된 **MVI (Model-View-Intent)** 아키텍처와 **멀티 모듈(Multi-Module)** 구조를 깊이 있게 학습하고 실무에 적용하기 위해 개발되었습니다.

---

## 🌟 Key Features
- **Pokemon Paging List:** `Paging3` 및 `RemoteMediator`를 활용한 대용량 데이터의 효율적인 무한 스크롤 및 네트워크-로컬 DB 동기화.
- **Search & Filter:** 포켓몬 이름 검색 및 타입별 필터링 기능을 통한 데이터 탐색.
- **Offline Support:** 네트워크 연결 없이도 기존에 로드된 데이터를 탐색할 수 있는 `Room` 기반 로컬 캐싱.
- **Dynamic UI:** 포켓몬 고유 타입에 맞춘 동적 테마 컬러링 및 `Jetpack Compose` 애니메이션을 활용한 스탯 시각화.
- **Favorites:** 관심 있는 포켓몬을 즐겨찾기에 등록하고 관리하는 기능.

---

## 🛠 Tech Stacks
- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarative UI)
- **Architecture:** MVI (Model-View-Intent), Clean Architecture, Multi-Module
- **Build & Infra:** Kotlin DSL, Convention Plugins (build-logic), Version Catalog
- **DI:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit2](https://square.github.io/retrofit/), OkHttp3, Kotlinx Serialization
- **Local Storage:** [Room](https://developer.android.com/training/data-storage/room) (SQLite)
- **Async & Stream:** Coroutines, Flow (StateFlow, SharedFlow)
- **Image Loading:** [Coil3](https://coil-kt.github.io/coil/)
- **Pagination:** Paging3 (RemoteMediator for offline caching)

---

## 🏗 Architecture & Design

### 1. Build Logic & Convention Plugins (Gradle 컨벤션 플러그인)
복잡한 멀티 모듈 프로젝트를 효율적으로 관리하기 위해 `build-logic` 디렉토리에 **Gradle 컨벤션 플러그인**을 직접 구현하여 사용했습니다.
- **설정의 중앙 집중화:** Kotlin, Android, Compose, Hilt, Room 등 반복되는 빌드 설정을 플러그인으로 추출하여 재사용성을 높였습니다.
- **Type-Safe 의존성 관리:** Version Catalog(`libs.versions.toml`)를 활용하여 프로젝트 전체의 라이브러리 버전을 일관되게 관리합니다.
- **모듈별 빌드 스크립트 최적화:** 각 모듈의 `build.gradle.kts` 파일을 간결하게 유지하고, 공통 설정은 컨벤션 플러그인에 위임하여 유지보수성을 극대화했습니다.

### 2. MVI (Model-View-Intent)
예측 가능한 UI 상태 관리를 위해 단방향 데이터 흐름(UDF)을 보장하고 MVVM에서 발생할 수 있는 State Fragment(상태 파편화) 문제를 해결하고 UI 상태의 예측 가능성을 보장하기 위해 도입했습니다.
- **State:** UI의 모든 상태를 단일 진실 공급원(SSOT)으로 관리하여 불일치 문제 해결.
- **Intent:** 사용자의 액션을 캡슐화하여 비즈니스 로직으로 명확히 전달.
- **Effect:** 네비게이션, 스낵바 출력 등 일회성 이벤트를 사이드 이펙트로 분리 처리.
- **Base Interface:** `UiState`, `UiIntent`, `UiEffect` 인터페이스를 통한 일관된 ViewModel 구조 정의.
- **[Data Flow]** `User Intent` ➔ `ViewModel` ➔ `Repository` ➔ `UiState Update` ➔ `UI Rendering`

### 3. Multi-Module Structure
관심사 분리(SoC)와 빌드 속도 최적화를 위해 기능 및 레이어별로 모듈 간 결합도를 낮추고, 병렬 빌드를 통한 빌드 속도 최적화 및 기능 단위의 독립적인 테스트 환경을 구축했습니다.
- `:app`: 모든 모듈을 통합하고 의존성 주입의 진입점 역할.
- `:feature:*`: 기능 단위 모듈 (List, Detail, Favorites). UI 로직 및 ViewModel 포함.
- `:core:data`: 리포지토리 패턴을 통해 데이터 소스(Network, Database) 관리.
- `:core:domain`: 비즈니스 로직 및 UseCase 정의.
- `:core:network` / `:core:database`: 외부 데이터 통신 및 로컬 영속성 관리.
- `:core:ui`: 공통 UI 컴포넌트 및 테마 정의.

---

## 🚀 Technical Challenges & Learnings

### 1. 효율적인 오프라인 캐싱 (RemoteMediator)
단순한 네트워크 통신을 넘어, 사용자가 네트워크가 불안정한 환경에서도 끊김 없는 경험을 할 수 있도록 `Paging3`의 `RemoteMediator`를 도입했습니다. 이를 통해 네트워크 데이터를 로컬 DB에 우선 저장하고, UI는 오직 DB만을 관찰하는 구조를 구현했습니다.

### 2. 고성능 이미지 로딩 및 최적화
포켓몬 도감 특성상 수많은 이미지를 로드해야 합니다. `Coil3`를 활용하여 이미지 메모리 캐싱 및 디스크 캐싱을 설정하고, 리스트 스크롤 시 발생할 수 있는 버벅임을 최소화했습니다.

### 3. 멀티 모듈 간의 네비게이션
Compose Navigation을 멀티 모듈 환경에서 유연하게 관리하기 위해 각 모듈별로 라우팅 로직을 분리하고, `:app` 모듈에서 이를 통합하는 방식을 취하여 모듈 간 의존성을 낮췄습니다.

---

## 📸 Screenshots

| 포켓몬 리스트 | 상세 정보 | 즐겨찾기 |
| :---: | :---: | :---: |
| <img src="screenshots/list_screen.png" width="250"/> | <img src="screenshots/detail_screen.png" width="250"/> | <img src="screenshots/favorites_screen.png" width="250"/> |

---

## 🛠 Getting Started
1. 이 저장소를 클론합니다.
2. Android Studio (Ladybug 이상 권장)에서 프로젝트를 엽니다.
3. Gradle Sync를 완료한 후 `app` 모듈을 실행합니다.
