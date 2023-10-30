package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {

    @Test
    @DisplayName("JDBC connection 테스트")
    public void connection() {
        // given

        // when
        Connection connection = DBConnectionUtil.getConnection();
        
        // then
        assertThat(connection).isNotNull();
    }
}