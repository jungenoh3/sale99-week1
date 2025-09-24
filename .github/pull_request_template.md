### **커밋 설명**

- initial commit: 초기 커밋. 과제 파일을 레포지토리로 가져왔습니다.
- implement service: PointService를 Database를 사용해 구현했습니다.
- Red: write code test: 테스트 코드를 작성했습니다. 이때 PointService가 실패하도록 구현한 기능을 지우고, initial commit에 있던 Controller의 리턴값을 사용했습니다.
- Green: implement service: 다시 PointService를 구현했습니다...
- write unitTest: mock을 사용, test명을 give-when-then에 맞춰 작성했습니다.

---
### **리뷰 받고 싶은 내용(질문)**

리뷰 포인트 1

- 커밋: write unitTest
- 내용: usePoint와 charPoint를 할때 PointHistory 테이블을 갱신합니다. 이것도 다른 함수에서 테스트하고 싶었는데, 이러면 해당 함수에 사용하지 않는 selectAllByUserId를 사용해야 해서 고민했습니다. 아예 service에 pointHistory 리스트를 검증하는 부분이 있는 게 나았을까요?


리뷰 포인트 2

- 커밋: Green: implement service ~ write unitTest
- 내용1: 단위 테스트는 오로지 로직 확인용인가요? 처음 테스트를 작성할 때, 통합 테스트(레포지토리, 여기서는 데이터베이스)를 작성했습니다. 이후 단위 테스트로 변경했습니다만, 의존성을 제외했다는 것말고는 큰 차이를 느끼지 못했습니다. 의존성을 제외할 때 어떤 이점이 있는가요?

> 로직 집중은 수업에서 이미 말씀하신 내용이나, 좀 더 알고싶어 질문드립니다.


리뷰 포인트 3

- 커밋: Green: implement service
- 내용: PointService의 usePoint와 chargePoint에서, pointHistory를 분리하는 게 더 독립적으로 나았을까요? PointService의 리팩토링이 충분하지 궁금합니다.


---

### **과제 셀프 피드백**
- 단위 테스트와 통합 테스트의 개념이 모호. 특히 서비스 기능을 구현할 때는 통합 테스트로 진행함
- use와 charge 메서드에서의 pointHistory에 대한 테스트를 어떻게 구현할지 생각하지 못함
- 간단한 API로 TDD를 익힘

### 기술적 성장
- 처음으로 test를 사용: mock, @Test, assert을 익힘
- give - then - when식 구조/테스트명에 대해 익힘
