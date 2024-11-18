package study.my_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.my_board.domain.MemberRole;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
