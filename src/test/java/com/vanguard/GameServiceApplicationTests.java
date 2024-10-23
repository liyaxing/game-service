package com.vanguard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void testUploadCSV() throws Exception {
        String csvContent = "id,game_no,game_name,game_code,type,cost_price,tax,sale_price,date_of_sale\n" +
                            "1,7,VeDkRBEVu1MnKgXJ,9nM,1,46.04,0.09,50.18,2024-04-02 04:50:44\n" +
                            "2,98,PSRXlhUlP,5,1,3.47,0.09,3.78,2024-04-02 15:30:08\n";

        MockMultipartFile file = new MockMultipartFile("file", "gamesales.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(multipart("/import")
                        .file(file))
                .andExpect(status().isCreated());
    }
}
