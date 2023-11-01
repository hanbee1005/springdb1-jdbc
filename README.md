# Spring DB 1
인프런 강의 **스프링 DB 1편 - 데이터 접근 핵심 원리** 프로젝트입니다.

## JDBC 이해
<details><summary>등장 이유</summary>
<p>

- 대부분의 서비스의 경우 주요 데이터를 데이터베이스에 저장합니다.
- 일반적인 애플리케이션 서버와 데이터베이스의 연결은 다음과 같습니다.
  + 커넥션 연결: 주로 TCP/IP를 사용해 커넥션을 연결
  + SQL 전달: 애플리케이션 서버는 DB가 이해할 수 있는 SQL을 연결된 커넥션을 통해 DB에 전달
  + 결과 응답: DB는 전달된 SQL을 수행하고 그 결과를 응답
- 여기에는 2가지 큰 문제가 있었는데
  + DB마다 연결 방식이 달라서 DB를 변경하면 애플리케이션 서버에 개발된 DB 사용 코드도 함께 수정했어야 했고
  + 개발자가 각각의 DB마다 커넥션 연결, SQL 전달, 결과 응답 방식을 새로 학습해야 했습니다.
- 따라서 이 문제들을 해결하기 위해 JDBC라는 Java 표준이 등장합니다.

</p>
</details>

<details><summary>JDBC 표준 인터페이스</summary>
<p>

- JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API입니다.
- JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공합니다.
- 대표적으로 3가지 기능을 인터페이스로 정의해 제공합니다.
  + ```java.sql.Connection```: 연결
  + ```java.sql.Statement```: SQL을 담은 내용
  + ```java.sql.ResultSet```: SQL 요청 응답
- 개발자는 이제 이 인터페이스를 사용하면 되는데 각 DB에 맞게 구현된 라이브러리인 JDBC 드라이버를 사용하면 됩니다.
- 표준화의 한계
  + 공통이 있긴 하지만 각 DB에 맞는 SQL을 변경해야하는 부분은 여전히 존재

</p>
</details>

<details><summary>JDBC와 최신 데이터 접근 기술</summary>
<p>

- JDBC를 편리하게 사용하기 위해 SQL Mapper 와 ORM 기술이 존재합니다.
- SQL Mapper
  + 장점
    - SQL 응답 결과를 객체로 편리하게 변환
    - JDBC의 반복 코드를 제거
  + 단점
    - 개발자가 직접 SQL을 작성
  + 대표 기술: 스프링 JDBC Template, MyBatis
- ORM
  + ORM은 객체를 관계형 데이터베이스와 매핑해주는 기술로 덕분에 개발자는 반복적인 SQL을 직접 작성하지 않고 ORM 기술이 동적으로 SQL을 만들어 실행해줍니다.
  + 대표 기술: JPA, 하이버네이트, 이클립스 링크
  + JPA는 자바 진영의 ORM 표준 인터페이스이고 이것을 구현한 하이버네이트, 이클립스 링크 등의 기술이 있습니다.

</p>
</details>

<details><summary>데이터베이스 연결</summary>
<p>

- JDBC가 제공하는 ```DriverManager```는 라이브러리에 등록된 DB 드라이버를 관리하고 커넥션을 획득하는 기능을 제공합니다.
- 애플리케이션 로직에서 커넥션이 필요하면 ```DriverManager.getConnection()```을 호출합니다.
- ```DriverManager```는 라이브러리에 등록된 드라이버 목록을 자동으로 인식합니다. 이 드라이버들에게 순서대로 다음 정보를 넘겨 커넥션을 획득할 수 있는지 확인합니다.
  + URL (예. ```jdbc:h2:tcp://localhost/~/test```)
  + 이름, 비밀번호 등 접속에 필요한 정보
  + 여기서 각각의 드라이버는 URL 정보를 체크해서 본인이 처리할 수 있는 요청인지 확인합니다.
- 이렇게 찾은 커넥션 구현체가 클라이언트에 반환됩니다.

</p>
</details>

## Connection Pool 이해
<details><summary>DB 커넥션 방식</summary>
<p>

1. 애플리케이션 로직은 DB 드라이버를 통해 커넥션을 조회합니다.
2. DB 드라이버는 DB 와 TCP/IP 커넥션을 연결합니다.
3. DB 드라이버는 TCP/IP 커넥션이 연결되면 ID, PW와 기타 부가 정보를 DB에 전달합니다.
4. DB는 ID, PW를 통해 내부 인증을 완료하고 내부에 DB 세션을 생성합니다.
5. DB는 커넥션 생성이 완료되었다는 응답을 보냅니다.
6. DB 드라이버는 커넥션 객체를 생성해서 클라이언트에 반환합니다.

- 이러한 방식은 과정도 복잡하고 시간도 많이 걸리고 리소스를 많이 사용하는 일입니다.
</p>
</details>

<details><summary>커넥션 풀</summary>
<p>

- 애플리케이션 시작 시점에 미리 커넥션들을 생성하여 풀에 저장합니다. (보통 기본값 10)
- 이미 연결이 되어 있기 때문에 즉시 SQL문을 실행할 수 있습니다.
- 커넥션은 사용 후 살아있는 상태로 다시 커넥션 풀로 반환됩니다.
- 커넥션 풀은 서버당 최대 커넥션 수를 제한할 수 있어서 DB를 보호할 수 있습니다.
- 대표적으로 커넥션 풀 오픈 소스는 여러가지가 있지만 Spring 2.0부터 HikriCP를 사용합니다.
- 스프링 부트를 사용하면 자동으로 HikariCP를 사용하게 됩니다.
</p>
</details>

<details><summary>DataSource 이해</summary>
<p>

- 커넥션을 획득하는 방법을 추상화한 것입니다.
- 개발자는 DriverManager 나 커넥션 풀에 직접 접근하는게 아니라 DataSource 인터페이스를 의존하여 사용하면 됩니다.
- DriverManager는 DataSource 인터페이스를 구현하고 있지는 않지만 Spring이 DriverManager도 DataSource 인터페이스를 통해 사용할 수 있도록 DriverManagerDataSource 라는 클래스를 제공합니다.
</p>
</details>