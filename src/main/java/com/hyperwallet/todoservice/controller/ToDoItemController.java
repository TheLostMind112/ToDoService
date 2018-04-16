package com.hyperwallet.todoservice.controller;

import com.hyperwallet.todoservice.domain.ToDoItem;
import com.hyperwallet.todoservice.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// I've not provided methods like deleteById, getById etc. We can add them if needed.
// I've assumed that we might want to lookup / delete "to do" items based on title. So I've provided getAll, getByTitleName, deleteByTitleName methods.

/**
 * Rest controller providing CRUD operations for ToDoItems.
 */
@RestController
public class ToDoItemController {

    @Autowired
    private ToDoItemService toDoItemService;

    @RequestMapping(method = RequestMethod.POST,value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ToDoItem toDoItem) {
        toDoItemService.create(toDoItem);
    }

    /**
     * Get a To Do item based on it's title.
     * @param itemTitle The title of the item.
     * @return The {@link ToDoItem} based on title.
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getByTitle")
    @ResponseStatus(HttpStatus.OK)
    public ToDoItem getByTitle(@RequestParam(value = "itemTitle") String itemTitle) {
        return toDoItemService.getByTitle(itemTitle);
    }

    /**
     * Get all To Do items.
     * @return A List of To Do items.
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoItem> getAll() {
        return toDoItemService.getAll();
    }

    /**
     * Update a {@link ToDoItem} based on it's id.
     * @param id The id of the {@link ToDoItem}.
     * @param toDoItem The new {@link ToDoItem} that has to replace the old one.
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable (value = "id") final long id, @RequestBody ToDoItem toDoItem) {
        toDoItem.setId(id);
        toDoItemService.update(toDoItem);
    }

    /**
     * Delete a {@link ToDoItem} based on title.
     * @param itemTitle The title of {@link ToDoItem} to delete.
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteByTitle")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByName(@RequestParam(value = "itemTitle") String itemTitle) {
        toDoItemService.deleteByTitle(itemTitle);
    }
}
