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

    @Query("SELECT DISTINCT lc.locationSidong FROM LocationCode lc where lc.locationSido = :sido and lc.locationSigu = :sigu")
    List<String> findDistinctLocationSidong(@Param("sido") String Sido, @Param("sigu") String sigu);

    @Query("SELECT DISTINCT lc.locationSiri FROM LocationCode lc where lc.locationSido = :sido and lc.locationSigu = :sigu and lc.locationSidong = :sidong")
    List<String> findDistinctLocationSiri(@Param("sido") String Sido, @Param("sigu") String sigu, @Param("sidong") String sidong);

    Optional<LocationCode> findByLocationSidoAndLocationSiguAndLocationSidongAndLocationSiri(String daepyoSidoCd, String daepyoSiguCd, String daepyoSidongCd, String daepyoSiriCd);
}
