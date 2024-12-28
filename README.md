# kafka-user

Kafka MSA 구현의 시작점인 kafka-user 프로젝트입니다.<br>
현재 Kafka와 gRPC를 공부하며 코드에 담아내고 있습니다.<br>
간단한 API만 존재하지만, ✨ 내부 로직을 정확히 이해하고 제대로 구축한다면 ✨<br>
다른 API를 추가하는 것은 어렵지 않을 것이라 생각합니다.<br>
<br>

### 🌊 이 프로젝트의 로직은 다음과 같습니다.

1. 사용자 회원가입
2. Event 발행
3. Outbox Entity 저장 및 Kafka 메시지 발행
4. 발행한 Kafka 메시지를 내부적으로 소비하여 정상 작동 확인 및 Outbox Processed 처리
5. Consumer 측(다른 MSA)에서 gRPC를 통해 데이터 확인 시, <br>
   Outbox Success 처리하여 데이터 동기화 종료
6. 이후에는 다른 MSA(kafka-post)에서 데이터 동기화
