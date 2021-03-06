/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.client.management;

import static org.eclipse.ditto.model.base.common.ConditionChecker.argumentNotNull;

import java.util.concurrent.CompletableFuture;

import org.eclipse.ditto.client.options.Option;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.JsonValue;

/**
 * {@code ThingAttributeManagement} provides all functionality required for managing {@link
 * org.eclipse.ditto.model.things.Thing} attributes.
 * <p>
 * Note: All methods returning a {@link CompletableFuture} are executed non-blocking and asynchronously. Therefore,
 * these methods return a {@code CompletableFuture} object that will complete either successfully if the operation was
 * executed and confirmed, or exceptionally with a specific {@link org.eclipse.ditto.model.base.exceptions.DittoRuntimeException}
 * if it was executed but has failed.
 *
 * @since 1.0.0
 */
public interface ThingAttributeManagement {

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final boolean value,
            final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, boolean value, Option<?>... options);

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final double value,
            final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, double value, Option<?>... options);

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final int value, final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, int value, Option<?>... options);

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final long value,
            final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, long value, Option<?>... options);

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final String value,
            final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, String value, Option<?>... options);

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the path to the attribute value to be set - may contain {@code "/"} for addressing nested paths in a
     * hierarchy.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> putAttribute(final CharSequence path, final JsonValue value,
            final Option<?>... options) {
        argumentNotNull(path);
        return putAttribute(JsonFactory.newPointer(path), value, options);
    }

    /**
     * Sets the given attribute to the Thing.
     *
     * @param path the hierarchical path to the attribute value to be set.
     * @param value the attribute value to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if any argument is {@code null} or if {@code path} is empty.
     */
    CompletableFuture<Void> putAttribute(JsonPointer path, JsonValue value, Option<?>... options);

    /**
     * Sets the given attributes to this Thing.
     *
     * @param value the attributes to be set.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    CompletableFuture<Void> setAttributes(JsonObject value, Option<?>... options);

    /**
     * Deletes the attribute specified by the given path.
     *
     * @param path the path to the attribute to be created/modified within the attributes using {@code "/"} as
     * separator, e. g. {@code "address/city"}.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    default CompletableFuture<Void> deleteAttribute(final CharSequence path, final Option<?>... options) {
        argumentNotNull(path);
        return deleteAttribute(JsonFactory.newPointer(path), options);
    }

    /**
     * Deletes the attribute specified by the given path.
     *
     * @param path the path to the attribute to be created/modified within the attributes using {@code "/"} as
     * separator, e. g. {@code "address/city"}.
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     * @throws IllegalArgumentException if {@code path} is {@code null} or empty.
     */
    CompletableFuture<Void> deleteAttribute(JsonPointer path, Option<?>... options);

    /**
     * Deletes all attributes of this thing.
     *
     * @param options options to be applied configuring behaviour of this method, see {@link
     * org.eclipse.ditto.client.options.Options}.
     * @return a completable future providing the result of this operation or a specific {@link
     * org.eclipse.ditto.model.base.exceptions.DittoRuntimeException} if the operation failed
     */
    CompletableFuture<Void> deleteAttributes(Option<?>... options);
}
