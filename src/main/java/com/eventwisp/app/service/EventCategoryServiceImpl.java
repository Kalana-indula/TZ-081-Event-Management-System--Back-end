package com.eventwisp.app.service;

import com.eventwisp.app.entity.EventCategory;
import com.eventwisp.app.repository.EventCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCategoryServiceImpl implements EventCategoryService{

    //Create an instance of 'EventCategoryRepository'
    private EventCategoryRepository eventCategoryRepository;

    //Inject an 'EventCategoryRepository'
    @Autowired
    public EventCategoryServiceImpl(EventCategoryRepository eventCategoryRepository){
        this.eventCategoryRepository=eventCategoryRepository;
    }

    //save a new category
    @Override
    public EventCategory addCategory(EventCategory eventCategory) {
        return eventCategoryRepository.save(eventCategory);
    }

    //Get all categories
    @Override
    public List<EventCategory> getAllCategories() {
        return eventCategoryRepository.findAll();
    }

    //Get a cetegory by id
    @Override
    public EventCategory findCategoryById(Long id) {
        return eventCategoryRepository.findById(id).orElse(null);
    }

    //Update an existing category
    @Override
    public EventCategory updateCategory(Long id, EventCategory updatedEventCategory) {
        //Find existing category
        EventCategory existingCategory=eventCategoryRepository.findById(id).orElse(null);

        //Chek if there is an existing category
        if(existingCategory==null){
            return null;
        }

        existingCategory.setCategory(updatedEventCategory.getCategory());

        return eventCategoryRepository.save(existingCategory);
    }

    //Delete a category
    @Override
    public Boolean deleteCategory(Long id) {

        //Check if a category is existing
        boolean isExist=eventCategoryRepository.existsById(id);

        if(isExist){
            eventCategoryRepository.deleteById(id);

            return true;
        }
        return false;
    }
}
