package mainpack.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private List<Address> addresses;
}
