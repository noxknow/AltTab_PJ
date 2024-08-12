package com.ssafy.alttab.notification.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.notification.dto.NotificationRequestDto;
import com.ssafy.alttab.notification.dto.NotificationListResponseDto;
import com.ssafy.alttab.notification.dto.NotificationResponseDto;
import com.ssafy.alttab.notification.entity.Notification;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.alttab.common.jointable.entity.MemberStudy.createMemberStudy;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.MEMBER;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional
    public void createNotification(Long memberId, Long studyId, String studyName) throws MemberNotFoundException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        member.addNotification(Notification.createNotification(member, studyId, studyName));
    }

    @Transactional
    public void checkNotification(String username, NotificationRequestDto notificationRequestDto) throws MemberNotFoundException, StudyNotFoundException {

        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException(username));

        if (notificationRequestDto.isCheck()) {
            Study study = studyRepository.findById(notificationRequestDto.getStudyId())
                    .orElseThrow(() -> new StudyNotFoundException(notificationRequestDto.getStudyId()));

            MemberStudy memberStudy = createMemberStudy(member, study, MEMBER);
            memberStudyRepository.save(memberStudy);
            study.addMemberStudy(memberStudy);
        }

        member.getNotifications().removeIf(notification -> notification.getId().equals(notificationRequestDto.getNotificationId()));
    }

    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotifications(String username) throws MemberNotFoundException {

        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException(username));

        return NotificationListResponseDto.builder()
                .notifications(member.getNotifications().stream()
                        .map(NotificationResponseDto::toDto)
                        .toList())
                .build();
    }
}
