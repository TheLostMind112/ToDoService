package com.hyperwallet.todoservice.service;

import com.hyperwallet.todoservice.dao.ToDoItemDao;
import com.hyperwallet.todoservice.domain.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // start transactions at service level. Let them propagate to DAO level.
public class ToDoItemServiceImpl implements ToDoItemService {

    @Autowired
    private ToDoItemDao toDoItemDao;

    @Override
    @Transactional(readOnly = false)
    public void create(ToDoItem toDoItem) {
        toDoItemDao.create(toDoItem);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(ToDoItem toDoItem) {
        toDoItemDao.update(toDoItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ToDoItem getByTitle(String title) {
        return toDoItemDao.getByTitle(title);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByTitle(String title) {
        toDoItemDao.deleteByTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoItem> getAll() {
        return toDoItemDao.getAll();
    }
}
