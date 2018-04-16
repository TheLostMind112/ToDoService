package com.hyperwallet.todoservice.dao;

import com.hyperwallet.todoservice.domain.ToDoItem;

import java.util.List;

/**
 * Dao interface declaring CRUD operations on H2 DB.
 */
public interface ToDoItemDao {
    void create(ToDoItem toDoItem);
    void update(ToDoItem toDoItem);
    ToDoItem getByTitle(String title);
    void deleteByTitle(String title);
    List<ToDoItem> getAll();
}
