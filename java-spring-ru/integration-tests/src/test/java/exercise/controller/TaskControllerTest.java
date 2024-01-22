package exercise.controller;

import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    @Test
    public void testShow() throws Exception {
        var task = generateTask();
        taskRepository.save(task);

        mockMvc.perform(get("/tasks/" + task.getId())).andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<>();
        data.put("title", "title");
        data.put("description", "description");

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var data = new HashMap<>();
        data.put("title", "title");
        data.put("description", "description");

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());
        var maybeTask = taskRepository.findById(task.getId()).get();

        assertThat(maybeTask.getId()).isEqualTo((task.getId()));
        assertThat(maybeTask.getTitle()).isEqualTo(("title"));
        assertThat(maybeTask.getDescription()).isEqualTo(("description"));
    }

    @Test
    public void testDelete() throws Exception {
        var task = generateTask();
        taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId())).andExpect(status().isOk());

        var maybeTask = taskRepository.findById(task.getId());
        assertThat(maybeTask.isPresent()).isEqualTo(false);
    }
}
