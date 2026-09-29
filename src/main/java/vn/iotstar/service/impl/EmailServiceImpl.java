package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.iotstar.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtp(String email, String otp, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText("""
            Xin chào,

            Mã OTP xác thực của bạn là: %s

            Mã OTP này có hiệu lực trong vòng 5 phút và chỉ sử dụng một lần.
            Vui lòng không chia sẻ mã này cho bất kỳ ai để bảo vệ an toàn cho tài khoản của bạn.

            Trân trọng,
            Hệ thống IOTSTAR SHOP
            """.formatted(otp));
        mailSender.send(message);
    }
}
