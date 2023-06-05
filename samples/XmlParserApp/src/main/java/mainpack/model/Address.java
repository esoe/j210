package mainpack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private int id;
    private String country;
    private String city;
    private String street;
    private String latitude;
    private String longitude;
    private int personId;
}
