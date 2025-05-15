package stduy.querydsl;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import stduy.querydsl.entity.Member;
import stduy.querydsl.entity.QMember;
import stduy.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

	@PersistenceContext
	EntityManager em;

	JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
	}

	@Test
	public void startJPQL() throws Exception {
	    // given
		Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
			.setParameter("username", "member1")
			.getSingleResult();

		// when

	    // then
		assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void startQuerydsl() throws Exception {
	    // given
		queryFactory = new JPAQueryFactory(em);
		QMember m = new QMember("M");

		// when
		Member findMember = queryFactory
			.select(m)
			.from(m)
			.where(m.username.eq("member1"))
			.fetchOne();

	    // then
		assertThat(findMember.getUsername()).isEqualTo("member1");

	}
}
