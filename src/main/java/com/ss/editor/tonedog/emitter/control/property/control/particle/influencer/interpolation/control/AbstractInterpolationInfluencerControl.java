package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import com.ss.editor.tonedog.emitter.control.operation.ParticleInfluencerPropertyOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.UpdatableControl;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.editor.ui.util.UIUtils;
import com.ss.rlib.ui.util.FXUtils;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

    public AbstractInterpolationInfluencerControl(@NotNull final ModelChangeConsumer modelChangeConsumer,
                                                  @NotNull final I influencer,
                                                  @NotNull final Object parent) {
        this.modelChangeConsumer = modelChangeConsumer;
        this.parent = parent;
        this.influencer = influencer;
        createControls();
        FXUtils.addClassesTo(this, CSSClasses.DEF_VBOX, CSSClasses.ABSTRACT_PARAM_CONTROL_INFLUENCER);
    }

    /**
     * Create controls.
     */
    @FxThread
    protected void createControls() {

        final Label propertyNameLabel = new Label(getControlTitle() + ":");

        elementContainer = new VBox();

        final Button addButton = new Button();
        addButton.setGraphic(new ImageView(Icons.ADD_16));
        addButton.setOnAction(event -> processAdd());

        final Button removeButton = new Button();
        removeButton.setGraphic(new ImageView(Icons.REMOVE_12));
        removeButton.setOnAction(event -> processRemove());

        final HBox buttonContainer = new HBox(addButton, removeButton);

        final ObservableList<Node> children = elementContainer.getChildren();
        children.addListener((ListChangeListener<Node>) c -> removeButton.setDisable(children.size() < (getMinElements() + 1)));

        FXUtils.addToPane(propertyNameLabel, this);
        FXUtils.addToPane(elementContainer, this);
        FXUtils.addToPane(buttonContainer, this);

        FXUtils.addClassTo(propertyNameLabel, CSSClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        FXUtils.addClassTo(elementContainer, CSSClasses.DEF_VBOX);
        FXUtils.addClassTo(addButton, CSSClasses.BUTTON_WITHOUT_RIGHT_BORDER);
        FXUtils.addClassTo(removeButton, CSSClasses.BUTTON_WITHOUT_LEFT_BORDER);
        FXUtils.addClassTo(buttonContainer, CSSClasses.DEF_HBOX);

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
     * Gets control title.
     *
     * @return the control title
     */
    @FxThread
    protected @NotNull String getControlTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets influencer.
     *
     * @return the influencer.
     */
    @FxThread
    public @NotNull I getInfluencer() {
        return influencer;
    }

    /**
     * Gets element container.
     *
     * @return the element container.
     */
    @FxThread
    protected @NotNull VBox getElementContainer() {
        return notNull(elementContainer);
    }

    /**
     * Gets model change consumer.
     *
     * @return the consumer of changes.
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

        final I influencer = getInfluencer();
        final VBox root = getElementContainer();
        final ObservableList<Node> children = root.getChildren();

        if (isNeedRebuild(influencer, children.size())) {
            UIUtils.clear(root);
            fillControl(influencer, root);
        } else {
            children.stream()
                    .map(InterpolationElement.class::cast)
                    .forEach(InterpolationElement::reload);
        }
    }

    /**
     * Is need rebuild boolean.
     *
     * @param influencer   the influencer
     * @param currentCount the current count
     * @return the boolean
     */
    @FxThread
    protected boolean isNeedRebuild(@NotNull final I influencer, final int currentCount) {
        return influencer.getStepCount() != currentCount;
    }

    /**
     * Fill this control.
     *
     * @param influencer the influencer
     * @param root       the root
     */
    @FxThread
    protected void fillControl(@NotNull final I influencer, @NotNull final VBox root) {
    }

    /**
     * Handle removing last interpolation.
     */
    @FxThread
    protected void processRemove() {
    }

    /**
     * Handle adding new interpolation.
     */
    @FxThread
    protected void processAdd() {
    }

    /**
     * Request to change interpolation.
     *
     * @param newValue the new interpolation.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@Nullable final Interpolation newValue, final int index) {

        final I influencer = getInfluencer();
        final Interpolation oldValue = influencer.getInterpolation(index);

        execute(newValue, oldValue, (alphaInfluencer, interpolation) -> alphaInfluencer.updateInterpolation(interpolation, index));
    }

    /**
     * Execute change operation.
     *
     * @param <T>          the type of value.
     * @param newValue     the new value.
     * @param oldValue     the old value.
     * @param applyHandler the apply handler.
     */
    @FxThread
    protected <T> void execute(@Nullable final T newValue, @Nullable final T oldValue, @NotNull final BiConsumer<I, T> applyHandler) {

        final ParticleInfluencerPropertyOperation<I, T> operation = new ParticleInfluencerPropertyOperation<>(influencer, parent, getPropertyName(), newValue, oldValue);
        operation.setApplyHandler(applyHandler);

        final ModelChangeConsumer modelChangeConsumer = getModelChangeConsumer();
        modelChangeConsumer.execute(operation);
    }

    /**
     * Gets property name.
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
