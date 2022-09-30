package com.dimpo.geofrm.repository;

import com.dimpo.geofrm.entity.Role;
import com.dimpo.geofrm.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SectionRepository extends CrudRepository<Section, Long> {
}
