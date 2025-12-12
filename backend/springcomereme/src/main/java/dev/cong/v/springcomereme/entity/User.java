package dev.cong.v.springcomereme.entity;


import dev.cong.v.springcomereme.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@Table(name = "user")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User  implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(name = "email",unique = true)
    private  String email;

    @Column(name = "password")
    private  String password;

    @Column(name = "first_name")
    private  String firstName;

    @Column(name = "last_name")
    private  String lastName;



    @Column(name = "role")
    private  String role;

    @Column(name = "refresh_token")
    private  String refreshToken;

    @Column(name = "photo_url")
    private  String photoURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender",nullable = false)
    private Gender gender;

    @Column(name = "dob")
    private String dob;



    @OneToMany( cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                mappedBy = "user" )
    private List<Address> addresses;

    public void addAddress(Address address){
        if(this.addresses==null){
            this.addresses = new ArrayList<>();
        }
        this.addresses.add(address);
        address.setUser(this);
    }


    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<Order> orders;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
