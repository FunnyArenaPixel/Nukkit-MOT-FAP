package cn.nukkit.form.element;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementDropdown extends Element {

    @SuppressWarnings("unused")
    private final String type = "dropdown"; //This variable is used for JSON import operations. Do NOT delete :) -- @Snake1999
    private String text = "";
    private List<String> options;
    @SerializedName("default")
    private int defaultOptionIndex = 0;
    /**
     * This option will show an exclamation icon that will display a tooltip if it is hovered.
     * @since 1.21.80
     */
    @Nullable
    private String tooltip;

    public ElementDropdown(String text) {
        this(text, new ArrayList<>());
    }

    public ElementDropdown(String text, List<String> options) {
        this(text, options, 0);
    }

    public ElementDropdown(String text, List<String> options, int defaultOption) {
        this.text = text;
        this.options = options;
        this.defaultOptionIndex = defaultOption;
    }

    public int getDefaultOptionIndex() {
        return defaultOptionIndex;
    }

    public void setDefaultOptionIndex(int index) {
        if (index >= options.size()) return;
        this.defaultOptionIndex = index;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addOption(String option) {
        addOption(option, false);
    }

    public void addOption(String option, boolean isDefault) {
        options.add(option);
        if (isDefault) this.defaultOptionIndex = options.size() - 1;
    }

    @Nullable
    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
