package com.example.demo.repository;

import com.example.demo.entity.LocationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationCodeRepository  extends JpaRepository<LocationCode, Long> {
    @Query("SELECT DISTINCT lc.locationSido FROM LocationCode lc")
    List<String> findDistinctLocationSido();

    @Query("SELECT DISTINCT lc.locationSigu FROM LocationCode lc where lc.locationSido = :sido")
    List<String> findDistinctLocationSigu(@Param("sido") String sido);


    Optional<LocationCode> findByLocationSidoAndLocationSiguAndLocationSidongAndLocationSiri(String daepyoSidoCd, String daepyoSiguCd, String daepyoSidongCd, String daepyoSiriCd);
}
