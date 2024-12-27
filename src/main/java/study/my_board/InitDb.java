package study.my_board;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.my_board.domain.Member;

@Component
@RequiredArgsConstructor
public class InitDb {

//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit1();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//
//        private final EntityManager em;
//        public void dbInit1() {
//            Member member = Member.createMember("userA", "123");
//            em.persist(member);
//        }
//    }
}
