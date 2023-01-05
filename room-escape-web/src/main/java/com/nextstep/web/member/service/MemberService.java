package com.nextstep.web.member.service;

import com.nextstep.web.common.JwtTokenProvider;
import com.nextstep.web.member.LoginMember;
import com.nextstep.web.member.dto.MemberRequest;
import com.nextstep.web.member.repository.MemberDao;
import nextstep.common.BusinessException;
import nextstep.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        memberDao.findByUsername(memberRequest.getUsername()).ifPresent(memberEntity -> {
                    throw new BusinessException("이미 존재하는 닉네임입니다.");
        });
        return memberDao.save(memberRequest.toEntity());
    }

    public Member read(LoginMember loginMember) {
        return memberDao.findById(loginMember.getId()).orElseThrow(() ->
                new BusinessException("")).fromThis();
    }
}
