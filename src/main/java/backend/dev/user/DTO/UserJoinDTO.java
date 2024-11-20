package backend.dev.user.DTO;

public record UserJoinDTO(String email
        ,String password
        ,String checkPassword
        ,String nickname) {

    public boolean isSame(){
        return password.equals(checkPassword);
    }
}
