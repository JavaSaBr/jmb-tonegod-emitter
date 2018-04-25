package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.RotationInterpolationElement;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.RotationInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing sizes in the {@link RotationInfluencer}.
 *
 * @author JavaSaBr
 */
public class RotationInfluencerControl extends AbstractInterpolationInfluencerControl<RotationInfluencer> {

    public RotationInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull RotationInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_ROTATION_INTERPOLATION;
    }

    @Override
    @FxThread
    protected int getMinElements() {
        return 1;
    }

    /**
     * Request to change.
     *
     * @param newValue the new value.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@NotNull Vector3f newValue, int index) {

        var influencer = getInfluencer();
        var oldValue = influencer.getRotationSpeed(index);

        execute(newValue, oldValue, (rotationInfluencer, alpha) ->
                rotationInfluencer.updateRotationSpeed(alpha, index));
    }

    @Override
    @FxThread
    protected void fillControl(@NotNull RotationInfluencer influencer, @NotNull VBox root) {

        var speeds = influencer.getRotationSpeeds();

        for (int i = 0, length = speeds.size(); i < length; i++) {
            var element = new RotationInterpolationElement(this, i);
            element.prefWidthProperty().bind(widthProperty());
            FxUtils.addChild(root, element);
        }
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (influencer, needAdd) -> {
            if (needAdd) {
                influencer.addRotationSpeed(Vector3f.UNIT_Z, Interpolation.LINEAR);
            } else {
                influencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var influencer = getInfluencer();
        var speeds = influencer.getRotationSpeeds();

        var speed = influencer.getRotationSpeed(speeds.size() - 1);
        var interpolation = influencer.getInterpolation(speeds.size() - 1);

        execute(true, false, (rotationInfluencer, needRemove) -> {
            if (needRemove) {
                rotationInfluencer.removeLast();
            } else {
                rotationInfluencer.addRotationSpeed(speed, interpolation);
            }
        });
    }
}
