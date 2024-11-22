package backend.dev.user.DTO;

public record UserChangeInfoDTO(
        String nickname,
        String description
) {
    private static final String NICKNAME_REGEX = "^[a-z0-9가-힣]{2,10}$";

    public boolean checkNickname() {
        return nickname.matches(NICKNAME_REGEX);
    }
}
