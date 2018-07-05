package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.jme3.math.ColorRGBA;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.ColorInterpolationElement;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.ColorInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing colors in the {@link ColorInfluencer}.
 *
 * @author JavaSaBr
 */
public class ColorInfluencerControl extends AbstractInterpolationInfluencerControl<ColorInfluencer> {

    public ColorInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull ColorInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_COLOR_INTERPOLATION;
    }

    @Override
    @FxThread
    protected @NotNull InterpolationElement<?, ?, ?> createElement(int i) {
        return new ColorInterpolationElement(this, i);
    }

    /**
     * Request to change.
     *
     * @param newValue the new value.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@NotNull ColorRGBA newValue, int index) {

        var oldValue = influencer.getColor(index);

        execute(newValue, oldValue, (colorInfluencer, colorRGBA) ->
                colorInfluencer.updateColor(colorRGBA, index));
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (colorInfluencer, needAdd) -> {
            if (needAdd) {
                colorInfluencer.addColor(new ColorRGBA(ColorRGBA.White), Interpolation.LINEAR);
            } else {
                colorInfluencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var colors = influencer.getColors();

        var color = influencer.getColor(colors.size() - 1);
        var interpolation = influencer.getInterpolation(colors.size() - 1);

        execute(true, false, (colorInfluencer, needRemove) -> {
            if (needRemove) {
                colorInfluencer.removeLast();
            } else {
                colorInfluencer.addColor(color, interpolation);
            }
        });
    }
}
