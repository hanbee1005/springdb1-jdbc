package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        // then
        assertThat(findMember).isEqualTo(member);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }

}