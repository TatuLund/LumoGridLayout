package org.vaadin.addons.tatu;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;

/**
 * Java API of GridLayout based on Lumo Grid utility classes
 * 
 * Supports max 12 columns
 * Supports max 6 rows
 * 
 * @author Tatu Lund
 */
@CssImport("./grid-layout-styles.css")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class GridLayout extends Div {

    /**
     * Classes for defining the space between items in a flexbox or grid layout.
     */
    public enum Gap {
        NONE(""), XSMALL("xs"), SMALL("s"), MEDIUM("m"), LARGE("l"), XLARGE("xl");

        private String gap;

        Gap(String gap) {
            this.gap = gap;
        }

        String getGap() {
            return "gap-" + gap;
        }

        String getGap(Orientation orientation) {
            if (orientation == Orientation.BY_ROWS) {
                return "gap-y-" + gap;
            } else {
                return "gap-x-" + gap;
            }
        }
    }

    /**
     * Classes for aligning items along a flexbox’s cross axis or a grid’s block
     * axis.
     */
    public enum Align {
        BASELINE("items-baseline"), CENTER("items-center"), END(
                "items-end"), START("items-start"), STRETCH("items-stretch");

        private String align;

        Align(String align) {
            this.align = align;
        }

        String getAlign() {
            return align;
        }
    }

    /**
     * Classes for aligning items along a flexbox’s main axis or a grid’s inline
     * axis
     */
    public enum Justify {
        CENTER("justify-center"), END("justify-end"), START(
                "justify-start"), AROUND("justify-around"), BETWEEN(
                        "justify-between"), EVENLY("justify-evenly");

        private String justify;

        Justify(String justify) {
            this.justify = justify;
        }

        String getJustify() {
            return justify;
        }
    }

    /**
     * Classes for distributing space around and between items along a grid’s
     * block axis.
     */
    public enum Content {
        CENTER("content-center"), END("content-end"), START(
                "content-start"), AROUND("content-around"), BETWEEN(
                        "content-between"), EVENLY(
                                "content-evenly"), STRETCH("content-stretch");

        private String content;

        Content(String content) {
            this.content = content;
        }

        String getJustify() {
            return content;
        }
    }

    /**
     * Orientation mode of the GridLayout
     */
    public enum Orientation {
        BY_ROWS, BY_COLUMNS
    }

    /**
     * Create GridLayout without settings
     */
    public GridLayout() {
        addClassName("grid");
    }

    /**
     * Create GridLayout with given settings.
     * 
     * @param orientation
     *            Define row or column orientation
     * @param number
     *            Define max number of columns (upto 12) or rows (upto 6)
     */
    public GridLayout(Orientation orientation, int number) {
        addClassName("grid");
        setOrientationInternal(orientation, number);
    }

    /**
     * Set Gap uniformly. Overrides row/column Gap.
     * 
     * @param gap
     *            Gap class
     */
    public void setGap(Gap gap) {
        cleanClasses("gap");
    	if (gap != Gap.NONE ) {
            addClassName(gap.getGap());
    	}
    }

    /**
     * Set column or row Gap. Overrides uniform Gap.
     * 
     * @param gap
     *            The Gap class
     * @param orientation
     *            Define row or column orientation
     */
    public void setGap(Gap gap, Orientation orientation) {
        cleanClasses("gap");
    	if (gap != Gap.NONE ) {
            addClassName(gap.getGap(orientation));
    	}
    }

    /**
     * Set GridLayout be row / column
     * 
     * @param orientation
     *            Define row or column orientation
     * @param number
     *            Define max number of columns (upto 12) or rows (upto 6)
     */
    public void setOrientation(Orientation orientation, int number) {
        cleanClasses("grid-rows", "grid-cols");

        setOrientationInternal(orientation, number);
    }

    /**
     * Get current orientation.
     * 
     * @return Orientation
     */
    public Orientation getOrientation() {
        if (getClassNames().contains("grid-flow-col")) {
            return Orientation.BY_ROWS;
        }
        return Orientation.BY_COLUMNS;
    }

    private void setOrientationInternal(Orientation orientation, int number) {
        String className = null;
        if (orientation == Orientation.BY_COLUMNS) {
        	removeClassName("grid-flow-col");
            if (number > 12) {
                throw new IllegalArgumentException(
                        "Maximum 12 rows supported in GridLayout");
            }
            className = "grid-cols-";
        } else {
        	addClassName("grid-flow-col");
            if (number > 6) {
                throw new IllegalArgumentException(
                        "Maximum 6 rows supported in GridLayout");
            }
            className = "grid-rows-";
        }
        className += number;
        addClassName(className);
    }

    private void cleanClasses(String... prefixes) {
        for (String prefix : prefixes) {
            getElement().getClassList()
                    .removeIf(className -> className.startsWith(prefix));
        }
    }

    /**
     * Set colspan of the given child, overrides previous setting
     * 
     * @param child
     *            Child component (must be an immediate child)
     * @param colSpan
     *            Colspan of the child component
     */
    public void setColSpan(Component child, int colSpan) {
        if (getChildren().filter(comp -> comp == child).findFirst()
                .isPresent()) {
            cleanClasses("col-span");
            child.getElement().getClassList().add("col-span-" + colSpan);
        } else {
            throw new IllegalArgumentException(
                    "Given component is not immediate child of GridLayout");
        }
    }

    /**
     * Set rowspan of the given child, overrides previous setting
     * 
     * @param child
     *            Child component (must be an immediate child)
     * @param rowSpan
     *            Rowspan of the child component
     */
    public void setRowSpan(Component child, int rowSpan) {
        if (getChildren().filter(comp -> comp == child).findFirst()
                .isPresent()) {
            cleanClasses("row-span");
            child.getElement().getClassList().add("row-span-" + rowSpan);
        } else {
            throw new IllegalArgumentException(
                    "Given component is not immediate child of GridLayout");
        }
    }

    /**
     * Set mode of aligning items along a flexbox’s cross axis or a grid’s block
     * axis.
     * 
     * @param align The Align mode class
     */
    public void setAlign(Align align) {
        cleanClasses("items");
        addClassName(align.getAlign());
    }

    /**
     * Set mode of aligning items along a flexbox’s main axis or a grid’s inline
     * axis
     * 
     * @param justify The Justify mode class
     */
    public void setJustify(Justify justify) {
        cleanClasses("justify");
        addClassName(justify.getJustify());
    }

    /**
     * Set mode of distributing space around and between items along a grid’s
     * block axis.
     * 
     * @param content The Content mode class
     */
    public void setContent(Content content) {
        cleanClasses("content");
        addClassName(content.getJustify());
    }
}
