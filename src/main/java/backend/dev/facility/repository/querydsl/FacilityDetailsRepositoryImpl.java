package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Component
@Repository
public class FacilityDetailsRepositoryImpl implements FacilityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final Pageable defaultPageable;

    public FacilityDetailsRepositoryImpl(JPAQueryFactory jpaQueryFactory, Pageable defaultPageable) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.defaultPageable = defaultPageable;
    }

    @Override
    public Page<FacilityDetails> findFacility(FacilityFilterDTO filterDTO, Pageable pageable) {
        log.info("Category check : {}", filterDTO.getFacilityCategory());
        QFacilityDetails qFacilityDetails = QFacilityDetails.facilityDetails;

        // 조건 빌딩
        BooleanBuilder builder = new BooleanBuilder();

        // 각 필터 조건이 null이 아닌 경우에만 추가
        if (filterDTO.getPriceType() != null) {
            builder.and(qFacilityDetails.priceType.eq(filterDTO.getPriceType()));
        }
        if (filterDTO.getFacilityCategory() != null) {
            builder.and(qFacilityDetails.facilityCategory.eq(FacilityCategory.fromName(filterDTO.getFacilityCategory())));
        }
        if (filterDTO.getArea() != null && !filterDTO.getArea().isEmpty()) {
            builder.and(qFacilityDetails.area.containsIgnoreCase(filterDTO.getArea()));
        }
        if (filterDTO.getFacilityName() != null && !filterDTO.getFacilityName().isEmpty()) {
            builder.and(qFacilityDetails.facilityName.containsIgnoreCase(filterDTO.getFacilityName()));
        }
        log.info("좋아요 오더 : {}",filterDTO.getLikeOrder());

            OrderSpecifier<?> orderSpecifier = null;
            switch (filterDTO.getLikeOrder()){
                case 1:
                    orderSpecifier = qFacilityDetails.likes.desc();
                    break;
                case 2:
                    orderSpecifier = qFacilityDetails.likes.asc();
                    break;
                default:
                    break;
            }



        // 조건이 없으면 findAll을 사용
        if (!builder.hasValue()) {
            var resultList = jpaQueryFactory
                    .selectFrom(qFacilityDetails)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long totalCount = jpaQueryFactory
                    .selectFrom(qFacilityDetails)
                    .fetchCount();

            return new PageImpl<>(resultList, pageable, totalCount);
        }

        // 조건이 있을 때만 쿼리 실행
        List<FacilityDetails> list = jpaQueryFactory
                .selectFrom(qFacilityDetails)
                .where(builder)
                .offset(pageable.getOffset())
                .orderBy(orderSpecifier!= null ? orderSpecifier : qFacilityDetails.facilityId.asc())
                .limit(pageable.getPageSize())
                .fetch();


        // 조건에 따른 데이터 수
        long totalCount = jpaQueryFactory
                .selectFrom(qFacilityDetails)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(list, pageable, totalCount);
    }


    @Override
    public Page<FacilityDetails> findFacilityByName(String name) {
        QFacilityDetails facility = QFacilityDetails.facilityDetails;
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



    @Override
    public Page<FacilityDetails> findFacilitiesByLocation(Double latitude, Double longitude, Double radius, Pageable pageable) {
        QFacilityDetails facility = QFacilityDetails.facilityDetails;

        double earthRadius = 6371; // 지구 반지름 (킬로미터)
        double radiusInKm = radius; // 반경을 킬로미터로 변환

        // Haversine 공식 계산을 위한 템플릿 생성
        TemplateExpression<Double> haversineDistance =
                ExpressionUtils.template(Double.class,
                        "6371 * ACOS(SIN(RADIANS({0})) * SIN(RADIANS({1})) + COS(RADIANS({0})) * COS(RADIANS({1})) * COS(RADIANS({2}) - RADIANS({3})))",
                        latitude, facility.latitude, longitude, facility.longitude);

        // `haversineDistance`를 `NumberTemplate`로 변환
        NumberTemplate<Double> haversineDistanceTemplate =
                Expressions.numberTemplate(Double.class, "{0}", haversineDistance);

        // 반경 내의 시설만 필터링 (거리가 radiusInKm 이하인 시설만 찾기)
        long total = jpaQueryFactory.selectFrom(facility)
                .where(haversineDistanceTemplate.loe(radiusInKm))
                .fetchCount();

        // 페이지네이션 적용한 결과 조회
        var resultList = jpaQueryFactory.selectFrom(facility)
                .where(haversineDistanceTemplate.loe(radiusInKm))
                .orderBy(facility.facilityName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(resultList, pageable, total);
    }




}
