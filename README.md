## 💊 Pill-Buddy

**Pill-Buddy**는 사용자의 매일 복용해야 할 약이나 영양제를 쉽게 관리하고, 복용 시간에 맞춰 알림을 받을 수 있도록 돕는 서비스입니다.
약을 잊지 않고 제때 복용하도록 알림을 제공하며, 필요에 따라 보호자에게 복용 여부 확인까지 지원합니다.

> “약 복용을 잊지 않고, 내 건강을 Pill-Buddy와 함께 지키세요!”


### 복용 알림으로 건강을 챙기세요 ✉️

사용자는 자신이 복용하는 약에 대해 **복용 메세지 알림**을 받을 수 있습니다.
15분이 지나도 사용자가 약을 복용하지 않은 경우, **보호자에게 약 복용 확인 알림 메세지**가 전송됩니다.

### 원하는 약 정보를 간편하게 검색하세요 🔍

**e약은요 공공 API**를 통해 약 이름, 회사명, 품목 기준 코드로 원하는 약 정보를 검색할 수 있습니다.
약의 주요 정보와 안전한 복용법을 언제든지 쉽게 확인할 수 있어, 자신이 복용하는 약에 대한 신뢰를 높여줍니다.

### 건강을 함께하는 보호자와의 연결 🤝

**보호자 추가 기능**을 통해 사용자의 약 복용 여부를 확인하고 관리할 수 있습니다.
가족이나 보호자와 함께 복용 정보를 공유함으로써 건강 관리가 보다 체계적으로 이루어질 수 있습니다.

<br>

## 🛠️ 개발 환경

### Backend
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)![Spring](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![SpringSecurity](https://img.shields.io/badge/spring%20security-%230DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)![SpringCloud](https://img.shields.io/badge/spring%20cloud-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![SpringDataJpa](https://img.shields.io/badge/spring%20data%20jpa-%231DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![Swagger](https://img.shields.io/badge/Swagger-0?style=for-the-badge&logo=Swagger&logoColor=white&color=%2385EA2D)![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)![OAuth2](https://img.shields.io/badge/OAuth2-0?style=for-the-badge&logo=auth0&logoColor=white&color=%23000000)

### Frontend
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

### Testing
![Junit5](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)![Mockito](https://img.shields.io/badge/Mockito-E34F26?style=for-the-badge&logo=mega&logoColor=white)![K6](https://img.shields.io/badge/k6-7D64FF?style=for-the-badge&logo=k6&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![H2](https://img.shields.io/badge/H2%20database-00205B?style=for-the-badge&logo=null&logoColor=white)

### Tools
![intellij](https://img.shields.io/badge/IntelliJ_IDEA-00415E.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)![GitHub](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=github&logoColor=white")

<br>

## 💻 프로젝트 구조

<details>
  <summary>프로젝트 구조 보기(눌러서 보기)</summary>

```yml
├── main
│   ├── kotlin
│   │   └── medinine
│   │       └── pill_buddy
│   │           ├── PillBuddyApplication.kt
│   │           │
│   │           ├── domain
│   │           │   ├── medicationApi
│   │           │   │   ├── config
│   │           │   │   │   ├── EntityToDtoMapper.kt
│   │           │   │   │   └── RestTemplateConfig.kt
│   │           │   │   ├── controller
│   │           │   │   │   ├── MedicationApiController.kt
│   │           │   │   │   └── MedicationWebController.kt
│   │           │   │   ├── dto
│   │           │   │   │   ├── JsonForm.kt
│   │           │   │   │   ├── MedicationDTO.kt
│   │           │   │   │   ├── MedicationForm.kt
│   │           │   │   │   └── MyPageImpl.kt
│   │           │   │   ├── entity
│   │           │   │   │   └── Medication.kt
│   │           │   │   ├── repository
│   │           │   │   │   └── MedicationApiRepository.kt
│   │           │   │   └── service
│   │           │   │       └── MedicationApiService.kt
│   │           │   │  
│   │           │   ├── notification
│   │           │   │   ├── controller
│   │           │   │   │   ├── NotificationController.kt
│   │           │   │   │   └── NotificationWebController.kt
│   │           │   │   ├── dto
│   │           │   │   │   ├── NotificationDTO.kt
│   │           │   │   │   └── UpdateNotificationDTO.kt
│   │           │   │   ├── entity
│   │           │   │   │   └── Notification.kt
│   │           │   │   ├── provider
│   │           │   │   │   └── SmsProvider.kt
│   │           │   │   ├── repository
│   │           │   │   │   └── NotificationRepository.kt
│   │           │   │   └── service
│   │           │   │       └── NotificationService.kt
│   │           │   │  
│   │           │   ├── record
│   │           │   │   ├── dto
│   │           │   │   │   └── RecordDTO.kt
│   │           │   │   ├── entity
│   │           │   │   │   ├── Record.kt
│   │           │   │   │   └── Taken.kt
│   │           │   │   ├── repository
│   │           │   │   │   └── RecordRepository.kt
│   │           │   │   └── service
│   │           │   │       ├── RecordService.kt
│   │           │   │       └── RecordServiceImpl.kt
│   │           │   │  
│   │           │   ├── user
│   │           │   │   ├── caregiver
│   │           │   │   │   ├── controller
│   │           │   │   │   │   └── CaregiverController.kt
│   │           │   │   │   ├── entity
│   │           │   │   │   │   └── Caregiver.kt
│   │           │   │   │   ├── repository
│   │           │   │   │   │   └── CaregiverRepository.kt
│   │           │   │   │   └── service
│   │           │   │   │       └── CaregiverService.kt
│   │           │   │   │  
│   │           │   │   ├── caretaker
│   │           │   │   │   ├── controller
│   │           │   │   │   │   └── CaretakerController.kt
│   │           │   │   │   ├── dto
│   │           │   │   │   │   └── CaretakerCaregiverDTO.kt
│   │           │   │   │   ├── entity
│   │           │   │   │   │   ├── Caretaker.kt
│   │           │   │   │   │   └── CaretakerCaregiver.kt
│   │           │   │   │   ├── repository
│   │           │   │   │   │   ├── CaretakerCaregiverRepository.kt
│   │           │   │   │   │   └── CaretakerRepository.kt
│   │           │   │   │   └── service
│   │           │   │   │       ├── CaretakerService.kt
│   │           │   │   │       └── CaretakerServiceImpl.kt
│   │           │   │   │  
│   │           │   │   ├── controller
│   │           │   │   │   ├── AuthController.kt
│   │           │   │   │   └── UserController.kt
│   │           │   │   ├── dto
│   │           │   │   │   ├── JoinDto.kt
│   │           │   │   │   ├── LoginDto.kt
│   │           │   │   │   ├── UserDto.kt
│   │           │   │   │   ├── UserPasswordUpdateDto.kt
│   │           │   │   │   ├── UserType.kt
│   │           │   │   │   └── UserUpdateDto.kt
│   │           │   │   ├── entity
│   │           │   │   │   ├── Role.kt
│   │           │   │   │   └── User.kt
│   │           │   │   │── service
│   │           │   │   │   ├── AuthService.kt
│   │           │   │   │   ├── CustomUserDetails.kt
│   │           │   │   │   ├── MyUserDetailService.kt
│   │           │   │   │   └── UserService.kt
│   │           │   │   │
│   │           │   │   ├── oauth
│   │           │   │   │   ├── constant
│   │           │   │   │   │   ├── KakaoProperty.kt
│   │           │   │   │   │   └── NaverProperty.kt
│   │           │   │   │   ├── controller
│   │           │   │   │   │   └── OAuthController.kt
│   │           │   │   │   ├── dto
│   │           │   │   │   │   ├── KakaoUserResponse.kt
│   │           │   │   │   │   ├── NaverUserResponse.kt
│   │           │   │   │   │   ├── OAuthProfile.kt
│   │           │   │   │   │   └── OAuthTokenResponse.kt
│   │           │   │   │   └── service
│   │           │   │   │       ├── OAuthClient.kt
│   │           │   │   │       ├── SocialLoginService.kt
│   │           │   │   │       ├── UserReader.kt
│   │           │   │   │       ├── kakao
│   │           │   │   │       │   ├── KakaoAuthClient.kt
│   │           │   │   │       │   ├── KakaoClient.kt
│   │           │   │   │       │   └── KakaoProfileClient.kt
│   │           │   │   │       └── naver
│   │           │   │   │           ├── NaverAuthClient.kt
│   │           │   │   │           ├── NaverClient.kt
│   │           │   │   │           └── NaverProfileClient.kt
│   │           │   │   │
│   │           │   │   └── profile
│   │           │   │       ├── controller
│   │           │   │       │   └── ProfileController.kt
│   │           │   │       ├── dto
│   │           │   │       │   └── ProfileUploadDto.kt
│   │           │   │       ├── entity
│   │           │   │       │   └── Image.kt
│   │           │   │       ├── repository
│   │           │   │       │   └── ImageRepository.kt
│   │           │   │       └── service
│   │           │   │           ├── ProfileService.kt
│   │           │   │           └── uploader
│   │           │   │               ├── CaregiverProfileUploader.kt
│   │           │   │               ├── CaretakerProfileUploader.kt
│   │           │   │               └── ProfileUploader.kt
│   │           │   │   │  
│   │           │   │   │  
│   │           │   └── userMedication
│   │           │       ├── controller
│   │           │       │   └── UserMedicationController.kt
│   │           │       ├── dto
│   │           │       │   └── UserMedicationDTO.kt
│   │           │       ├── entity
│   │           │       │   ├── Frequency.kt
│   │           │       │   ├── MedicationType.kt
│   │           │       │   └── UserMedication.kt
│   │           │       ├── repository
│   │           │       │   └── UserMedicationRepository.kt
│   │           │       └── service
│   │           │           ├── UserMedicationService.kt
│   │           │           └── UserMedicationServiceImpl.kt
│   │           └── global
│   │               ├── advice
│   │               │   └── GlobalExceptionHandler.kt
│   │               ├── config
│   │               │   ├── FeignConfig.kt
│   │               │   ├── RedisCacheConfig.kt
│   │               │   ├── RedisConfig.kt
│   │               │   ├── SecurityConfig.kt
│   │               │   └── SwaggerConfig.kt
│   │               ├── entity
│   │               │   └── BaseTimeEntity.kt
│   │               ├── exception
│   │               │   ├── ErrorCode.kt
│   │               │   ├── ErrorResponse.kt
│   │               │   └── PillBuddyCustomException.kt
│   │               ├── jwt
│   │               │   ├── JwtAccessDeniedHandler.kt
│   │               │   ├── JwtAuthenticationEntryPoint.kt
│   │               │   ├── JwtAuthenticationFilter.kt
│   │               │   ├── JwtToken.kt
│   │               │   └── JwtTokenProvider.kt
│   │               ├── redis
│   │               │   └── RedisUtils.kt
│   │               └── util
│   │                   └── UploadUtils.kt
│   │  
│   └── resources
│       ├── application-db.yml
│       ├── application.yml
│       ├── static
│       │   ├── LoginForm.html
│       │   ├── afterLogin.html
│       │   ├── css
│       │   │   └── styles.css
│       │   ├── index.html
│       │   └── js
│       │       └── scripts.js
│       └── templates
│           ├── create_notification.html
│           ├── medication
│           │   ├── medication.html
│           │   ├── medicationList.html
│           │   └── search.html
│           ├── notifications.html
│           └── update_notification.html
└── test
├── kotlin
│   └── medinine
│       └── pill_buddy
│           ├── PillBuddyApplicationTests.kt
│           ├── domain
│           │   ├── medicationApi
│           │   │   ├── controller
│           │   │   │   └── MedicationApiControllerTest.kt
│           │   │   └── service
│           │   │       └── MedicationApiServiceTest.kt
│           │   ├── notification
│           │   │   ├── controller
│           │   │   │   └── NotificationControllerTest.kt
│           │   │   ├── repository
│           │   │   │   └── NotificationRepositoryTest.kt
│           │   │   └── service
│           │   │       └── NotificationServiceTest.kt
│           │   ├── user
│           │   │   ├── caregiver
│           │   │   │   ├── controller
│           │   │   │   │   └── CaregiverControllerTest.kt
│           │   │   │   ├── repository
│           │   │   │   │   └── CaregiverRepositoryTest.kt
│           │   │   │   └── service
│           │   │   │       └── CaregiverServiceTest.kt
│           │   │   ├── caretaker
│           │   │   │   ├── controller
│           │   │   │   │   └── CaretakerControllerTest.kt
│           │   │   │   └── service
│           │   │   │       └── CaretakerServiceImplTest.kt
│           │   │   ├── controller
│           │   │   │   ├── AuthControllerTest.kt
│           │   │   │   └── UserControllerTest.kt
│           │   │   ├── oauth
│           │   │   │   └── service
│           │   │   │       └── KakaoOAuthServiceTest.kt
│           │   │   ├── profile
│           │   │   │   └── service
│           │   │   │       ├── ProfileServiceTest.kt
│           │   │   │       └── uploader
│           │   │   │           ├── CaregiverProfileUploaderTest.kt
│           │   │   │           └── CaretakerProfileUploaderTest.kt
│           │   │   └── service
│           │   │       ├── AuthServiceTest.kt
│           │   │       ├── MyUserDetailServiceTest.kt
│           │   │       └── UserServiceTest.kt
│           │   └── userMedication
│           │       ├── controller
│           │       │   └── UserMedicationControllerTest.kt
│           │       └── service
│           │           └── UserMedicationServiceImplTest.kt
│           └── global
│               └── jwt
│                   └── JwtTokenProviderTest.kt
└── resources
└── application.yml

```

</details>

<br>

## 📑 개발 문서

<details>
  <summary>🔗 ER Diagram</summary>

![ER Diagram](https://github.com/user-attachments/assets/a643e7fe-787c-48e1-ac56-33ba4c9d1914)

</details>

<details>
  <summary>⭐️ Class Diagram</summary>

![Class Diagram](https://github.com/user-attachments/assets/c2af143a-bb44-40f8-b5fe-beec11be6533)

</details>

<details>
  <summary>👨🏻‍💻 Sequence Diagram</summary>

<br>

<details>
  <summary>🔐 로그인</summary>

![로그인 Sequence Diagram](https://github.com/user-attachments/assets/08db32d9-0fe0-4f63-87a3-dcfdc72271fc)

</details>

<details>
  <summary>📲 소셜 로그인</summary>

![소셜 로그인 Sequence Diagram](https://github.com/user-attachments/assets/b3628ebb-a8eb-43d2-83df-1bc5d1d4b40a)

</details>

<details>
  <summary>♻️ 토큰 재발급</summary>

![토큰 재발급 Sequence Diagram](https://github.com/user-attachments/assets/9afa4ff9-86b8-482b-9c17-bcad349e289b)

</details>

<details>
  <summary>💌 알림 전송</summary>

![알림 전송 Sequence Diagram](https://github.com/user-attachments/assets/240e999c-7156-4d40-9485-ee35c06c20c6)

</details>


<details>
  <summary>💊 약 정보 검색</summary>

![약 정보 검색 Sequence Diagram](https://github.com/user-attachments/assets/8d620c59-19e3-420b-aba6-04f9398cd45c)

</details>

</details>


<br>

## 👬 팀 소개
|                                        Backend                                         |                                        Backend                                         |                                        Backend                                         |                                        Backend                                         |                                        Backend                                         |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|
| ![진우](https://github.com/user-attachments/assets/43b44089-e9a1-4e6a-89a1-b1bc9a8e8a4a) | ![홍제](https://github.com/user-attachments/assets/6ab12390-7dd5-46c7-88dd-b808a86de5dd) | ![성겸](https://github.com/user-attachments/assets/d12b5ad8-95b9-4e1c-a207-e99a7a123e38) | ![현우](https://github.com/user-attachments/assets/7e75a7a3-d77b-44bd-8dcb-080378caf6e9) | ![소희](https://github.com/user-attachments/assets/b729e0e6-2724-471f-b544-a31e46d0a0d6) |
|                        [이진우 (팀장)](https://github.com/jinw0olee)                        |                           [안홍제](https://github.com/hongjeZZ)                           |                           [김성겸](https://github.com/xxxkyeom)                           |                           [신현우](https://github.com/Dia2Fan)                            |                          [양소희](https://github.com/MisaSohee)                           |
|   프로젝트 총괄 관리<br>팀원 간 소통 조율<br>보호자 기능 구현   |                 Github 관리<br>회원 관리 기능 구현<br>JWT 인증 로직 구현<br>소셜 로그인 구현                  |                         프로젝트 기획<br>사용자 기능 구현<br>Redis 쿼리 성능 개선                         |               약 정보 검색 기능 구현<br>(e약은요 API)<br>Thymeleaf 화면 구현                |                알림 기능 구현<br>(coolsms API)<br>Thymeleaf 화면 구현                 | 

<br>

## ▶️ 시연 영상

### 알림 관리
![알림-추가-시연](https://github.com/user-attachments/assets/9594af4c-513a-4e04-9864-528f8e3a2c9e)
![알림-수정-시연](https://github.com/user-attachments/assets/1a87d496-55c7-4f33-a244-1e3ef9632a7f)

### 약 정보 검색
![약-정보-검색-시연](https://github.com/user-attachments/assets/88adcaf0-ebb0-461a-88ce-95c754baf141)