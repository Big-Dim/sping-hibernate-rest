package com.dimpo.geofrm.repository;

import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GeologicalClassesRepository extends CrudRepository<GeologicalClasses, Long> {
    List<GeologicalClasses> findByCode(String code);

}
