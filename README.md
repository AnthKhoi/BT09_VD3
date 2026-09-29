# BT09 - VÍ DỤ 3: HỆ THỐNG QUẢN LÝ SHOP ĐẦY ĐỦ VỚI SPRING BOOT 4 + SPRING SECURITY 7 + CLOUDINARY + OTP GMAIL

Dự án triển khai toàn bộ chức năng theo bài tập **Ví dụ 3** của tài liệu hướng dẫn:
- **Xác thực hoàn chỉnh**: Đăng ký xác nhận OTP qua Gmail, Đăng nhập lưu Session, Quên mật khẩu gửi OTP đổi mật khẩu qua Mail.
- **Quản lý User**: Phân quyền ROLE_ADMIN và ROLE_USER, CRUD User, tìm kiếm và phân trang, đếm tổng số User và đếm số sản phẩm của từng User.
- **Quản lý Product**: CRUD Product thuộc quyền sở hữu của User, tìm kiếm và phân trang, upload và xóa ảnh trực tiếp trên Cloudinary.
- **Kiến trúc chuẩn**: Spring Boot 4.1.1 + Spring Security 7.1.x + MapStruct 1.6.3 + Thymeleaf + SQL Server (database: webst3).

---

## 🚀 Công nghệ sử dụng

| Thành phần | Công nghệ |
| :--- | :--- |
| **Backend Framework** | Spring Boot 4.1.1 |
| **Bảo mật & Phân quyền** | Spring Security 7.1.x (Form Login, Session Management, BCrypt) |
| **Java** | Java 21+ / JDK 26 |
| **Cơ sở dữ liệu** | Microsoft SQL Server (Database: webst3) |
| **ORM** | Spring Data JPA / Hibernate ORM |
| **Giao diện (View)** | Thymeleaf + Thymeleaf Extras Spring Security 6 |
| **Layout** | Thymeleaf standard fragments |
| **Mapping Object** | MapStruct 1.6.3 (UserMapper, ProductMapper) |
| **Gửi Mail OTP** | Spring Boot Starter Mail (Gmail SMTP) |
| **Lưu trữ hình ảnh** | Cloudinary Java SDK (cloudinary-http5) |
| **Validation** | Jakarta Validation |
| **Tiện ích** | Lombok, Spring Boot DevTools |

---

## 📁 Cấu trúc thư mục dự án

`
Vi_DU_3/
├── .env                                       # Cấu hình môi trường (DB, Server, Mail, Cloudinary)
├── .env.example                               # Mẫu cấu hình môi trường
├── .gitignore                                 # Bỏ qua file nhạy cảm và target/
├── pom.xml                                    # Danh sách dependency và build plugins
├── README.md                                  # Hướng dẫn chi tiết
└── src/
    └── main/
        ├── java/vn/iotstar/
        │   ├── ShopApplication.java           # Main application class, tự động bật trình duyệt
        │   ├── config/
        │   │   ├── CloudinaryConfig.java      # Cấu hình Bean Cloudinary API
        │   │   ├── EncodingConfig.java        # Cấu hình lọc UTF-8
        │   │   └── SecurityConfig.java        # Cấu hình phân quyền Spring Security 7
        │   ├── controller/
        │   │   ├── AuthController.java        # Xử lý Login, Register, Verify OTP, Forgot/Reset Password
        │   │   ├── HomeController.java        # Trang chủ Dashboard, thống kê Users & Products
        │   │   ├── ProductController.java     # CRUD, tìm kiếm, phân trang và upload ảnh sản phẩm
        │   │   ├── UserController.java        # Quản lý tài khoản người dùng (ADMIN)
        │   │   └── ErrorController.java       # Xử lý trang thông báo lỗi
        │   ├── dto/
        │   │   ├── ForgotPasswordDTO.java     # DTO quên mật khẩu
        │   │   ├── LoginDTO.java              # DTO đăng nhập
        │   │   ├── ProductDTO.java            # DTO sản phẩm
        │   │   ├── RegisterDTO.java           # DTO đăng ký
        │   │   ├── ResetPasswordDTO.java      # DTO đặt lại mật khẩu
        │   │   └── UserDTO.java               # DTO người dùng
        │   │   └── VerifyOtpDTO.java          # DTO xác thực OTP
        │   ├── entity/
        │   │   ├── Role.java                  # Entity bảng roles
        │   │   ├── User.java                  # Entity bảng users (quan hệ 1 - n với products)
        │   │   ├── Product.java               # Entity bảng products
        │   │   └── OtpToken.java              # Entity bảng otp_tokens
        │   ├── mapper/
        │   │   ├── ProductMapper.java         # MapStruct Product <-> ProductDTO
        │   │   └── UserMapper.java            # MapStruct User <-> UserDTO
        │   ├── repository/
        │   │   ├── OtpTokenRepository.java    # Repository OTP Tokens
        │   │   ├── ProductRepository.java     # Repository Products (Search, Count by User)
        │   │   ├── RoleRepository.java        # Repository Roles
        │   │   └── UserRepository.java        # Repository Users (Search, Count products)
        │   ├── security/
        │   │   ├── CustomUserDetails.java     # Implement UserDetails
        │   │   └── CustomUserDetailsService.java # Tải user theo username
        │   └── service/
        │       ├── AuthService.java           # Interface nghiệp vụ xác thực
        │       ├── CloudinaryService.java     # Interface upload/xóa ảnh Cloudinary
        │       ├── EmailService.java          # Interface gửi email qua Gmail SMTP
        │       ├── OtpService.java            # Interface sinh và kiểm tra OTP
        │       ├── ProductService.java        # Interface nghiệp vụ Product
        │       ├── UserService.java           # Interface nghiệp vụ User
        │       └── impl/                      # Các lớp hiện thực dịch vụ (ServiceImpl)
        └── resources/
            ├── application.properties         # Cấu hình Spring Boot và import .env
            ├── data.sql                       # Dữ liệu khởi tạo roles mẫu
            ├── static/
            │   ├── css/app.css                # Toàn bộ CSS phong cách giao diện
            │   └── images/                    # Ảnh mặc định
            └── templates/
                ├── auth/                      # Các view: login, register, verify-otp, forgot-password, reset-password
                ├── fragments/                 # Header & Footer fragments
                ├── layouts/layout.html        # Layout tổng thể
                ├── products/                  # List và Form sản phẩm
                ├── users/                     # List và Form quản lý user
                ├── home.html                  # Dashboard tổng quan
                └── error.html                 # Trang hiển thị lỗi
`

---

## ⚙️ Hướng dẫn cài đặt & Khởi chạy

### 1. Cấu hình file .env
Tạo file .env tại thư mục gốc với nội dung:
`properties
# ===============================
# DATABASE (SQL Server)
# ===============================
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=webst3;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
DB_USERNAME=sa
DB_PASSWORD=13234

# ===============================
# JPA
# ===============================
DDL_AUTO=update
SHOW_SQL=true

# ===============================
# SERVER
# ===============================
SERVER_PORT=8089

# ===============================
# SMTP GMAIL (Dùng gửi OTP)
# ===============================
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_app_password

# ===============================
# CLOUDINARY (Dùng lưu ảnh sản phẩm)
# ===============================
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
`

### 2. Khởi chạy
- **Bằng IntelliJ IDEA:** Mở thư mục dự án và bấm **Run** tại file ShopApplication.java.
- **Bằng Terminal:**
  `ash
  mvn spring-boot:run
  `
- Ứng dụng tích hợp tự động mở trình duyệt web tại địa chỉ: http://localhost:8089/login.

---

## 🔑 Tài khoản kiểm thử mặc định (Seed Data)

Khi khởi chạy, ứng dụng tự động kiểm tra và tạo sẵn tài khoản Admin:
- **Username:** dmin
- **Email:** lehuynhanhkhoi2006@gmail.com
- **Mật khẩu:** 123456
- **Quyền hạn:** ROLE_ADMIN
