package backend.dev.user.service;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.admin.AdminJoinDTO;
import backend.dev.user.entity.AdminCode;
import backend.dev.user.entity.User;
import backend.dev.user.DTO.UserMapper;
import backend.dev.user.repository.CodeRepository;
import backend.dev.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

    @Transactional(readOnly = true)
    public List<User> findUserList() {
        return userRepository.findAllUser();
    }

    @Transactional(readOnly = true)
    public List<User> findAdminList() {
        return userRepository.findAllAdmin();
    }

    public boolean joinAdmin(AdminJoinDTO adminJoinDTO) {

        if (adminJoinDTO.isDifferent()) {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        if (codeRepository.checkCode(adminJoinDTO.adminCode())) {
            String encodedPassword = passwordEncoder.encode(adminJoinDTO.password());
            User admin = UserMapper.DtoToAdmin(adminJoinDTO, encodedPassword);
            userRepository.save(admin);
            AdminCode usedCode = codeRepository.findAdminCodeByCode(adminJoinDTO.adminCode());
            codeRepository.delete(usedCode);
            return true;
        }
        throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_CERTIFICATION);
    }

    public List<AdminCode> findAdminCodeList() {
        return codeRepository.findAllAdminCodes();
    }

    public boolean generateCode() {
        List<AdminCode> adminCodeList = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                String uuid = UUID.randomUUID().toString();
                AdminCode code = AdminCode.builder().code(uuid).build();
                adminCodeList.add(code);
            }
            codeRepository.saveAll(adminCodeList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteAdmin(String userId) {
        User admin = userRepository.findById(userId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        userRepository.delete(admin);
    }
}
