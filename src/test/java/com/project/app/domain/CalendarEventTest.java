package com.project.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendarEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendarEvent.class);
        CalendarEvent calendarEvent1 = new CalendarEvent();
        calendarEvent1.setId(1L);
        CalendarEvent calendarEvent2 = new CalendarEvent();
        calendarEvent2.setId(calendarEvent1.getId());
        assertThat(calendarEvent1).isEqualTo(calendarEvent2);
        calendarEvent2.setId(2L);
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);
        calendarEvent1.setId(null);
        assertThat(calendarEvent1).isNotEqualTo(calendarEvent2);
    }
}
