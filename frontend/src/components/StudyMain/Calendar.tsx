import { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import AttendanceInfo from './AttendanceInfo';
import './Calendar.scss';
import { EventClickArg } from '@fullcalendar/core/index.js';
import { v4 } from 'uuid';

type EventData = {
  id: string;
  title: string;
  start: string;
  participants: string[];
};

const data: EventData[] = [
  {
    id: '1',
    title: 'Study',
    start: '2024-08-09',
    participants: ['재영', '승호', '경헌', '지원', '종권', '치왕'],
  },
];

export default function Calendar() {
  const [selectedEvent, setSelectedEvent] = useState<EventClickArg | null>(
    null,
  );
  const [events, setEvents] = useState(data);

  const headerOptions = {
    left: 'today',
    center: 'title',
    end: 'prev,next',
  };

  const handleEventClick = (eventclickarg: EventClickArg) => {
    if (eventclickarg.event.id === selectedEvent?.event.id) {
      setSelectedEvent(null);
    } else {
      setSelectedEvent(eventclickarg);
    }
  };

  const handleDateClick = (dateclickarg: DateClickArg) => {
    const updatedEvents = events.filter(
      (event) => event.start === dateclickarg.dateStr,
    );
    if (updatedEvents.length === 1) {
      const remainingEvents = events.filter(
        (event) => event.start !== dateclickarg.dateStr,
      );
      setEvents(remainingEvents);
    } else {
      const newEvent: EventData = {
        id: v4(), // 고유 id 받아야 함
        title: 'Study',
        start: dateclickarg.dateStr,
        participants: [],
      };
      setEvents([...events, newEvent]);
    }
  };
  return (
    <div className="main">
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        headerToolbar={headerOptions}
        events={events}
        eventClick={handleEventClick}
        dateClick={handleDateClick}
        stickyFooterScrollbar={false}
        fixedWeekCount={false}
      />
      {selectedEvent && <AttendanceInfo eventclickarg={selectedEvent} />}
    </div>
  );
}
