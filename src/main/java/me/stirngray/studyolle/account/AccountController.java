package me.stirngray.studyolle.account;

import lombok.RequiredArgsConstructor;
import me.stirngray.studyolle.domain.Account;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.print.attribute.standard.PresentationDirection;
import javax.validation.Valid;
import java.net.http.HttpClient;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    private final SignUpFormValidator signUpFormValidator;

    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }
        //TODO 전달받은 signUpForm객체를 저장
        Account account = accountService.newAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String CheckEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view ="account/checked-email";
        if(account == null){
            model.addAttribute("error","wrong email");
            return view;
        }

        if(!account.isVerifiedToken(token)){
            model.addAttribute("error","wrong token");
            return view;
        }

        account.completeSignUp();
        accountService.login(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname",account.getNickname());

        return view;
    }




}
