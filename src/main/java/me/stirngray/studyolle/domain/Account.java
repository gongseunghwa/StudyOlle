package me.stirngray.studyolle.domain;


import lombok.*;
import org.springframework.context.annotation.Conditional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(of="id") //연관관계가 복잡해질 떄 무한루프로 인한 스택 오버플로우가 발생 할 수 있다
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String  nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob // Text 타입으로 db와 맵핑
    @Basic(fetch = FetchType.EAGER) //프로필이미지는 무조건 가져옴
    private String profileImage;

    //아래로는 알림을 위한 설정용
    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdateByEmail;

    private boolean studyUpdateByWeb;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
