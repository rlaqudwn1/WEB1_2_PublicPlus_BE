package backend.dev.tag.enums;

public enum TagValue {
    YANGCHEON("양천구", TagType.REGION),
    GANGSEO("강서구", TagType.REGION),
    GURO("구로구", TagType.REGION),
    GEUMCHEON("금천구", TagType.REGION),
    GWANAK("관악구", TagType.REGION),
    DONGJAK("동작구", TagType.REGION),
    SEOCHO("서초구", TagType.REGION),
    GANGNAM("강남구", TagType.REGION),
    SONGPA("송파구", TagType.REGION),
    GANGDONG("강동구", TagType.REGION),
    GWANGJIN("광진구", TagType.REGION),
    SEONGDONG("성동구", TagType.REGION),
    JUNGGU("중구", TagType.REGION),
    YONGSAN("용산구", TagType.REGION),
    YEONGDEUNGPO("영등포구", TagType.REGION),
    MAPO("마포구", TagType.REGION),
    SEODAEMUN("서대문구", TagType.REGION),
    EUNPYEONG("은평구", TagType.REGION),
    JONGNO("종로구", TagType.REGION),
    DONGDAEMUN("동대문구", TagType.REGION),
    SEONGBUK("성북구", TagType.REGION),
    JUNGRANG("중랑구", TagType.REGION),
    NOWON("노원구", TagType.REGION),
    DOBONG("도봉구", TagType.REGION),
    GANGBUK("강북구", TagType.REGION),


    TENNIS("테니스", TagType.CATEGORY),
    BASKETBALL("농구", TagType.CATEGORY),
    FOOTBALL("축구", TagType.CATEGORY),
    FUTSAL("풋살", TagType.CATEGORY),
    BASEBALL("야구", TagType.CATEGORY),
    VOLLEYBALL("배구", TagType.CATEGORY),
    PINGPONG("탁구", TagType.CATEGORY),
    BADMINTON("배드민턴", TagType.CATEGORY),
    BOWLING("볼링", TagType.CATEGORY),
    SWIMMING("수영", TagType.CATEGORY),
    HOCKEY("하키", TagType.CATEGORY),
    GOLF("골프", TagType.CATEGORY),
    ETC("기타", TagType.CATEGORY),

    CLEAN("깨끗함", TagType.ADVANTAGE),
    SPACIOUS("넓음", TagType.ADVANTAGE),
    FREE("무료 이용", TagType.ADVANTAGE),
    CAFE("카페 운영", TagType.ADVANTAGE),
    GOOD_LOCATION("편리한 위치", TagType.ADVANTAGE),
    EASY_RESERVATION("예약이 쉬움", TagType.ADVANTAGE),
    LARGE_RESERVATION("단체예약 가능", TagType.ADVANTAGE),
    MODERN_EQUIPMENT("최신 장비 구비", TagType.ADVANTAGE),
    RENTAL_AVAILABLE("장비대여 가능", TagType.ADVANTAGE),
    PARKING_AVAILABLE("주차 가능", TagType.ADVANTAGE),
    EAT_AVAILABLE("취식 가능", TagType.ADVANTAGE),
    SHOWER_AVAILABLE("샤워 시설 구비", TagType.ADVANTAGE),
    CHILDCARE_AVAILABLE("아이 돌봄 서비스", TagType.ADVANTAGE),
    CLASSES_AVAILABLE("강습 프로그램 운영", TagType.ADVANTAGE),
    EVENT_AVAILABLE("행사 운영", TagType.ADVANTAGE),
    WHEELCHAIR_ACCESSIBLE("휠체어 접근 가능", TagType.ADVANTAGE),
    SAFE_ENVIRONMENT("안전한 환경", TagType.ADVANTAGE),
    CHANGING_ROOM("탈의실 구비", TagType.ADVANTAGE),
    PET_FRIENDLY("반려동물 동반 가능", TagType.ADVANTAGE),
    ALL_HOURS("24시간 운영", TagType.ADVANTAGE);

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