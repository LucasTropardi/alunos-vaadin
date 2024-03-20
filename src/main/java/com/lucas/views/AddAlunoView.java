package com.lucas.views;

import com.lucas.constants.Constants;
import com.lucas.model.Aluno;
import com.lucas.model.Status;
import com.lucas.services.AlunoService;
import com.lucas.services.StatusService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Adicionar Aluno")
@Route(value = "add-aluno")
@RolesAllowed("ROLE_ADMIN")
public class AddAlunoView extends VerticalLayout {

    private final StatusService statusService;
    private final AlunoService alunoService;

    private TextField idade;
    private TextField cep;
    private TextField nome;
    private TextField nacionalidade;
    private ComboBox<Status> status;
    private LogoLayout image;

    private Button save;
    private Button cancel;

    private Aluno aluno;
    private Binder<Aluno> binder;

    public AddAlunoView(StatusService statusService, AlunoService alunoService) {
        setAlignItems(Alignment.CENTER);

        this.statusService = statusService;
        this.alunoService = alunoService;
        createVariables();
        createStatus();
        createBinder();
        add(image);
        add(createFormLayout());
    }

    private void createBinder() {
        aluno = new Aluno();
        binder = new BeanValidationBinder<>(Aluno.class);
        binder.bindInstanceFields(this);
    }

    private void createStatus() {
        List<Status> statusItems =  statusService.findAll();
        status.setItems(statusItems);
        status.setValue(statusItems.get(0));
        status.setItemLabelGenerator(Status::getNome);
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nome, idade, nacionalidade, cep, status, createButtons());
        formLayout.setColspan(image, 2);
        formLayout.setColspan(nome, 2);
        return formLayout;
    }

    private Component createButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel.addClickListener(e -> closeView());
        save.addClickListener(e -> saveAluno());

        return new HorizontalLayout(save, cancel);
    }

    private void saveAluno() {
        try {
            binder.writeBean(aluno);
            alunoService.save(aluno);
            clearFields();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        UI.getCurrent().navigate("/");

        Notification notification = Notification.show(Constants.STUDENT_SAVED);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_END);
    }

    private void closeView() {
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void createVariables() {
        idade = new TextField(Constants.AGE);
        cep = new TextField(Constants.ZIP_CODE);
        nome = new TextField(Constants.NAME);
        nacionalidade = new TextField(Constants.COUNTRY);
        status = new ComboBox<>(Constants.STATUS);
        image = new LogoLayout();
        save = new Button(Constants.SAVE);
        cancel = new Button(Constants.CANCEL);
    }
}
