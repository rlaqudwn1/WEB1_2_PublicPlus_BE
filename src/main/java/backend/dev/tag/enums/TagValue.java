package backend.dev.tag.enums;

public enum TagValue {
    DONGDAEMUN("동대문구", TagType.REGION),
    YANGCHEON("양천구", TagType.REGION),

    TENNIS("테니스", TagType.CATEGORY),
    BASKETBALL("농구", TagType.CATEGORY),
    FOOTBALL("축구", TagType.CATEGORY),

    CLEAN("깨끗함", TagType.ADVANTAGE),
    NEAR_SUBWAY("역가까움", TagType.ADVANTAGE);

    private final String value;
    private final TagType tagType;

    TagValue(String value, TagType tagType) {
        this.value = value;
        this.tagType = tagType;
    }

    public String getValue() {
        return value;
    }

    public TagType getTagType() {
        return tagType;
    }
}
