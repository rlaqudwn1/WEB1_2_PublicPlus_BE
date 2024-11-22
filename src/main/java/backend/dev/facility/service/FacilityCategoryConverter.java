package backend.dev.facility.service;

import backend.dev.facility.entity.FacilityCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FacilityCategoryConverter implements AttributeConverter<FacilityCategory, String> {
    @Override
    public String convertToDatabaseColumn(FacilityCategory category) {
        return category != null ? category.toString() : null;
    }

    @Override
    public FacilityCategory convertToEntityAttribute(String s) {
        return FacilityCategory.fromName(s);
    }
}
