package com.hyperwallet.todoservice.dao;

import com.hyperwallet.todoservice.domain.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// PS : I've used Spring JDBC template to access H2 DB.

/**
 * DAO implementation defining methods to create/update/delete/get To Do items.
 */
@Repository
public class ToDoItemDaoImpl implements ToDoItemDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String INSERT_TO_DO_ITEM_SQL = "INSERT INTO to_do_items (title, description, completed)" +
     " VALUES (LOWER(:title), :description, :completed)";

    private static final String UPDATE_TO_DO_ITEM_SQL = "UPDATE to_do_items SET title = LOWER(:title), " +
            " description = :description," +
            " completed = :completed" +
            " WHERE id = :id";

    private static final String GET_ALL_SQL = "SELECT * FROM to_do_items";

    private static final String GET_BY_TITLE_SQL = "SELECT * FROM to_do_items WHERE title = LOWER(:title)";

    private static final String DELETE_BY_TITLE_SQL = "DELETE FROM to_do_items WHERE title = LOWER(:title)";

    // H2 is mot letting me create unique index on LOWER(title). So I am converting all title values to lower case before insert / update.
    // This will ensure that multiple entries with the same title name but different case are not inserted.
    @Override
    @Transactional(readOnly = false)
    public void create(ToDoItem toDoItem) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", toDoItem.getTitle());
        parameterSource.addValue("description", toDoItem.getDescription());
        parameterSource.addValue("completed", toDoItem.getCompleted());

        namedParameterJdbcTemplate.update(INSERT_TO_DO_ITEM_SQL, parameterSource);

    }

    @Override
    @Transactional(readOnly = false)
    public void update(ToDoItem toDoItem) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", toDoItem.getTitle());
        parameterSource.addValue("description", toDoItem.getDescription());
        parameterSource.addValue("completed", toDoItem.getCompleted());
        parameterSource.addValue("id", toDoItem.getId());

        namedParameterJdbcTemplate.update(UPDATE_TO_DO_ITEM_SQL, parameterSource);
    }

    @Override
    @Transactional(readOnly = true)
    public ToDoItem getByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        final List<ToDoItem> toDoItems = namedParameterJdbcTemplate.query(GET_BY_TITLE_SQL, parameterSource, createRowMapper());
        if (toDoItems.isEmpty()) {
            // based on use case, we can throw ResourceNotFoundException as well.
            return null;
        }
        return toDoItems.get(0);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        namedParameterJdbcTemplate.update(DELETE_BY_TITLE_SQL, parameterSource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoItem> getAll() {
        Map<String, String> params = Collections.emptyMap();
        final List<ToDoItem> toDoItems = namedParameterJdbcTemplate.query(GET_ALL_SQL, params,createRowMapper());
        return toDoItems;
    }

    private RowMapper<ToDoItem> createRowMapper() {
        return ((rs, rowNum) -> {
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setTitle(rs.getString("title"));
            toDoItem.setDescription(rs.getString("description"));
            toDoItem.setCompleted(rs.getBoolean("completed"));
            toDoItem.setId(rs.getLong("id"));

            return toDoItem;
        });
    }
}
