package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member)
    {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member)
    {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty())
            throw new IllegalStateException("이미 존재 하는 회원 입니다.");
    }

    public List<Member> findMembers()
    {
        log.info("findMembers()");
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId)
    {
        return memberRepository.findOne(memberId);
    }
}
