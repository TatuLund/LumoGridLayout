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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.EffectAllowed;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;

@Route("")
public class View extends VerticalLayout implements AppShellConfigurator {

    Random random = new Random();
    private GridLayout gridLayout;

    public View() {
        gridLayout = new GridLayout(Orientation.BY_COLUMNS, 8);
        DropTarget<GridLayout> dropTarget = DropTarget.create(gridLayout);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.setActive(true);
        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(div -> {
                gridLayout.remove(div);
                gridLayout.add(div);
            });
        });
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
            gridLayout.setOrientation(gridLayout.getOrientation(),
                    event.getValue());
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
        tools.add(count, rowscols, gap, justify, content, align);
        add(gridLayout, tools, orientation);
    }

    public List<Div> getComponents(int count) {
        return IntStream.range(0, count).mapToObj(i -> {
            Div div = new Div();
            div.addClassName("shadow-xs");
            div.getStyle().set("background",
                    "hsl(0, 0%," + (40 + random.nextInt(50)) + "%)");

            DragSource<Div> dragSource = DragSource.create(div);
            dragSource.setDraggable(true);
            dragSource.setEffectAllowed(EffectAllowed.MOVE);

            DropTarget<Div> dropTarget = DropTarget.create(div);
            dropTarget.setDropEffect(DropEffect.MOVE);
            dropTarget.setActive(true);
            dropTarget.addDropListener(event -> {
                event.getDragSourceComponent().ifPresent(dragged -> {
                    if (dragged == div) {
                        return;
                    }
                    int indexDiv = gridLayout.indexOf(div);
                    gridLayout.remove(dragged);
                    gridLayout.addComponentAtIndex(indexDiv, dragged);
                });
            });

            Div header = new Div();
            header.setWidth("100%");
            header.getStyle().set("display", "flex");
            header.getStyle().set("justify-content", "space-between");
            header.add(new Span("Component " + i));
            header.addClassName("shadow-xs");

            MenuBar menu = new MenuBar();
            menu.setWidth("32px");
            menu.addThemeVariants(MenuBarVariant.LUMO_SMALL);
            menu.addItem(VaadinIcon.ARROW_RIGHT.create(), event -> gridLayout
                    .setColSpan(div, adjustColSpan(div, 1)));
            menu.addItem(VaadinIcon.ARROW_LEFT.create(), event -> gridLayout
                    .setColSpan(div, adjustColSpan(div, -1)));
            menu.addItem(VaadinIcon.ARROW_DOWN.create(), event -> gridLayout
                    .setRowSpan(div, adjustRowSpan(div, 1)));
            menu.addItem(VaadinIcon.ARROW_UP.create(), event -> gridLayout
                    .setRowSpan(div, adjustRowSpan(div, -1)));

            header.add(menu);
            div.add(header);

            return div;
        }).collect(Collectors.toList());
    }

    private int adjustColSpan(Div div, int i) {
        int s = gridLayout.getColSpan(div);
        if (i == 0) return s;
        if ((s + i) > 0 && (s + i) < 13) { 
            return s+i;
        } else if (i > 0) {
            return s;
        } else {
            return 1;
        }
    }

    private int adjustRowSpan(Div div, int i) {
        int s = gridLayout.getRowSpan(div);
        if (i == 0) return s;
        if ((s + i) > 0 && (s + i) < 7) { 
            return s+i;
        } else if (i > 0) {
            return s;
        } else {
            return 1;
        }
    }

    private ComponentEventListener<ClickEvent<MenuItem>> Text(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
