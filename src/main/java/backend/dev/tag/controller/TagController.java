package backend.dev.tag.controller;

import backend.dev.tag.dto.TagDTO;
import backend.dev.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
}
