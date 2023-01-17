package hexlet.code;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private static final int OK = 200;

    @Test
    public void testRootWelcomePage() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(get("/welcome"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(OK);
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }
}
