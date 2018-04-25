package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.DestinationWeightInterpolationElement;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.DestinationInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing sizes in the {@link DestinationInfluencer}.
 *
 * @author JavaSaBr
 */
public class DestinationInfluencerControl extends AbstractInterpolationInfluencerControl<DestinationInfluencer> {

    public DestinationInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull DestinationInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_DESTINATION_INTERPOLATION;
    }

    @Override
    @FxThread
    protected int getMinElements() {
        return 1;
    }

    /**
     * Request to change.
     *
     * @param newValue the new value
     * @param index    the index
     */
    @FxThread
    public void requestToChange(@NotNull Vector3f newValue, int index) {

        var influencer = getInfluencer();
        var oldValue = influencer.getDestination(index);

        execute(newValue, oldValue, (destinationInfluencer, destination) ->
                destinationInfluencer.updateDestination(destination, index));
    }

    /**
     * Request to change.
     *
     * @param newValue the new value
     * @param index    the index
     */
    @FxThread
    public void requestToChange(@NotNull Float newValue, int index) {

        var influencer = getInfluencer();
        var oldValue = influencer.getWeight(index);

        execute(newValue, oldValue, (destinationInfluencer, weight) ->
                destinationInfluencer.updateWeight(weight, index));
    }

    @Override
    @FxThread
    protected void fillControl(@NotNull DestinationInfluencer influencer, @NotNull VBox root) {

        var speeds = influencer.getDestinations();

        for (int i = 0, length = speeds.size(); i < length; i++) {
            var element = new DestinationWeightInterpolationElement(this, i);
            element.prefWidthProperty().bind(widthProperty());
            FxUtils.addChild(root, element);
        }
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (influencer, needAdd) -> {
            if (needAdd) {
                influencer.addDestination(Vector3f.UNIT_Z, 1, Interpolation.LINEAR);
            } else {
                influencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var influencer = getInfluencer();
        var destinations = influencer.getDestinations();

        var destination = influencer.getDestination(destinations.size() - 1);
        var interpolation = influencer.getInterpolation(destinations.size() - 1);
        var weight = influencer.getWeight(destinations.size() - 1);

        execute(true, false, (rotationInfluencer, needRemove) -> {
            if (needRemove) {
                rotationInfluencer.removeLast();
            } else {
                rotationInfluencer.addDestination(destination, weight, interpolation);
            }
        });
    }
}
