package backend.dev.tag.service;

import backend.dev.tag.dto.TagDTO;
import backend.dev.tag.enums.TagValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    public List<TagDTO> getAllTags() {
        return List.of(TagValue.values()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TagDTO convertToDTO(TagValue tagValue) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setValue(tagValue.getValue());
        tagDTO.setType(tagValue.getTagType().name());
        return tagDTO;
    }
}