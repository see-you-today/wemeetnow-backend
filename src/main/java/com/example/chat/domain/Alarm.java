package com.example.chat.domain;

import com.example.chat.domain.enums.AlarmType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "alarm")
public class Alarm extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private String content; // 알람내용
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(columnDefinition="tinyint(1) default 0")
    private Boolean isChecked; // 0: no, 1: yes
//
//    / 한명이 여러개의 알림을 보낼 수 있고, 수신자의 id도 알아야함..

}