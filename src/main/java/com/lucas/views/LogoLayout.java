package com.lucas.views;

import com.lucas.constants.Constants;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class LogoLayout extends HorizontalLayout {

    private Image image;

    public LogoLayout() {
        image = new Image(Constants.LOGO_URL, "Minha Imagem");
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(image);
    }
}
