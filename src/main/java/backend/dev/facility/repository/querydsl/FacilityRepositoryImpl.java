package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilityFilterDTO;
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
    public Page<Facility> findFacility(FacilityFilterDTO filterDTO, Pageable pageable) {
        QFacility facility = QFacility.facility;

        // 조건 빌딩
        BooleanBuilder builder = new BooleanBuilder();

        // 각 필터 조건이 null이 아닌 경우에만 추가
        if (filterDTO.getPriceType() != null) {
            builder.and(facility.priceType.eq(filterDTO.getPriceType()));
        }
        if (filterDTO.getFacilityCategory() != null) {
            builder.and(facility.facilityCategory.eq(FacilityCategory.fromName(filterDTO.getFacilityCategory())));
        }
        if (filterDTO.getArea() != null && !filterDTO.getArea().isEmpty()) {
            builder.and(facility.area.containsIgnoreCase(filterDTO.getArea()));
        }
        if (filterDTO.getFacilityName() != null && !filterDTO.getFacilityName().isEmpty()) {
            builder.and(facility.facilityName.containsIgnoreCase(filterDTO.getFacilityName()));
        }

        // 조건이 없으면 findAll을 사용
        if (!builder.hasValue()) {
            var resultList = jpaQueryFactory
                    .selectFrom(facility)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long totalCount = jpaQueryFactory
                    .selectFrom(facility)
                    .fetchCount();

            return new PageImpl<>(resultList, pageable, totalCount);
        }

        // 조건이 있을 때만 쿼리 실행
        var resultList = jpaQueryFactory
                .selectFrom(facility)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 조건에 따른 데이터 수
        long totalCount = jpaQueryFactory
                .selectFrom(facility)
                .where(builder)
                .fetchCount();

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
