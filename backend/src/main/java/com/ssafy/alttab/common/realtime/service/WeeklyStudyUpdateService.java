package com.ssafy.alttab.common.realtime.service;

import com.ssafy.alttab.community.dto.WeeklyStudyDto;
import com.ssafy.alttab.community.service.CommunityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyStudyUpdateService extends AbstractRealtimeUpdateService<List<WeeklyStudyDto>> {

    private final CommunityService communityService;

    @Override
    protected List<WeeklyStudyDto> getInitialData() {
        return communityService.getWeeklyStudies();
    }
}