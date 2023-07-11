package com.example.chat.domain;

import com.example.chat.domain.enums.FriendStatus;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * '알림'을 통해 친구신청이 갔을 것이고 이를 통해 수락한 경우에만 현재 엔티티로 영향줌
 * */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "friend")
public class Friend extends BaseTime {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    private Long friendedId; // 친구신청 당한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 친구신청 한 사람 -> 한 사람이 여려명에게 친구신청을 보낼 수 있기 때문

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus; // BLOCK(차단), HIDE(숨김), FAVORITE(즐겨찾기)

}
