package backend.dev.activity.controller;

import backend.dev.activity.dto.ActivityCreateDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.dto.ActivityUpdateDTO;
import backend.dev.activity.entity.Activity;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/activity")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> getActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok((activityService.readActivity(activityId)));
    }
    @PostMapping
    public ResponseEntity<ActivityResponseDTO> createActivity(@RequestBody ActivityCreateDTO activityCreateDTO) {
        log.info(activityCreateDTO.toString());
        return ResponseEntity.ok(activityService.createActivity(activityCreateDTO));
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> updateActivity(@RequestBody ActivityUpdateDTO activityUpdateDTO, @PathVariable Long activityId) {
        log.info(activityUpdateDTO.toString());
        return ResponseEntity.ok(activityService.updateActivity(activityId,activityUpdateDTO));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Map<String,String>> deleteActivity(@PathVariable Long activityId) {
        log.info(activityId.toString());
        activityService.deleteActivity(activityId);
        return ResponseEntity.status(200).body(Map.of("200", "deleted"));
    }
}
