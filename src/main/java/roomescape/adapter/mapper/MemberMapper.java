package roomescape.adapter.mapper;

import roomescape.adapter.out.MemberEntity;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.domain.Member;

public class MemberMapper {

  private MemberMapper() {
    throw new UnsupportedOperationException("생성 불가");
  }

  public static Member mapToDomain(MemberEntity memberEntity) {
    return Member.of(memberEntity.getName(), memberEntity.getEmail(), memberEntity.getPassword(),
      memberEntity.getRole());
  }

  public static Member mapToDomain(MemberCommand memberCommand) {
    return Member.of(memberCommand.name(), memberCommand.email(), memberCommand.password(), memberCommand.role());
  }

  public static MemberResponse mapToResponse(Member member) {
    return new MemberResponse(member.getName(), member.getRole());
  }

  public static MemberEntity mapToEntity(Member member) {
    return MemberEntity.of(member);
  }
}
