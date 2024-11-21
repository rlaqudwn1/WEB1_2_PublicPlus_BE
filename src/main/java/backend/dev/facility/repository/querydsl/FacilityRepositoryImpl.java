package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilitySearchCriteriaDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.QFacility;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class FacilityRepositoryImpl implements FacilityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final Pageable defaultPageable;

    public FacilityRepositoryImpl(JPAQueryFactory jpaQueryFactory, Pageable defaultPageable) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.defaultPageable = defaultPageable;
    }

    @Override
    public Page<Facility> findFacility(FacilitySearchCriteriaDTO criteria, Pageable pageable) {
        QFacility facility = QFacility.facility;

        // 조건 빌딩
        BooleanBuilder builder =new BooleanBuilder(); // 기본적으로 모든 시설이 포함됨
        builder.and(facility.priceType.eq(criteria.getPriceType()));
        builder.and(facility.facilityCategory.eq(FacilityCategory.fromName(criteria.getFacilityCategory())));
        builder.and(facility.area.containsIgnoreCase(criteria.getArea()));


        // 쿼리 실행
        var resultList = jpaQueryFactory
                .selectFrom(facility)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 전체 데이터 수
        long totalCount = resultList.size();
        return new PageImpl<>(resultList, pageable, totalCount);
    }
    @Override
    public Page<Facility> findFacilityByName(String name) {
        QFacility facility = QFacility.facility;
        BooleanBuilder builder =new BooleanBuilder();
        builder.and(facility.facilityName.containsIgnoreCase(name));

        var resultList = jpaQueryFactory
                .selectFrom(facility)
                .where(builder)
                .offset(defaultPageable.getOffset())
                .limit(defaultPageable.getPageSize())
                .fetch();
        long totalCount = resultList.size();
        return new PageImpl<>(resultList, defaultPageable, totalCount);
    }
}
