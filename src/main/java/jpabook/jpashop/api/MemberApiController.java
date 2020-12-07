package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest member)
    {
        Long id = memberService.join(member.toEntity());
        return new CreateMemberResponse(id, member.getName());
    }

    @PostMapping("/api/v1/members/{id}")
    public UpdateMemberResponse modifyMember(
            @PathVariable(value = "id") Long id
            , @RequestBody @Valid UpdateMemberRequest request)
    {
        memberService.update(id, request.getName());
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(id, member.getName());
    }

    @GetMapping("/api/v1/members")
    public List<Member> membersV1()
    {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result<List<MemberDto>> memberV2()
    {

        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> members = findMembers
                .stream()
                .map(s-> new MemberDto(s.getName()))
                .collect(Collectors.toList());

        return new Result<>(members, members.size());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>
    {
        private T data;
        private int count;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto
    {
        private String name;
    }

    @Data
    static class CreateMemberResponse
    {
        private Long id;
        private String name;

        public CreateMemberResponse(Long id, String name)
        {
            this.name = name;
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest
    {
        @NotEmpty
        private String name;

        public Member toEntity()
        {
            Member member  = new Member();
            member.setName(name);
            return member;
        }
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse
    {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest
    {
        @NotEmpty
        private String name;
    }
}
