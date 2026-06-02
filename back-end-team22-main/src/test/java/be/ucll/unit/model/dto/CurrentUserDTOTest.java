// package be.ucll.unit.model.dto;

// import org.junit.jupiter.api.Test;

// import be.ucll.model.dto.*;

// import static org.junit.jupiter.api.Assertions.*;

// class CurrentUserDTOTest {

//     @Test
//     void testDefaultConstructor() {
//         CurrentUserDTO dto = new CurrentUserDTO();

//         assertNull(dto.getId());
//         assertNull(dto.getName());
//         assertNull(dto.getEmail());
//     }

//     @Test
//     void testParameterizedConstructor() {
//         CurrentUserDTO dto = new CurrentUserDTO(1L, "John Doe", "john@example.com", "Belgian", 30, "Male", null, 150, null);

//         assertEquals(1L, dto.getId());
//         assertEquals("John Doe", dto.getName());
//         assertEquals("john@example.com", dto.getEmail());
//     }

//     @Test
//     void testEquality() {
//         CurrentUserDTO dto1 = new CurrentUserDTO(1L, "John Doe", "john@example.com", "Belgian", 30, "Male", null, 150, null);
//         CurrentUserDTO dto2 = new CurrentUserDTO(1L, "John Doe", "john@example.com", "Belgian", 30, "Male", null, 150, null);

//         assertEquals(dto1.getId(), dto2.getId());
//         assertEquals(dto1.getName(), dto2.getName());
//         assertEquals(dto1.getEmail(), dto2.getEmail());
//     }

//     @Test
//     void testFieldEdgeCases() {
//         String longName = "A".repeat(255);
//         String longEmail = "B".repeat(100) + "@example.com";

//         CurrentUserDTO dto = new CurrentUserDTO(Long.MAX_VALUE, longName, longEmail, "", null, "", null, 0, null);

//         assertEquals(Long.MAX_VALUE, dto.getId());
//         assertEquals(longName, dto.getName());
//         assertEquals(longEmail, dto.getEmail());
//     }

//     @Test
//     void testNullHandling() {
//         CurrentUserDTO dto = new CurrentUserDTO(null, null, null, null, null, null, null, 0, null);

//         assertNull(dto.getId());
//         assertNull(dto.getName());
//         assertNull(dto.getEmail());
//     }
// }