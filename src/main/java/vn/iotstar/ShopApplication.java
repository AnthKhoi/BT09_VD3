package vn.iotstar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class ShopApplication {

    @Value("${server.port:8089}")
    private String serverPort;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserOnStartup() {
        String url = "http://localhost:" + serverPort + "/login";
        System.out.println(">>> [AUTO-LAUNCH] Dang tu dong mo trinh duyet tai: " + url);
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }
        } catch (Exception e) {
            System.err.println("Khong the mo trinh duyet: " + e.getMessage());
        }
    }

    @Bean
    CommandLineRunner initRolesAndAdmin(
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() ->
                roleRepository.save(Role.builder().name("ROLE_USER").build())
            );
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() ->
                roleRepository.save(Role.builder().name("ROLE_ADMIN").build())
            );

            // Seed admin account
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                    .username("admin")
                    .email("lehuynhanhkhoi2006@gmail.com")
                    .fullName("Administrator")
                    .password(passwordEncoder.encode("123456"))
                    .role(adminRole)
                    .enabled(true)
                    .build();
                userRepository.save(admin);
            }
        };
    }
}
