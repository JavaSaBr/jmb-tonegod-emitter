package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static java.lang.Math.min;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.ColorInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.util.UiUtils;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.ColorInfluencer;

/**
 * The implementation of the element for {@link ColorInfluencerControl} for editing color and interpolation.
 *
 * @author JavaSaBr
 */
public class ColorInterpolationElement extends
        InterpolationElement<ColorInfluencer, ColorPicker, ColorInfluencerControl> {

    public ColorInterpolationElement(@NotNull ColorInfluencerControl control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return Messages.MODEL_PROPERTY_COLOR;
    }

    @Override
    @FxThread
    protected @NotNull ColorPicker createEditableControl() {

        var colorPicker = new ColorPicker();
        colorPicker.prefWidthProperty()
                .bind(widthProperty().multiply(0.35));

        FxControlUtils.onColorChange(colorPicker, this::apply);
        FxUtils.addClass(colorPicker, CssClasses.PROPERTY_CONTROL_COLOR_PICKER);

        return colorPicker;
    }

    /**
     * Apply the new value.
     */
    @FxThread
    private void apply(@NotNull Color newValue) {
        if (!isIgnoreListeners()) {
            control.requestToChange(UiUtils.from(newValue), getIndex());
        }
    }

    @Override
    @FxThread
    protected void reloadImpl() {

        var influencer = control.getInfluencer();
        var newColor = influencer.getColor(getIndex());

        var red = min(newColor.getRed(), 1F);
        var green = min(newColor.getGreen(), 1F);
        var blue = min(newColor.getBlue(), 1F);
        var alpha = min(newColor.getAlpha(), 1F);

        editableControl.setValue(new Color(red, green, blue, alpha));

        super.reloadImpl();
    }
}
