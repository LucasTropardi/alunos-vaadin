package com.lucas.views;

import com.lucas.constants.Constants;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@org.springframework.stereotype.Component
public class LoginViewFactory {

    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    private class LoginForm {

        private VerticalLayout root;
        private TextField username;
        private PasswordField password;
        private Button login;
        private Button signup;

        public LoginForm init() {


            username = new TextField("Usuário");
            password = new PasswordField("Senha");
            login = new Button("Entrar");
            signup = new Button("Cadastre-se");

            login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            signup.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            login.addClickListener(e -> login());
            signup.addClickListener(e -> signup());

            root = new VerticalLayout();
            root.setAlignItems(FlexComponent.Alignment.CENTER);
            root.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            root.setMargin(true);

            return this;
        }

        private void signup() {
            login.getUI().ifPresent(ui -> ui.navigate("signup"));
        }

        private void login() {
            try {
                var auth = new UsernamePasswordAuthenticationToken(username.getValue(), password.getValue());
                var authenticated = authenticationProvider.authenticate(auth);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                login.getUI().ifPresent(ui -> ui.navigate(""));
            } catch (AuthenticationException e) {
                Notification notification = Notification.show("Usuário e/ou senha inválidos.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);            }
        }

        public Component layout() {
            root.add(new Image(Constants.LOGIN_IMAGE_URL, "imagem login"));
            root.add(username);
            root.add(password);
            root.add(new HorizontalLayout(login, signup));
            return root;
        }
    }

    public Component create() {
        return new LoginForm().init().layout();
    }
}
