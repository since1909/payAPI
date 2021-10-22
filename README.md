## Simple Pay API

### 프로젝트 사용 기술

***

+ Spring Boot, Java 8

+ Maven

+ MyBatis

+ PostgreSQL

  

### 프로젝트 구성

***

+ 결제 승인

+ 결제 취소

  + 전체 취소 요청
  + 부분 취소 요청

+ 결제/취소 정보 조회

  

### 빌드 및 실행방법

***

1. **CLONE** master branch 

2. **RUN** src/main/java/com/hw/payAPI/PayApiApplicaation.java 



### 프로젝트 사용 설명

***

+ 결제 승인

  **http://{Server IP Address}:{Port Num}/payment**	

  위와 같은 URL을 통해 결제 승인 요청을 전송할 수 있습니다.

  요청 전송 시, Body에 '결제 데이터 예시' 와 같은 형태의 결제 정보를 json으로 전송해야 합니다.

  

  결제 데이터 예시 )

  ```json
  {
      "cardNum":"1234567890123456",
      "validDate":"1021",
      "cvc":"123",
      "installments":"0",
      "cost":"11000",
      "tax":"1000"
  }
  ```

  

  **카드 번호(cradNum), 유효기간(validDate), CVC 번호(cvc), 할부 개월(installments), 결제 금액(cost), 부가가치세(tax)**  정보를 반드시 포함해야 합니다. 

  카드 번호는 10자리 이상 20자리 이하로 입력해야 합니다.

  **부가가치세** 정보는 **null** (정보 없음) 일 수 있습니다. 경우에 따라 *"tax" : null* 을 기입하면 결제 금액에 따른 부가가치세를 자동으로 계산하여 결제를 승인합니다. 

  

+ 결제 취소

  **http://{Server IP Address}:{Port Num}/cancel**	

  위와 같은 URL을 통해 결제 취소 요청을 전송할 수 있습니다.

  요청 전송 시, Body에 '취소 데이터 예시' 와 같은 형태의 결제 정보를 json으로 전송해야 합니다.

  

  취소 데이터 예시 )

  ```json
  {
     "uid":"pay20211022432266085",
     "cost":"11000",
     "tax":"1000"
  }
  ```

  

  **결제 승인 번호(uid)**는 결제 승인 완료 시 반환되는 ID 로 취소 하고자 하는 결제 건의 ID 를 입력합니다.

  **부가가치세** 정보는 **null** (정보 없음) 일 수 있습니다. 경우에 따라 *"tax" : null* 을 기입하면 결제 금액에 따른 부가가치세를 자동으로 계산하여 결제를 취소 합니다.

  

  * 부분 취소

    결제 된 금액 내에서 반복적으로 부분 취소 요청을 보낼 수 있습니다.

    결제 금액을 초과한 취소를 요청하거나, 저장된 부가가치세 보다 큰 금액을 취소 요청하면 결제 취소가 거부 됩니다.

    

+ 결제/취소 정보 조회

  **http://{Server IP Address}:{Port Num}/payment/{uid}**	

  위와 같은 URL을 통해 결제 및 결제 취소 데이터를 조회 할 수 있습니다.

  

  **결제 승인 번호(uid)**는 결제 및 취소 승인 반환되는 ID 입니다.

  

  ```json
  {
      "uid":"pay20211018172253001",
      "maskedCardNum":"123456*******456",
      "validDate":"1021",
      "cvc":"123",
      "header":"PAYMENT",
      "cost":"11000",
      "tax":"1000"
  }
  ```

  

  ID를 통해 조회된 데이터는 위와 같습니다.
  
  
  
  **결제/취소 승인 ID (uid), 카드 번호 (maskedCardNum), 유효 기간 (validDate), CVC 번호 (cvc), 결제/취소 헤더 (header), 금액 (cost), 부가가치세(tax)** 정보를 확인할 수 있습니다. 
  
  카드 번호는 앞 6자리와 끝 3자리만 제공됩니다.



### 프로젝트 기능 설명

***

#### 데이터베이스 설계

![image](https://user-images.githubusercontent.com/37170306/138400445-a8da6263-a388-49f4-a169-8f64e398d77b.png)

+ payments 테이블


![image](https://user-images.githubusercontent.com/37170306/138400273-35c48f27-ccf5-4fe9-b174-19b8cbebd650.png)



결제 승인 ID 와 결제 정보를 담고 있는 문자열을 저장합니다.



+ cancels 테이블

  

![image](https://user-images.githubusercontent.com/37170306/138400327-3fa697c9-47b0-47ef-8b3e-d0ade5649779.png)

  

결제 취소 승인 ID와 결제 취소 정보를 담고 있는 문자열을 저장합니다.

부분 취소 기능 구현 시 쿼리에서 SUM 연산을 활용하기 위해 cost와 tax  값을 bigint 타입 column에 별도로 저장하도록 설계하였습니다. 



#### 결제 승인

결제 승인 요청을 통해 전달 받은 json 데이터를 가공하여 데이터베이스에 저장합니다.



#### 결제 취소

결제 취소 요청을 통해 전달 받은 json 데이터에서 금액 정보와 부가가치세 정보를 확인후 데이터베이스에 저장합니다.

데이터 베이스에 저장된 결제 승인 데이터와 그에 따른 이전 취소 데이터를 불러와 요청 받은 금액 정보의 유효성을 확인합니다.

확인된 데이터를 가공하여 데이터베이스에 저장합니다.



#### 결제/취소 정보 조회

데이터베이스에 각각 저장된 **결제 정보**와 **취소 정보**를 JOIN 하여 입력받은 ID와 일치하는 정보를 반환합니다. 



### 저작권 및 사용권 정보

***



### 버그 및 버전

***



### FAQ

***
