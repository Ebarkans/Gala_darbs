package lv.rvt;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import MainFolder.CSVManager;
import MainFolder.Students;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

     @Test
     public void testEmailExists() {
         String email = "test@example.com";
 
         boolean exists = CSVManager.emailExists(email);
 
         assertTrue(exists, "Email should exist in the CSV file");
     }
 
     @Test
     public void testEmailDoesNotExist() {
         String email = "nonexistent@example.com";
 
         boolean exists = CSVManager.emailExists(email);
 
         assertFalse(exists, "Email should not exist in the CSV file");
     }

     @Test
     public void testGetStudent() {
         String email = "test@example.com";
         String password = "password";
 
         Students student = CSVManager.getStudent(email, password);
 
         assertNotNull(student, "Student should not be null");
         assertEquals(email, student.getEmail(), "Email should match the input");
         assertEquals(password, student.getPassword(), "Password should match the input");
         // Add more assertions for the other properties if necessary
     }
}
