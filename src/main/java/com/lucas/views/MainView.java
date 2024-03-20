package com.lucas.views;

import com.lucas.constants.Constants;
import com.lucas.model.Aluno;
import com.lucas.services.AlunoService;
import com.lucas.services.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.annotation.security.RolesAllowed;
import java.text.MessageFormat;

@PageTitle(value = "Home")
@Route(value = "")
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MainView extends VerticalLayout {

	private final AlunoService alunoService;
	private final SecurityService securityService;
	private LogoLayout logoLayout;
	private Grid<Aluno> grid;
	private TextField filterField;
	private Checkbox themeToggle;
	private static boolean isChecked;

	public MainView(AlunoService alunoService, SecurityService securityService) {
		this.alunoService = alunoService;
		this.securityService = securityService;
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		createFieldVariables();
		configuraGrid();
		add(logoLayout, createToolbar(), grid);
		carregaAlunos();
	}

	private Checkbox createToggle() {
		themeToggle = new Checkbox("Tema escuro");
		themeToggle.setValue(isChecked);
		themeToggle.addValueChangeListener(e -> {
			MainView.isChecked = !isChecked;
			setTheme(isChecked);
		});

		return themeToggle;
	}

	private void setTheme(boolean dark) {
		var js = MessageFormat.format("""
					document.documentElement.setAttribute("theme", "{0}")
				""", dark ? Lumo.DARK : Lumo.LIGHT);
		getElement().executeJs(js);
	}

	private Component createToolbar() {
		filterField.setPlaceholder("Filtrar por nome");
		filterField.setClearButtonVisible(true);
		filterField.setValueChangeMode(ValueChangeMode.LAZY);
		filterField.addValueChangeListener(e -> updateAlunos());

		Button addAlunoButton = new Button(Constants.ADD_STUDENT);
		Button removeAlunoButton = new Button(Constants.REMOVE_STUDENT);
		Button logout = new Button("Logout");

		addAlunoButton.addClickListener(e -> {
			if (securityService.hasRole("ROLE_ADMIN")) {
				getUI().ifPresent(ui -> ui.navigate("add-aluno"));
			} else {
				getUI().ifPresent(ui -> ui.access(() -> {
					Notification notification = Notification.show("Acesso restrito a administradores.",
							2000, Notification.Position.TOP_END);
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}));
			}
		});
		removeAlunoButton.addClickListener(e -> {
			if (securityService.hasRole("ROLE_ADMIN")) {
				getUI().ifPresent(ui -> ui.navigate("remove-aluno"));
			} else {
				getUI().ifPresent(ui -> ui.access(() -> {
					Notification notification = Notification.show("Acesso restrito a administradores.",
							2000, Notification.Position.TOP_END);
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				}));
			}
		});
		logout.addClickListener(e -> securityService.logout());

		return new HorizontalLayout(filterField, addAlunoButton, removeAlunoButton, logout, createToggle());
	}

	private void updateAlunos() {
		grid.setItems(alunoService.find(filterField.getValue()));
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
		}).setHeader("Status");

		grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	private void carregaAlunos() {

		grid.setItems(alunoService.findAll());
	}

	private void createFieldVariables() {
		this.logoLayout = new LogoLayout();
		this.grid = new Grid<>(Aluno.class);
		this.filterField = new TextField();
	}
}
