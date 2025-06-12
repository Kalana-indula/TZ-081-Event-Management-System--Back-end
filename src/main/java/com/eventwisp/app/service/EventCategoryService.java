package com.eventwisp.app.service;

import com.eventwisp.app.entity.EventCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventCategoryService {
    EventCategory addCategory(EventCategory eventCategory);

    List<EventCategory> getAllCategories();

    EventCategory findCategoryById(Long id);

    EventCategory updateCategory(Long id, EventCategory updatedEventCategory);

    Boolean deleteCategory(Long id);
}
