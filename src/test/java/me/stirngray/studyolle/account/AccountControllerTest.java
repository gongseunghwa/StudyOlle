package me.stirngray.studyolle.account;
import me.stirngray.studyolle.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired AccountRepository accountRepository;

    @MockBean JavaMailSender javaMailSender;


    @DisplayName("인증메일 확인 테스트 - 잘못된 값 입력")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "asdadasfd")
                .param("email","tmdghk502@naver.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }

    @Transactional
    @DisplayName("인증메일 확인 테스트 - 올바른 값 입력")
    @Test
    void checkEmailToken_with_corret_input() throws Exception {
        Account account = Account.builder()
                .email("tmdghk502@naver.com")
                .nickname("seunghwagong")
                .password("123232555")
                .build();

        Account newAccount = accountRepository.save(account);

//        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                .param("token",newAccount.getEmailCheckToken())
                .param("email",newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated());
    }

    @DisplayName("회원가입 화면이 보이는지 테스트")
    @Test
    void signUpForm() throws Exception{
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원가입이 실패 할 때 테스트")
    @Test
    void signUp_fail_test() throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("nickname","seunghwa")
                .param("email","tmdsd")
                .param("password","11233")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입이 성공 할 때 테스트")
    @Test
    void signUp_correct_input_test() throws Exception{
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "keesun")
                        .param("email", "keesun@email.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("keesun"));

        Account account = accountRepository.findByEmail("keesun@email.com");

        assertNotEquals(account.getPassword(),"12345678");
        assertNotNull(account.getEmailCheckToken());


        assertTrue(accountRepository.existsByEmail("keesun@email.com"));
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }
}