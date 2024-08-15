import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { AttendanceInfo } from './AttendanceInfo';
import './Calendar.scss';
import { EventClickArg, DatesSetArg } from '@fullcalendar/core/index.js';
import { v4 } from 'uuid';
import { useClickedDate } from '@/hooks/useClickedDate';
import { format } from 'date-fns';
import {
  useDeleteProblemQuery,
  useGetSchedulesQuery,
  usePostProblemQuery,
} from '@/queries/schedule';

type EventData = {
  id: string;
  title: string;
  start: string;
};

type CalendarProps = {
  participants: string[] | undefined;
  refetchAttendances: () => Promise<void>;
};

export function Calendar({ participants, refetchAttendances }: CalendarProps) {
  const [events, setEvents] = useState<EventData[]>();
  const { studyId } = useParams<{ studyId: string }>();
  const { setClickedDate } = useClickedDate();
  const postProblemMutation = usePostProblemQuery();
  const deleteProblemMutation = useDeleteProblemQuery();
  const [yearMonth, setYearMonth] = useState(format(new Date(), 'yyyy-MM-dd'));
  const { refetch } = useGetSchedulesQuery(studyId!, yearMonth);

  const refetchSchedules = useCallback(async () => {
    const { data: schedules } = await refetch();
    if (schedules) {
      setEvents(convertDatesToEventData(schedules!.deadlines));
    }
  }, []);

  const convertDatesToEventData = (dates: string[]): EventData[] =>
    dates.map((date) => ({
      id: v4(),
      title: 'Study',
      start: date,
    }));

  useEffect(() => {
    refetchSchedules();
  }, [yearMonth]);

  const headerOptions = {
    left: 'today',
    center: 'title',
    end: 'prev,next',
  };

  const handleEventClick = (eventclickarg: EventClickArg) => {
    setClickedDate(format(eventclickarg!.event!.start!, 'yyyy-MM-dd'));
  };

  const handleDateClick = async (dateclickarg: DateClickArg) => {
    setClickedDate(format(dateclickarg.dateStr, 'yyyy-MM-dd'));
    const updatedEvents = events?.filter(
      (event) => event.start === dateclickarg.dateStr,
    );
    if (updatedEvents?.length === 1) {
      return;
    } else {
      const scheduleForm = {
        studyId: parseInt(studyId!),
        deadline: dateclickarg.dateStr,
        presenter: '',
        problemId: 1000,
      };
      await postProblemMutation.mutateAsync(scheduleForm, {
        onSuccess: async () => {
          const problemForm = {
            studyId: parseInt(studyId!, 10),
            deadline: dateclickarg.dateStr,
            problemId: 1000,
          };
          await deleteProblemMutation.mutateAsync(problemForm);
        },
      });
    }
    refetchSchedules();
  };

  const handleDatesSet = (arg: DatesSetArg) => {
    setYearMonth(format(arg.start, 'yyyy-MM-dd'));
  };

  return (
    <div className="calendar">
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        headerToolbar={headerOptions}
        events={events}
        eventClick={handleEventClick}
        dateClick={handleDateClick}
        stickyFooterScrollbar={false}
        fixedWeekCount={false}
        datesSet={handleDatesSet}
        showNonCurrentDates={false}
      />
      <AttendanceInfo
        events={events}
        refetchSchedules={refetchSchedules}
        participants={participants}
        refetchAttendances={refetchAttendances}
      />
    </div>
  );
}
