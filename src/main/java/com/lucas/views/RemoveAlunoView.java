package com.lucas.views;

import com.lucas.constants.Constants;
import com.lucas.model.Aluno;
import com.lucas.services.AlunoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@PageTitle("Excluir Aluno")
@Route(value = "remove-aluno")
@RolesAllowed("ROLE_ADMIN")
public class RemoveAlunoView extends VerticalLayout implements SelectionListener<Grid<Aluno>, Aluno> {


    private Grid<Aluno> grid;
    private final AlunoService alunoService;
    private Button remove;
    private Button cancel;
    private Set<Aluno> selected;
    public RemoveAlunoView(AlunoService alunoService) {
        this.alunoService = alunoService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        createFieldVariables();
        configuraGrid();
        add(grid, createButtonLayout());

        carregaAlunos();
    }

    private void carregaAlunos() {
        grid.setItems(alunoService.findAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(this);
    }

    private Component createButtonLayout() {
        remove.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel.addClickListener(e -> closeView());
        remove.addClickListener(e -> removeSelected());

        return new HorizontalLayout(remove, cancel);
    }

    private void removeSelected() {
        selected.forEach(alunoService::remove);
        Notification notification = Notification.show(Constants.STUDENT_REMOVED);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_END);
        grid.setItems(alunoService.findAll());
    }

    private void closeView() {
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void configuraGrid() {
        grid.setSizeFull();
        grid.setColumns("nome", "idade");
        grid.addColumn(s -> s.getNacionalidade()).setHeader(Constants.COUNTRY);
        grid.addColumn(s -> s.getCep()).setHeader(Constants.ZIP_CODE);

        grid.addComponentColumn(s -> {
            Icon icon;

            if (s.getStatus().getNome().equals("ATIVO")) {
                icon = VaadinIcon.CHECK_CIRCLE_O.create();
                icon.setColor("#006400");
            } else if (s.getStatus().getNome().equals("INATIVO")) {
                icon = VaadinIcon.CLOSE_CIRCLE.create();
                icon.setColor("red");
            } else {
                icon = VaadinIcon.CIRCLE.create();
                icon.setColor("orange");
            }

            return icon;
        }).setHeader(Constants.STATUS);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void createFieldVariables() {
        grid = new Grid<>(Aluno.class);
        remove = new Button(Constants.REMOVE);
        cancel = new Button(Constants.CANCEL);
    }

    @Override
    public void selectionChange(SelectionEvent<Grid<Aluno>, Aluno> event) {
        selected = event.getAllSelectedItems();
    }
}
