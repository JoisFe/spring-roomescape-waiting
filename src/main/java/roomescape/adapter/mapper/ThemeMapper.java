package roomescape.adapter.mapper;

import roomescape.adapter.out.ThemeEntity;
import roomescape.application.dto.ThemeCommand;
import roomescape.application.dto.ThemeResponse;
import roomescape.domain.Theme;

public class ThemeMapper {

  private ThemeMapper() {
    throw new UnsupportedOperationException("생성 불가");
  }

  public static ThemeResponse mapToResponse(Theme theme) {
    return ThemeResponse.of(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
  }

  public static Theme mapToDomain(ThemeCommand themeCommand) {
    return Theme.of(null, themeCommand.name(), themeCommand.description(), themeCommand.thumbnail());
  }

  public static Theme mapToDomain(ThemeEntity themeEntity) {
    return Theme.of(themeEntity.getId(), themeEntity.getName(), themeEntity.getDescription(),
      themeEntity.getThumbnail());
  }

  public static ThemeEntity mapToEntity(Theme theme) {
    return ThemeEntity.of(theme);
  }
}
