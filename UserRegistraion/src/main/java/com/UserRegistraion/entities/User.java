package com.UserRegistraion.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        private String city;

        private String email;

        private String mobile;

        private String state;

        private String country;

        @Column(name = "pin_code")
        private String pinCode;

        private String password;

        @Column(name = "user_photo")
        private String userPhoto;
        private String photoPath;


        public void setPhotoPath(String photoPath) {
                this.photoPath = photoPath;
        }

        public String getPhotoPath() {
                return photoPath;
        }

}
