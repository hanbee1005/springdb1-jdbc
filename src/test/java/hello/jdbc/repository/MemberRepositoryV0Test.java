package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();
    
    @Test
    @DisplayName("crud 테스트")
    public void crud() throws SQLException {
        // given
        Member member = new Member("memberV0", 10000);

        // when
        repository.save(member);
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);

        log.info("member == findMember: {}", member == findMember);
        log.info("member equals findMember: {}", member.equals(findMember));

        // then
        assertThat(findMember).isEqualTo(member);
    }

}