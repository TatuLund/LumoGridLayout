package org.vaadin.addons.tatu;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.vaadin.addons.tatu.GridLayout.Align;
import org.vaadin.addons.tatu.GridLayout.Content;
import org.vaadin.addons.tatu.GridLayout.Gap;
import org.vaadin.addons.tatu.GridLayout.Justify;
import org.vaadin.addons.tatu.GridLayout.Orientation;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;

@Route("")
public class View extends VerticalLayout implements AppShellConfigurator {

	Random random = new Random();
	
    public View() {
        GridLayout gridLayout = new GridLayout(Orientation.BY_COLUMNS,8);
        gridLayout.setWidthFull();
        gridLayout.setHeight("500px");

        IntegerField count = new IntegerField("Components");
        count.setHasControls(true);
        count.addValueChangeListener(event -> {
            gridLayout.removeAll();
            getComponents(event.getValue()).forEach(component -> {
            	gridLayout.add(component);
            	if (random.nextDouble() > 0.5) {
            		gridLayout.setColSpan(component, 2);
            	}
            	if (random.nextDouble() > 0.5) {
            		gridLayout.setRowSpan(component, 2);
            	}
            });
        });
        count.setMin(0);
        count.setValue(15);

        IntegerField rowscols = new IntegerField("Rows/Cols");
        rowscols.setValue(8);
        rowscols.setHasControls(true);
        rowscols.addValueChangeListener(event -> {
          	gridLayout.setOrientation(gridLayout.getOrientation(), event.getValue());
        });
        rowscols.setMax(12);
        rowscols.setMin(0);
        
        Checkbox orientation = new Checkbox("By rows");
        orientation.addValueChangeListener(event -> {
            if (event.getValue()) {
                gridLayout.setOrientation(Orientation.BY_ROWS, 5);
                rowscols.setMax(6);
            } else {
                gridLayout.setOrientation(Orientation.BY_COLUMNS, 8);
                rowscols.setMax(12);
            }
        });

        Select<Gap> gap = new Select();
        gap.setLabel("Gap");
        gap.setItems(Gap.values());
        gap.addValueChangeListener(event -> {
        	gridLayout.setGap(event.getValue());
        });
        gap.setValue(Gap.SMALL);

        Select<Align> align = new Select();
        align.setLabel("Align");
        align.setItems(Align.values());
        align.addValueChangeListener(event -> {
        	gridLayout.setAlign(event.getValue());
        });
        Select<Content> content = new Select();
        content.setLabel("Content");
        content.setItems(Content.values());
        content.addValueChangeListener(event -> {
        	gridLayout.setContent(event.getValue());
        });
        Select<Justify> justify = new Select();
        justify.setLabel("Justify");
        justify.setItems(Justify.values());
        justify.addValueChangeListener(event -> {
        	gridLayout.setJustify(event.getValue());
        });
        
        HorizontalLayout tools = new HorizontalLayout();
        tools.add(count	, rowscols, gap, justify, content, align);
        add(gridLayout, tools, orientation);
    }

    public List<Div> getComponents(int count) {
    	return IntStream.range(0, count).mapToObj(i -> {
    		Div div = new Div();
    		div.setText("Component "+i);
    		div.getStyle().set("background", "#f0f0f0");
    		return div;
    	}).collect(Collectors.toList());
    }
}
