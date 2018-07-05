package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.SizeInterpolationElement;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.SizeInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing sizes in the {@link SizeInfluencer}.
 *
 * @author JavaSaBr
 */
public class SizeInfluencerControl extends AbstractInterpolationInfluencerControl<SizeInfluencer> {

    public SizeInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull SizeInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_SIZE_INTERPOLATION;
    }

    /**
     * Request to change.
     *
     * @param newValue the new value.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@NotNull Vector3f newValue, int index) {

        var oldValue = influencer.getSize(index);

        execute(newValue, oldValue, (sizeInfluencer, alpha) ->
                sizeInfluencer.updateSize(alpha, index));
    }

    @Override
    @FxThread
    protected @NotNull InterpolationElement<?, ?, ?> createElement(int i) {
        return new SizeInterpolationElement(this, i);
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (influencer, needAdd) -> {
            if (needAdd) {
                influencer.addSize(1F, Interpolation.LINEAR);
            } else {
                influencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var sizes = influencer.getSizes();

        var size = influencer.getSize(sizes.size() - 1);
        var interpolation = influencer.getInterpolation(sizes.size() - 1);

        execute(true, false, (sizeInfluencer, needRemove) -> {
            if (needRemove) {
                sizeInfluencer.removeLast();
            } else {
                sizeInfluencer.addSize(size, interpolation);
            }
        });
    }
}
