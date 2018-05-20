package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ParticleInfluencerPropertyOperation;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.UpdatableControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.editor.ui.util.UiUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.interpolation.Interpolation;

import java.util.function.BiConsumer;

/**
 * The control for editing interpolations in the {@link ParticleInfluencer}.
 *
 * @param <I> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractInterpolationInfluencerControl<I extends InterpolatedParticleInfluencer> extends VBox
        implements UpdatableControl {

    /**
     * The consumer of changes.
     */
    @NotNull
    private final ModelChangeConsumer modelChangeConsumer;

    /**
     * The influencer.
     */
    @NotNull
    private final I influencer;

    /**
     * The parent.
     */
    @NotNull
    private final Object parent;

    /**
     * The element container.
     */
    @Nullable
    private VBox elementContainer;

    public AbstractInterpolationInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull I influencer,
            @NotNull Object parent
    ) {
        this.modelChangeConsumer = modelChangeConsumer;
        this.parent = parent;
        this.influencer = influencer;
        createControls();
        FxUtils.addClass(this,
                CssClasses.DEF_VBOX,
                CssClasses.ABSTRACT_PARAM_CONTROL_INFLUENCER);
    }

    /**
     * Create controls.
     */
    @FxThread
    protected void createControls() {

        var propertyNameLabel = new Label(getControlTitle() + ":");

        elementContainer = new VBox();

        var addButton = new Button();
        addButton.setGraphic(new ImageView(Icons.ADD_16));
        addButton.setOnAction(event -> processAdd());

        var removeButton = new Button();
        removeButton.setGraphic(new ImageView(Icons.REMOVE_12));
        removeButton.setOnAction(event -> processRemove());

        var buttonContainer = new HBox(addButton, removeButton);

        var children = elementContainer.getChildren();
        children.addListener((ListChangeListener<Node>)
                change -> removeButton.setDisable(children.size() < (getMinElements() + 1)));


        FxUtils.addClass(propertyNameLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW)
                .addClass(elementContainer, CssClasses.DEF_VBOX)
                .addClass(addButton, CssClasses.BUTTON_WITHOUT_RIGHT_BORDER)
                .addClass(removeButton, CssClasses.BUTTON_WITHOUT_LEFT_BORDER)
                .addClass(buttonContainer, CssClasses.DEF_HBOX);

        FxUtils.addChild(this, propertyNameLabel, elementContainer, buttonContainer);

        DynamicIconSupport.addSupport(addButton, removeButton);
    }

    /**
     * Get the count of minimum elements.
     *
     * @return the count of minimum elements.
     */
    @FxThread
    protected int getMinElements() {
        return 2;
    }

    /**
     * Get the control title.
     *
     * @return the control title
     */
    @FxThread
    protected @NotNull String getControlTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the influencer.
     *
     * @return the influencer.
     */
    @FxThread
    public @NotNull I getInfluencer() {
        return influencer;
    }

    /**
     * Get the element container.
     *
     * @return the element container.
     */
    @FxThread
    protected @NotNull VBox getElementContainer() {
        return notNull(elementContainer);
    }

    /**
     * Get the model change consumer.
     *
     * @return the model change consumer.
     */
    @FxThread
    protected @NotNull ModelChangeConsumer getModelChangeConsumer() {
        return modelChangeConsumer;
    }

    /**
     * Reload this control.
     */
    @FxThread
    public void reload() {

        var influencer = getInfluencer();
        var root = getElementContainer();
        var children = root.getChildren();

        if (isNeedRebuild(influencer, children.size())) {
            UiUtils.clear(root);
            fillControl(influencer, root);
        } else {
            children.stream()
                    .map(InterpolationElement.class::cast)
                    .forEach(InterpolationElement::reload);
        }
    }

    /**
     * Return true if need to rebuild this control.
     *
     * @param influencer   the influencer
     * @param currentCount the current count
     * @return true if need to rebuild this control.
     */
    @FxThread
    protected boolean isNeedRebuild(@NotNull I influencer, int currentCount) {
        return influencer.getStepCount() != currentCount;
    }

    /**
     * Fill this control.
     *
     * @param influencer the influencer.
     * @param root       the root.
     */
    @FxThread
    protected void fillControl(@NotNull I influencer, @NotNull VBox root) {
    }

    /**
     * Handle of removing a last interpolation.
     */
    @FxThread
    protected void processRemove() {
    }

    /**
     * Handle of adding a new interpolation.
     */
    @FxThread
    protected void processAdd() {
    }

    /**
     * Request to change an interpolation.
     *
     * @param newValue the new interpolation.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@Nullable Interpolation newValue, int index) {

        var influencer = getInfluencer();
        var oldValue = influencer.getInterpolation(index);

        execute(newValue, oldValue, (alphaInfluencer, interpolation) ->
                alphaInfluencer.updateInterpolation(interpolation, index));
    }

    /**
     * Execute the operation.
     *
     * @param <T>          the type of value.
     * @param newValue     the new value.
     * @param oldValue     the old value.
     * @param applyHandler the apply handler.
     */
    @FxThread
    protected <T> void execute(@Nullable T newValue, @Nullable T oldValue, @NotNull BiConsumer<I, T> applyHandler) {

        var operation = new ParticleInfluencerPropertyOperation<I, T>(influencer, parent,
                getPropertyName(), newValue, oldValue);

        operation.setApplyHandler(applyHandler);

        getModelChangeConsumer().execute(operation);
    }

    /**
     * Get the property name.
     *
     * @return the property name
     */
    protected @NotNull String getPropertyName() {
        return getControlTitle();
    }

    @Override
    @FxThread
    public void sync() {
        reload();
    }
}
