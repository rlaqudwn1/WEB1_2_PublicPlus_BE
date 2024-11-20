package backend.dev.facility.entity;

import lombok.Getter;

@Getter
public enum FacilityCategory {
    FOOTBALL_FIELD("축구장"),
    FUTSAL_FIELD("풋살장"),
    FOOT_VOLLEYBALL_FIELD("족구장"),
    BASEBALL_FIELD("야구장"),
    TENNIS_FIELD("테니스장"),
    BASKETBALL_FIELD("농구장"),
    VOLLEYBALL_FIELD("배구장"),
    MULTIPURPOSE_FIELD("다목적경기장"),
    SPORTS_FIELD("운동장"),
    GYM("체육관"),
    BADMINTON_FIELD("배드민턴장"),
    TABLE_TENNIS_FIELD("탁구장"),
    EDUCATIONAL_FACILITY("교육시설"),
    SWIMMING_POOL("수영장"),
    GOLF_FIELD("골프장");

    private final String name;

    FacilityCategory(String name) {
        this.name = name;
    }

    public static FacilityCategory fromName(String name) {
        for (FacilityCategory category : values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown FacilityCategory name: " + name);
    }
}
