package MainFolder;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Controller
public class DefaultController {

    @GetMapping(value = "/")
        public String index(){
            return "redirect:/mines";
        }
        
    @GetMapping(value = "/registration")
    public String register(HttpServletRequest request, Model model){
        model.addAttribute("student", new Students());
        if (request.getParameter("success") != null) {
            return "registration-success";
        }
        return "registration";
    }


    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("student") Students student, BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()){
            model.addAttribute("validationErrors",true);
            return "/registration";
        }

        if (student.getEmail().equals("ebarkans3@edu.riga.lv")) {
            student.setRole(Role.ADMIN);
        } else {
            student.setRole(Role.USER);
        }
        
        // Check if the email already exists
        if (CSVManager.emailExists(student.getEmail())) {
            model.addAttribute("emailExists", true);
            model.addAttribute("email", student.getEmail());
            return "registration";
        }

        try {
            CSVManager.addStudentToCSV(student.getName(), student.getSurname(), student.getEmail(), student.getPassword(), student.getDay());
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error occurred while writing to the file.)");
            return "registration";
        }
        
        // Redirect to the success page
        return "redirect:/registration?success";
    }

    @GetMapping("/mines")
    public String mines(HttpServletRequest request, Model model){
        Students student = (Students) request.getSession().getAttribute("student");
        if (student == null) {
            return "mines";
        }
        return "mines";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        model.addAttribute("student", new Students());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("student") Students student, BindingResult bindingResult, HttpServletRequest request, Model model){

    if (student.getEmail().isEmpty()) {
        bindingResult.addError(new FieldError("student ","errorE", "Email is required"));
        return ("login");
    }
    if (student.getPassword().isEmpty()) {
        bindingResult.addError(new FieldError("student", "errorP", "Password is required"));
        return ("login");
    }

    Students fullStudents = CSVManager.getStudent(student.getEmail(), student.getPassword());
    if (fullStudents == null) {
        model.addAttribute("errorMessage", "Error occurred while reading the file.");
        return ("login");
    }

    request.getSession().setAttribute("student", fullStudents);
    return "mines";
    }


    @GetMapping("/account")
    public String account(HttpServletRequest request, Model model){
        Students student = (Students) request.getSession().getAttribute("student");
        if (student == null) {
            return "redirect:/login";
        }
        model.addAttribute("student", student);
        return "account";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/mines";
    }

    @GetMapping("/edit")
    public String edit(HttpServletRequest request, Model model){

        Students student = (Students) request.getSession().getAttribute("student");
        model.addAttribute("student", student);
        if (student == null) {
            return "redirect:/login";
        }
        model.addAttribute("student", student);
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("student") Students student, BindingResult bindingResult, HttpServletRequest request, Model model){
    
        if (bindingResult.hasErrors()){
            return "account";
        } else {

        Students currentStudent = (Students) request.getSession().getAttribute("student");
        //student.setRole(CSVManager.getStudent(currentStudent.getEmail(), currentStudent.getPassword()).getRole());

        if (student.getEmail().isEmpty()) {
            System.out.println("Email is empty");
            bindingResult.addError(new FieldError("student ","errorE", "Email is required"));
            return "account";
        }
        if (student.getPassword().isEmpty()) {
            System.out.println("Password is empty");
            bindingResult.addError(new FieldError("student", "errorP", "Password is required"));
            return "account";
        }

        Students fullStudents = CSVManager.getStudent(currentStudent.getEmail(), currentStudent.getPassword());
        if (fullStudents == null) {
            model.addAttribute("errorMessage", "Error occurred while reading the file.");
            return "account";
        }

        

        CSVManager.updateStudent(currentStudent.getEmail(), student.getName(), student.getSurname(), student.getEmail(), student.getPassword(), student.getDay(), currentStudent.getRole());
        request.getSession().setAttribute("student", student);

        //Pārbaude vai viss notiek pareizi ar user editu 
        //System.out.println("edit method called");
        //System.out.println("Current Student: "+currentStudent);
        //System.out.println("Role: " + student.getRole());
        //System.out.println("Form Data: " + student);
        //System.out.println("Full Student"+ fullStudents);
        //System.out.println("Updated student" + request.getSession().getAttribute("student"));

        return "redirect:/account";
        }
    } 

    
    @GetMapping("/users")
    public ModelAndView getUsers(@RequestParam(required = false) String sort){
        List<Students> users = MainFolder.viewStudents.viewAllUsers();

        if ("name".equals(sort)) {
            MainFolder.viewStudents.sortUsersByName(users);
        } else if ("role".equals(sort)) {
            MainFolder.viewStudents.sortUsersByRole(users);
        }

        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/users/search")
    public ModelAndView searchUsers(@RequestParam String email) {
        List<Students> users = MainFolder.viewStudents.searchUsersByEmail(MainFolder.viewStudents.viewAllUsers(), email);
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", users);
        return mav;
    }

    @PostMapping("/users")
    public Students createUser(@RequestBody Students newUser) {
        return newUser;
    }


    @PostMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestParam String email) {
        try {
            CSVManager.deleteUser(email);
            return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
