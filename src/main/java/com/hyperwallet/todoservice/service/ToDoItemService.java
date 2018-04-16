package com.hyperwallet.todoservice.service;

import com.hyperwallet.todoservice.domain.ToDoItem;

import java.util.List;

public interface ToDoItemService {

    void create(ToDoItem toDoItem);
    void update(ToDoItem toDoItem);
    ToDoItem getByTitle(String title);
    void deleteByTitle(String title);
    List<ToDoItem> getAll();

}
