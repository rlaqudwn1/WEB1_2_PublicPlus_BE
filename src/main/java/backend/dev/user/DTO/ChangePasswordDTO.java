package backend.dev.user.DTO;

public record ChangePasswordDTO(String email,String changePassword, String checkChangePassword) {

    public boolean isSame(){
        return changePassword.equals(checkChangePassword);
    }
}
