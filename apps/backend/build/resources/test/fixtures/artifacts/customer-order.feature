Feature: 고객 주문 조회

  Scenario: 고객 ID로 주문 조회
    Given 고객 ID가 입력되었을 때
    When 주문번호로 주문을 조회하면
    Then 고객 ID에 해당하는 주문목록을 반환한다
