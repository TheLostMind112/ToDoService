package rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperwallet.todoservice.application.Application;
import com.hyperwallet.todoservice.domain.ToDoItem;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ToDoItemControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void checkEmptyTable() throws Exception {
        mvc.perform(get("/getAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private ToDoItem getBasicToDoItem() throws Exception {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setTitle("mail to Cat");
        toDoItem.setDescription("Send mail to Catelyn about Jon Snow.");
        toDoItem.setCompleted(true);

        return toDoItem;
    }

    @Test
    public void testCreate() throws Exception {
        ToDoItem toDoItem = getBasicToDoItem();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(toDoItem);

        insertToDoItem(jsonInString);

         // Test Get by title
         mvc.perform(get("/getByTitle")
                .param("itemTitle", "mail to Cat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(toDoItem.getDescription())))
                .andExpect(jsonPath("$.completed", is(toDoItem.getCompleted())))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.title", is(toDoItem.getTitle().toLowerCase())));

    }

    private void insertToDoItem(String jsonInString) throws Exception {
        mvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                .andExpect(status().isCreated());
    }

    @Test()
    public void testDeleteCaseInsensitive() throws Exception {
        // insert to do item.
        ToDoItem toDoItem = getBasicToDoItem();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(toDoItem);
        insertToDoItem(jsonInString);

        // call delete
        mvc.perform(delete("/deleteByTitle")
                .param("itemTitle", "MAIL to Cat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mvc.perform(get("/getByTitle")
                .param("itemTitle", "mail to Cat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdate() throws Exception {
        // insert record
        ToDoItem toDoItem = getBasicToDoItem();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(toDoItem);
        insertToDoItem(jsonInString);

        toDoItem = new ToDoItem();
        toDoItem.setTitle("mail to Jon");
        toDoItem.setDescription("Send mail to Jon about his Jorah.");
        toDoItem.setCompleted(false);

        jsonInString = mapper.writeValueAsString(toDoItem);

        // Get Id of inserted record.
        MvcResult mvcResult = mvc.perform(get("/getByTitle")
                .param("itemTitle", "mail to Cat")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

       long id = ((Number)JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id")).longValue();

        // call update
        mvc.perform(put("/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                .andExpect(status().isOk());

        // Test if value is updated
        mvc.perform(get("/getByTitle")
                .param("itemTitle", "mail to Jon")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(toDoItem.getDescription())))
                .andExpect(jsonPath("$.completed", is(toDoItem.getCompleted())))
                .andExpect(jsonPath("$.title", is(toDoItem.getTitle().toLowerCase())));

    }

    @Test
    public void testGetAll() throws Exception {
        ToDoItem toDoItem = getBasicToDoItem();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(toDoItem);
        insertToDoItem(jsonInString);

        toDoItem.setTitle("Tell Jon that he knows nothing!");
        jsonInString = mapper.writeValueAsString(toDoItem);
        insertToDoItem(jsonInString);
        // Test Get all
        mvc.perform(get("/getAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test(expected = Exception.class)
    public void testInsertFailureForDuplicateTitle() throws Exception {
        testCreate();
        testCreate(); // should fail with DuplicateKeyException
    }
}
