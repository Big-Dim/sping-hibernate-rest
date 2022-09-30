package com.dimpo.geofrm.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

@Table(name = "geoclasses")
public class GeologicalClasses {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String geoName;
    private String code;
    @ManyToOne
    @JoinColumn(name = "sec_id")
    private Section section;

}
