import React, { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid'; // DayGrid 플러그인
import interactionPlugin from '@fullcalendar/interaction'; // 클릭/드래그 플러그인

const Calendar = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const apiKey = 'AIzaSyAFpB67plDQvU1O3bFJKxJ5iolLU32i2PQ'; // 구글 API 키
        const calendarId = 'fdbab86003a08c78a1bccefc3300e308cf5cccff75c43f03ca4595fdc8ba405b@group.calendar.google.com'; // 구글 캘린더 ID
        const googleCalendarUrl = `https://www.googleapis.com/calendar/v3/calendars/${calendarId}/events?key=${apiKey}`;

        fetch(googleCalendarUrl)
            .then((response) => response.json())
            .then((data) => {
                const fetchedEvents = data.items.map((item) => ({
                    title: item.summary, // 이벤트 제목
                    start: item.start.dateTime || item.start.date, // 시작 시간
                    end: item.end.dateTime || item.end.date, // 종료 시간
                }));
                setEvents(fetchedEvents); // 이벤트 상태 업데이트
            })
            .catch((error) => console.error('Error fetching events:', error));
    }, []); // 컴포넌트가 마운트될 때 한 번만 실행

    return (
        <div>
            <FullCalendar
                plugins={[dayGridPlugin, interactionPlugin]} // 사용할 플러그인
                initialView="dayGridMonth" // 기본 뷰
                events={events} // 구글 캘린더에서 가져온 이벤트 데이터
                headerToolbar={{
                    left: 'prev,next today', // 왼쪽 툴바: 이전, 다음, 오늘 버튼
                    center: 'title', // 중앙 툴바: 캘린더 제목
                    right: 'dayGridMonth,dayGridWeek,dayGridDay', // 오른쪽 툴바: 월, 주, 일 뷰 전환
                }}
            />
        </div>
    );
};

export default Calendar;
