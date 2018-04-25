package com.ss.editor.tonedog.emitter.model;

import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;

import java.util.List;

/**
 * The container of particle influencers.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencers {

    /**
     * The emitter node.
     */
    @NotNull
    private final ParticleEmitterNode emitterNode;

    public ParticleInfluencers(@NotNull ParticleEmitterNode emitterNode) {
        this.emitterNode = emitterNode;
    }

    /**
     * Get the emitter node.
     *
     * @return the emitter node.
     */
    public @NotNull ParticleEmitterNode getEmitterNode() {
        return emitterNode;
    }

    /**
     * Get the list of influencers.
     *
     * @return the list of influencers.
     */
    public @NotNull List<ParticleInfluencer<?>> getInfluencers() {
        return emitterNode.getInfluencers();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ParticleInfluencers that = (ParticleInfluencers) o;
        return emitterNode.equals(that.emitterNode);
    }

    @Override
    public int hashCode() {
        return emitterNode.hashCode();
    }

    @Override
    public String toString() {
        return "ParticleInfluencers{" +
                "emitterNode=" + emitterNode +
                '}';
    }
}
