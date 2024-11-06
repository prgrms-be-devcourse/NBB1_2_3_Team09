# 💊 Pill-Buddy

Pill-Buddy는 사용자가 매일 복용해야 할 약이나 영양제를 관리하고, 복용 시간에 맞춰 알림을 받을 수 있도록 돕는 서비스입니다.<br> 사용자는 보호자를 추가하여 약 복용 여부를 확인하고 관리할 수 있으며, [식품의약품안전처](https://www.mfds.go.kr/index.do)의 공공 API 를 통해 약의 주요 정보들을 조회할 수 있습니다.


<br>

## 🛠️ 개발 환경

### Backend
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)![Spring](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![SpringSecurity](https://img.shields.io/badge/spring%20security-%230DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)![SpringCloud](https://img.shields.io/badge/spring%20cloud-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![SpringDataJpa](https://img.shields.io/badge/spring%20data%20jpa-%231DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)![Junit5](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

### Frontend
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

### Tools
![intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)![GitHub](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=github&logoColor=white")

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
|                                        Backend                                         |                                        Backend                                         |                           Backend                            |                           Backend                            |                           Backend                            |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------------:|
| ![진우](https://github.com/user-attachments/assets/43b44089-e9a1-4e6a-89a1-b1bc9a8e8a4a) | ![홍제](https://github.com/user-attachments/assets/6ab12390-7dd5-46c7-88dd-b808a86de5dd) | ![성겸](https://github.com/user-attachments/assets/d12b5ad8-95b9-4e1c-a207-e99a7a123e38) | ![현우](https://github.com/user-attachments/assets/7e75a7a3-d77b-44bd-8dcb-080378caf6e9) | ![소희](https://github.com/user-attachments/assets/b729e0e6-2724-471f-b544-a31e46d0a0d6) |
|                        [이진우 (팀장)](https://github.com/jinw0olee)                        |                           [안홍제](https://github.com/hongjeZZ)                           |      [김성겸](https://github.com/xxxkyeom)      |      [신현우](https://github.com/Dia2Fan)      |      [양소희](https://github.com/MisaSohee)      |
|   프로젝트 총괄 관리<br>팀원 간 소통 조율<br>보호자 기능 구현   |                       Github 관리<br>회원 관리 기능 구현<br>JWT 인증 로직 구현                       | 프로젝트 기획<br>사용자 기능 구현 | 프로젝트 기획<br>e약은요 API를 통해 약 정보 검색 기능 구현 | 프로젝트 기획<br>coolsms API를 통해 알림 기능 구현 | 

