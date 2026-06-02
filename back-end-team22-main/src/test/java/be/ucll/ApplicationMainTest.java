package be.ucll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ApplicationMainTest {

    @Test
    void testMainMethod() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            // When
            Application.main(new String[] {});

            // Then
            mocked.verify(() -> SpringApplication.run(Application.class, new String[] {}));
        }
    }
}