package study.my_board.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.my_board.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    select m from Member m where m.username = :username
//    List<Member> findByUsername(String username);

        @EntityGraph(attributePaths = { "posts" })
        List<Member> findAll();

        Optional<Member> findByUsername(String username);

        @Query("select r.name from Role r " +
                "join r.memberRoles mr " +
                "join mr.member m " +
                "where m.id = :memberId")
        List<String> findRolesByMemberId(@Param("memberId") Long memberId);

        public Member findOneByUsername(String username);


//        @Query("select m from member m " +
//                "join fetch m.memberRoles mr " +
//                "join fetch mr.role " +
//                "where m.username = :username")
        @Query("select m from Member m " +
                "join fetch m.memberRoles mr " +
                "join fetch mr.role " +
                "where m.username = :username")
        Optional<Member> findByUsernameWithRoles(@Param("username") String username);
}
