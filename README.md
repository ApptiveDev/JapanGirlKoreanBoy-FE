# JapanGirlKoreanBoy-FE (Android)

한국 남성과 일본 여성을 이어주는 소개팅 서비스 **"앤(縁)"** 의 안드로이드 앱 프런트엔드입니다. 언어 선택 → 로그인/회원가입 → 필수 정보 입력 → 완료까지의 온보딩 흐름을 Jetpack Compose로 구현했습니다.

## 개발자
- Android: 이병찬, 이예람

## 주요 기능
- **언어 선택 랜딩**: 한국어/일본어 진입점에서 로그인 화면으로 이동.
- **이메일 로그인·회원가입**: 이메일/비밀번호 인증, 인증코드 전송·검증, 가입 완료 후 로그인 화면 이동.
- **소셜 로그인**: Google Credential Manager + Google ID 토큰으로 간편 로그인.
- **필수정보 5단계 설문**: 성별 선택, 신체/거주/습관/종교, 학력·자산·자기소개, 프로필 사진 최소 2장 업로드(프리사인드 URL PUT 업로드), 선호 조건(키 범위, 종교/직업/자산/외모/MBTI/우선순위) 입력 후 `/members/me/preferences` 전송.
- **상태 관리**: `DataStore`로 사용자 정보·토큰 저장, `TokenProvider`를 통한 인메모리 토큰 주입, 커스텀 토스트/로딩 다이얼로그 제공.

## 기술 스택
- Kotlin, Android Gradle Plugin 8.12.3, Kotlin 2.0.21, compileSdk 36 / minSdk 33
- Jetpack Compose (Material3, Navigation Compose), Coil
- Android Credentials API, Google ID
- Retrofit2 + OkHttp, Gson, DataStore Preferences
- 프로젝트 루트: `app` 싱글 모듈

## 프로젝트 구조 (요약)
```
app/src/main/java/com/apptive/japkor/
  MainActivity.kt               # Compose 엔트리, 토스트 컨테이너
  navigation/AppNavHost.kt      # Language → Login → RequiredInfo → Complete → SignUp 네비게이션
  data/api/                     # Auth/RequiredInfo Retrofit 인터페이스, ApiClient, AuthInterceptor
  data/repository/              # AuthRepository, RequiredInfoRepository(ApiResult/업로드)
  data/local/                   # DataStoreManager, TokenProvider
  ui/                           # 화면별 Compose UI + ViewModel, 공용 컴포넌트/테마
  utils/required_info/          # 한글 라벨 → 서버 코드 매핑 헬퍼
```

## 환경 설정
- `local.properties`에 백엔드 주소를 추가합니다.
  ```
  BASE_URL=https://api.example.com/
  ```
- Google 로그인은 `app/src/main/res/values/strings.xml`의 `default_web_client_id`를 사용합니다. 필요 시 발급받은 클라이언트 ID로 교체하세요.

## 빌드 & 실행
1) Android Studio Ladybug+ (JDK 11)에서 열기  
2) 위 `BASE_URL`을 설정한 뒤 에뮬레이터/실기기 선택  
3) 명령행 빌드: `./gradlew assembleDebug` 또는 IDE에서 `Run 'app'`

## API 통신 요약
- 인증: `POST sign-in`, `POST sign-up`, `POST email-code/send`, `POST email-code/verify`
- 필수정보: `POST members/me/preferences`
- 이미지: `POST image/presigned-url`, `POST image/presigned-url/list` 후 프리사인드 URL `PUT` 업로드
