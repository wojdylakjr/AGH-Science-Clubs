package com.project.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.project.app.IntegrationTest;
import com.project.app.domain.CalendarEvent;
import com.project.app.repository.CalendarEventRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CalendarEventResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CalendarEventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PUBLICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendar-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Mock
    private CalendarEventRepository calendarEventRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendarEventMockMvc;

    private CalendarEvent calendarEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .imageUrl(DEFAULT_IMAGE_URL)
            .status(DEFAULT_STATUS);
        return calendarEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createUpdatedEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS);
        return calendarEvent;
    }

    @BeforeEach
    public void initTest() {
        calendarEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createCalendarEvent() throws Exception {
        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();
        // Create the CalendarEvent
        restCalendarEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isCreated());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate + 1);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCalendarEvent.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalendarEvent.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCalendarEventWithExistingId() throws Exception {
        // Create the CalendarEvent with an existing ID
        calendarEvent.setId(1L);

        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setTitle(null);

        // Create the CalendarEvent, which fails.

        restCalendarEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendarEvents() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEventList
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalendarEventsWithEagerRelationshipsIsEnabled() throws Exception {
        when(calendarEventRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalendarEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(calendarEventRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalendarEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(calendarEventRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalendarEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(calendarEventRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get the calendarEvent
        restCalendarEventMockMvc
            .perform(get(ENTITY_API_URL_ID, calendarEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendarEvent.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingCalendarEvent() throws Exception {
        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent
        CalendarEvent updatedCalendarEvent = calendarEventRepository.findById(calendarEvent.getId()).get();
        // Disconnect from session so that the updates on updatedCalendarEvent are not directly saved in db
        em.detach(updatedCalendarEvent);
        updatedCalendarEvent
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS);

        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalendarEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalendarEvent))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCalendarEvent.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalendarEvent.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendarEventWithPatch() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent using partial update
        CalendarEvent partialUpdatedCalendarEvent = new CalendarEvent();
        partialUpdatedCalendarEvent.setId(calendarEvent.getId());

        partialUpdatedCalendarEvent.title(UPDATED_TITLE).startDate(UPDATED_START_DATE).link(UPDATED_LINK).imageUrl(UPDATED_IMAGE_URL);

        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendarEvent))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCalendarEvent.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalendarEvent.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCalendarEventWithPatch() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent using partial update
        CalendarEvent partialUpdatedCalendarEvent = new CalendarEvent();
        partialUpdatedCalendarEvent.setId(calendarEvent.getId());

        partialUpdatedCalendarEvent
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS);

        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendarEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendarEvent))
            )
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEventList.get(calendarEventList.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendarEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCalendarEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCalendarEvent.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalendarEvent.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCalendarEvent.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCalendarEvent.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendarEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarEvent))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendarEvent() throws Exception {
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();
        calendarEvent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calendarEvent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        int databaseSizeBeforeDelete = calendarEventRepository.findAll().size();

        // Delete the calendarEvent
        restCalendarEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendarEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll();
        assertThat(calendarEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
