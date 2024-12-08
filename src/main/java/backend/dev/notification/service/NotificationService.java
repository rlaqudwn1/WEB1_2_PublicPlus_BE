package backend.dev.notification.service;


import backend.dev.notification.controller.PushNotificationController;
import backend.dev.notification.dto.NotificationDTO;
import backend.dev.notification.entity.Notification;
import backend.dev.notification.exception.NotificationException;
import backend.dev.notification.repository.NotificationRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PushNotificationService pushNotificationService;

    public NotificationDTO createNotification(NotificationDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(username).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        Notification notification = NotificationDTO.fromDTO(dto, user);
        Notification savedNotification = notificationRepository.save(notification);

        pushNotificationService.sendPushNotification(dto,user);
        return NotificationDTO.toDTO(savedNotification);
    }
    public NotificationDTO toSendNotification(NotificationDTO dto, User user) {
        Notification notification = NotificationDTO.fromDTO(dto, user);
        Notification savedNotification = notificationRepository.save(notification);
        pushNotificationService.sendPushNotification(dto,user);
        return NotificationDTO.toDTO(savedNotification);
    }
    public NotificationDTO toUserNotification(NotificationDTO dto) {
        List<User> allUser = userRepository.findAllUser();
        for (User user : allUser) {
            pushNotificationService.sendPushNotification(dto,user);
            Notification notification = NotificationDTO.fromDTO(dto, user);
            Notification savedNotification = notificationRepository.save(notification);
        }
        return dto;
    }


    public List<NotificationDTO> getAllNotificationsByUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        return notificationRepository.findByUser(user)
                .stream().map(NotificationDTO::toDTO)
                .collect(Collectors.toList());
    }


    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(NotificationException.NOTIFICATION_NOT_FOUND::getNotificationException);

        notification.changeRead();

        return NotificationDTO.toDTO(notification);
    }


    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }
    public void deleteAllNotifications() {
        notificationRepository.deleteAll();
    }
}