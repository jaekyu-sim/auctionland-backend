package com.example.demo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor // 매개변수가 없는 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자
public class LocationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationCode;
    private String locationSido;
    private String locationSigu;
    private String locationSidong;
    private String locationSiri;


}
