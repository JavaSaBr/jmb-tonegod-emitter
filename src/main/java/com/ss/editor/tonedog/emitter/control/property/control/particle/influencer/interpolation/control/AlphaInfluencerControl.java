package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.AlphaInterpolationElement;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.AlphaInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing alphas in the {@link AlphaInfluencer}.
 *
 * @author JavaSaBr
 */
public class AlphaInfluencerControl extends AbstractInterpolationInfluencerControl<AlphaInfluencer> {

    public AlphaInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull AlphaInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_ALPHA_INTERPOLATION;
    }

    /**
     * Request to change.
     *
     * @param newValue the new value.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@NotNull Float newValue, int index) {
        var oldValue = influencer.getAlpha(index);
        execute(newValue, oldValue, (alphaInfluencer, alpha) ->
                alphaInfluencer.updateAlpha(alpha, index));
    }

    @Override
    @FxThread
    protected @NotNull InterpolationElement<?, ?, ?> createElement(int i) {
        return new AlphaInterpolationElement(this, i);
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (alphaInfluencer, needAdd) -> {
            if (needAdd) {
                alphaInfluencer.addAlpha(1F, Interpolation.LINEAR);
            } else {
                alphaInfluencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var alphas = influencer.getAlphas();

        var alpha = influencer.getAlpha(alphas.size() - 1);
        var interpolation = influencer.getInterpolation(alphas.size() - 1);

        execute(true, false, (alphaInfluencer, needRemove) -> {
            if (needRemove) {
                alphaInfluencer.removeLast();
            } else {
                alphaInfluencer.addAlpha(alpha, interpolation);
            }
        });
    }
}
