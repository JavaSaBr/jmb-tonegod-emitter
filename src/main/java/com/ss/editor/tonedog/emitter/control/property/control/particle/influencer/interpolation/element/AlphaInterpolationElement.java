package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.AlphaInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.AlphaInfluencer;

/**
 * The implementation of the element for {@link AlphaInfluencer} for editing alpha and interpolation.
 *
 * @author JavaSaBr
 */
public class AlphaInterpolationElement extends
        InterpolationElement<AlphaInfluencer, FloatTextField, AlphaInfluencerControl> {

    public AlphaInterpolationElement(@NotNull AlphaInfluencerControl control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return Messages.MODEL_PROPERTY_ALPHA;
    }

    @Override
    @FxThread
    protected @NotNull FloatTextField createEditableControl() {

        var textField = new FloatTextField();
        textField.setMinMax(0F, 1F);
        textField.prefWidthProperty()
                .bind(widthProperty().multiply(0.35));

        FxControlUtils.onValueChange(textField, this::apply);
        FxUtils.addClass(textField, CssClasses.ABSTRACT_PARAM_CONTROL_VECTOR2F_FIELD);

        return textField;
    }

    /**
     * Apply the new value.
     */
    @FxThread
    private void apply(@NotNull Float newValue) {
        if (!isIgnoreListeners()) {
            getControl().requestToChange(newValue, getIndex());
        }
    }

    @Override
    @FxThread
    public void reload() {

        var influencer = getControl().getInfluencer();
        var alpha = influencer.getAlpha(getIndex());

        var editableControl = getEditableControl();
        var caretPosition = editableControl.getCaretPosition();
        editableControl.setValue(alpha);
        editableControl.positionCaret(caretPosition);

        super.reload();
    }
}
