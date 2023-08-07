package com.example.demo.repository;

import com.example.demo.entity.LocationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationCodeRepository  extends JpaRepository<LocationCode, Long> {
    @Query("SELECT DISTINCT lc.locationSido FROM LocationCode lc")
    List<String> findDistinctLocationSido();
    Optional<LocationCode> findByLocationSidoAndLocationSiguAndLocationSidongAndLocationSiri(String daepyoSidoCd, String daepyoSiguCd, String daepyoSidongCd, String daepyoSiriCd);
}
