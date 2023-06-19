package com.UserRegistraion.controller;
import com.UserRegistraion.entities.User;

import com.UserRegistraion.payload.UserDTO;
import com.UserRegistraion.service.UserService;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(
            @RequestParam("userPhoto") MultipartFile userPhoto,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("city") String city,
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("state") String state,
            @RequestParam("country") String country,
            @RequestParam("pinCode") String pinCode,
            @RequestParam("password") String password
    ) throws IOException {
        // Create a new User object and set its fields from the request parameters
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setState(state);
        user.setCountry(country);
        user.setPinCode(pinCode);
        user.setPassword(password);
        user.setPassword(passwordEncoder.encode(password));

        // If the user uploaded a photo, save it to the project directory and set the user's photoPath field
        if (userPhoto != null && !userPhoto.isEmpty()) {
            String directory = "D:\\userregistration\\UserRegistraion\\UserRegistraion\\src\\main\\resources\\static\\images";
            String filename = UUID.randomUUID().toString() + "-" + userPhoto.getOriginalFilename();
            String filepath = directory + "/" + filename;

            // Save the photo to the project directory
            userPhoto.transferTo(new File(filepath));

            // Set the photo path in the User object
            user.setPhotoPath(filepath);
        }

        // Save the user object to the database using the UserService
        User savedUser = userService.saveUser(user);

        // Map the saved User object to UserDto
        UserDTO savedUserDto = new UserDTO();
        savedUserDto.setId(savedUser.getId());
        savedUserDto.setFirstName(savedUser.getFirstName());
        savedUserDto.setLastName(savedUser.getLastName());
        savedUserDto.setCity(savedUser.getCity());
        savedUserDto.setEmail(savedUser.getEmail());
        savedUserDto.setMobile(savedUser.getMobile());
        savedUserDto.setState(savedUser.getState());
        savedUserDto.setCountry(savedUser.getCountry());
        savedUserDto.setPinCode(savedUser.getPinCode());
        savedUserDto.setUserPhoto(savedUser.getPhotoPath());
        savedUserDto.setPassword(null);

        return ResponseEntity.ok(savedUserDto);
    }
    //http://localhost:1010/api/users/download
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadUsers() {
        List<User> users = userService.getAllUsers();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");
            createHeaderRow(sheet);
            populateDataRows(sheet, users);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            byte[] excelBytes = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "users.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle the exception appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("City");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("Mobile");
        headerRow.createCell(6).setCellValue("State");
        headerRow.createCell(7).setCellValue("Country");
        headerRow.createCell(8).setCellValue("Pin Code");
        headerRow.createCell(9).setCellValue("Photo Path");
        // Add more columns as needed
    }

    private void populateDataRows(Sheet sheet, List<User> users) {
        int rowNumber = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(user.getCity());
            row.createCell(4).setCellValue(user.getEmail());
            row.createCell(5).setCellValue(user.getMobile());
            row.createCell(6).setCellValue(user.getState());
            row.createCell(7).setCellValue(user.getCountry());
            row.createCell(8).setCellValue(user.getPinCode());
            row.createCell(9).setCellValue(user.getPhotoPath());
            // Populate more columns as needed
        }
    }

}
