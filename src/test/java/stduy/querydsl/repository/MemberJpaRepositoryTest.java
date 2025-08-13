package stduy.querydsl.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import stduy.querydsl.dto.MemberSearchCondition;
import stduy.querydsl.dto.MemberTeamDto;
import stduy.querydsl.entity.Member;
import stduy.querydsl.entity.Team;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@Test
	public void basicTest() throws Exception {
	    Member member = new Member("member10", 10);
		memberJpaRepository.save(member);

		Member findMember = memberJpaRepository.findById(member.getId()).get();
		assertEquals(member, findMember);

		List<Member> result1 = memberJpaRepository.findAll();
		assertThat(result1).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUserName("member10");
		assertThat(result2).containsExactly(member);
	}

	@Test
	public void basicQuerydslTest() throws Exception {
		Member member = new Member("member10", 10);
		memberJpaRepository.save(member);

		List<Member> result1 = memberJpaRepository.findAll_Querydsl();
		assertThat(result1).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUserName_Querydsl("member10");
		assertThat(result2).containsExactly(member);
	}

	@Test
	public void searchTest() throws Exception {
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

		MemberSearchCondition condition = new MemberSearchCondition();
		condition.setAgeGoe(35);
		condition.setAgeLoe(40);
		condition.setTeamName("teamB");

		List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);

		assertThat(result).extracting("username").containsExactly("member4");
	}
}