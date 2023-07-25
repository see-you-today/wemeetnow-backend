package com.example.chat.service;

import com.example.chat.domain.Alarm;
import com.example.chat.domain.User;
import com.example.chat.domain.enums.AlarmType;
import com.example.chat.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AlarmWriteService {
    private final AlarmRepository alarmRepository;
//    / 누가 누구한테 보내는지도 정보가 있어야댐~
    public void makeFriendRequestAlarm(User loginedUser, Long friendedId) {
        Alarm newAlarm = Alarm.builder()
                            .content("")
                            .alarmType(AlarmType.FRIEND)
                            .isChecked(false)
                            .build();
        alarmRepository.save(newAlarm);
    }
}
