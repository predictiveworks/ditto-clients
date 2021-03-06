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
package org.eclipse.ditto.client;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.eclipse.ditto.client.TestConstants.Policy.POLICY;
import static org.eclipse.ditto.client.TestConstants.Policy.POLICY_ID;
import static org.eclipse.ditto.client.TestConstants.Policy.POLICY_JSON_OBJECT;
import static org.eclipse.ditto.client.TestConstants.Thing.THING_ID;
import static org.eclipse.ditto.client.TestConstants.Thing.THING_ID_COPY_POLICY;
import static org.eclipse.ditto.client.TestConstants.Thing.THING_WITH_INLINE_POLICY;
import static org.eclipse.ditto.client.assertions.ClientAssertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.assertj.core.api.Assertions;
import org.eclipse.ditto.client.internal.AbstractDittoClientTest;
import org.eclipse.ditto.client.options.Option;
import org.eclipse.ditto.client.options.Options;
import org.eclipse.ditto.client.registration.DuplicateRegistrationIdException;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.model.base.auth.AuthorizationModelFactory;
import org.eclipse.ditto.model.base.auth.AuthorizationSubject;
import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.messages.Message;
import org.eclipse.ditto.model.messages.MessageDirection;
import org.eclipse.ditto.model.messages.MessageHeaders;
import org.eclipse.ditto.model.messages.MessagesModelFactory;
import org.eclipse.ditto.model.policies.Policy;
import org.eclipse.ditto.model.policies.PolicyId;
import org.eclipse.ditto.model.things.Feature;
import org.eclipse.ditto.model.things.Thing;
import org.eclipse.ditto.model.things.ThingId;
import org.eclipse.ditto.model.things.ThingsModelFactory;
import org.eclipse.ditto.signals.commands.things.modify.DeleteThing;
import org.eclipse.ditto.signals.commands.things.modify.ModifyPolicyId;
import org.eclipse.ditto.signals.commands.things.modify.ModifyPolicyIdResponse;
import org.eclipse.ditto.signals.events.things.ThingCreated;
import org.eclipse.ditto.signals.events.things.ThingDeleted;
import org.eclipse.ditto.signals.events.things.ThingEvent;
import org.junit.Test;

/**
 * Test top-level (i.e. Thing) operations of the {@link DittoClient}.
 */
public final class DittoClientThingTest extends AbstractDittoClientTest {

    private static final String FEATURE_ID = "someFeature";
    private static final JsonPointer ATTRIBUTE_KEY_NEW = JsonFactory.newPointer("new");
    private static final String ATTRIBUTE_VALUE = "value";
    private static final Feature FEATURE = ThingsModelFactory.newFeatureBuilder()
            .properties(ThingsModelFactory.newFeaturePropertiesBuilder()
                    .set("propertyPointer", "propertyValue")
                    .build())
            .withId(FEATURE_ID)
            .build();

    private static final Thing THING = ThingsModelFactory.newThingBuilder()
            .setId(THING_ID)
            .setPolicyId(POLICY_ID)
            .setAttribute(ATTRIBUTE_KEY_NEW, JsonFactory.newValue(ATTRIBUTE_VALUE))
            .setFeature(FEATURE)
            .build();

    @Test
    public void testCreateThing() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().create(THING_ID);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void createThingFailsWithExistsOption() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().create(THING_ID, Options.Modify.exists(false)).get(TIMEOUT, TIME_UNIT));
    }

    @Test
    public void testPutThingWithoutExistsOption() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().put(THING);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithExistsOptionFalse() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasOnlyIfNoneMatchHeader();

            latch.countDown();
        });

        client.twin().put(THING, Options.Modify.exists(false));

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithExistsOptionTrue() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasOnlyIfMatchHeader();

            latch.countDown();
        });

        client.twin().put(THING, Options.Modify.exists(true));

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testUpdateThing() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasOnlyIfMatchHeader();

            latch.countDown();
        });

        client.twin().update(THING);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void updateThingFailsWithExistsOption() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().update(THING, Options.Modify.exists(false)).get(TIMEOUT, TIME_UNIT));
    }

    @Test
    public void testDeleteThing() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasSubject(DeleteThing.TYPE);

            latch.countDown();
        });

        client.twin()
                .forId(THING_ID)
                .delete();

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void deleteThingFailsWithExistsOption() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().delete(THING_ID, Options.Modify.exists(false)).get(TIMEOUT, TIME_UNIT));
    }

    @Test
    public void testReceiveCreatedEvent() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        client.twin().startConsumption();
        client.twin().registerForThingChanges("test", change -> {
            assertThat(change)
                    .hasThingId(THING_ID)
                    .isAdded();

            latch.countDown();
        });

        final MessageHeaders messageHeaders =
                MessageHeaders.newBuilder(MessageDirection.FROM, THING_ID, ThingCreated.TYPE).build();

        final Message<ThingEvent> thingCreated = MessagesModelFactory.<ThingEvent>newMessageBuilder(messageHeaders)
                .payload(ThingCreated.of(Thing.newBuilder().setId(THING_ID).build(), 1,
                        DittoHeaders.empty()))
                .build();

        messaging.receiveEvent(thingCreated);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testReceiveDeletedEvent() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        client.twin().startConsumption();
        client.twin().registerForThingChanges("test", change -> {
            assertThat(change)
                    .hasThingId(THING_ID)
                    .isDeleted();

            latch.countDown();
        });

        final Message<ThingEvent> thingDeleted = createThingDeletedMessage();
        messaging.receiveEvent(thingDeleted);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    private static Message<ThingEvent> createThingDeletedMessage() {
        final MessageHeaders messageHeaders =
                MessageHeaders.newBuilder(MessageDirection.FROM, THING_ID, ThingDeleted.TYPE).build();

        return MessagesModelFactory.<ThingEvent>newMessageBuilder(messageHeaders)
                .payload(ThingDeleted.of(THING_ID, 1, DittoHeaders.empty()))
                .build();
    }

    @Test
    public void testSetPolicyId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasSubject(ModifyPolicyId.TYPE)
                    .matches(message -> {
                        final ModifyPolicyId command = (ModifyPolicyId) message.getPayload().get();
                        return POLICY_ID.equals(command.getPolicyEntityId());
                    });

            messaging.receiveResponse(ModifyPolicyIdResponse.created(THING_ID, POLICY_ID, m.getHeaders()));
            latch.countDown();
        });

        final CompletableFuture<Void> response = client.twin()
                .forId(THING_ID)
                .setPolicyId(POLICY_ID);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
        Assertions.assertThat(response).isCompleted();
    }

    @Test
    public void testRegisterTwoHandlersWithSameSelector() throws Exception {
        final CountDownLatch latch = new CountDownLatch(2);

        client.twin().startConsumption();
        client.twin().registerForThingChanges("test", change -> latch.countDown());
        client.twin().registerForThingChanges("test2", change -> latch.countDown());

        final Message<ThingEvent> thingDeleted = createThingDeletedMessage();
        messaging.receiveEvent(thingDeleted);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test(expected = DuplicateRegistrationIdException.class)
    public void testRegisterTwoHandlersWithSameRegistrationId() {
        client.twin().registerForThingChanges("test", change -> {
        });
        client.twin().registerForThingChanges("test", change -> {
        });
    }

    @Test
    public void testDeregisterEventHandler() throws Exception {
        // prepare
        final Semaphore sem = new Semaphore(0);
        final String registrationId = "test";

        client.twin().startConsumption();
        client.twin().registerForThingChanges(registrationId, change -> sem.release());

        final Message<ThingEvent> thingDeleted = createThingDeletedMessage();
        messaging.receiveEvent(thingDeleted);
        Assertions.assertThat(sem.tryAcquire(1, TIMEOUT, TIME_UNIT)).isTrue();

        // test
        final boolean unregistered = client.twin().deregister(registrationId);
        Assertions.assertThat(unregistered).isTrue();

        messaging.receiveEvent(thingDeleted);

        // verify: handler must not have been called
        Assertions.assertThat(sem.tryAcquire(1, TIMEOUT, TIME_UNIT)).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThingWithMissingId() {
        client.twin().create(JsonFactory.newObject());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateThingWithMissingId() {
        client.twin().update(JsonFactory.newObject());
    }

    @Test
    public void testCreateThingWithoutFeatures() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final ThingId thingIdWithoutFeatures = ThingId.of("demo:mything1");

        messaging.onSend(m -> {
            assertThat(m).hasThingId(thingIdWithoutFeatures);

            latch.countDown();
        });

        final AuthorizationSubject authorizationSubject = AuthorizationModelFactory.newAuthSubject("someSubject");
        final Thing thing = ThingsModelFactory.newThingBuilder()
                .setId(thingIdWithoutFeatures)
                .setPermissions(authorizationSubject, ThingsModelFactory.allPermissions())
                .build();

        client.twin().create(thing);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithInlinePolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().create(THING_WITH_INLINE_POLICY);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithInitialPolicyJson() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().create(THING_ID, POLICY_JSON_OBJECT);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithInitialPolicyJsonNullable() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().create(THING_ID, (JsonObject) null));
    }

    @Test
    public void testCreateThingWithInitialPolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().create(THING_ID, POLICY);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithInitialPolicyNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().create(THING_ID, (Policy) null));
    }

    @Test
    public void testPutThingWithInlinePolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().put(THING_WITH_INLINE_POLICY);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithInitialPolicyJson() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().put(THING, POLICY_JSON_OBJECT);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithInitialPolicyJsonNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().put(THING, (JsonObject) null));
    }

    @Test
    public void testPutThingWithInitialPolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasInitialPolicy()
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        client.twin().put(THING, POLICY);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithInitialPolicyNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().put(THING, (Policy) null));
    }

    @Test
    public void testCreateThingWithOptionCopyPolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID_COPY_POLICY)
                    .hasOptionCopyPolicy(POLICY_ID)
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);
        client.twin().create(THING_ID_COPY_POLICY, copyPolicy);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithOptionCopyPolicyFromThing() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID_COPY_POLICY)
                    .hasOptionCopyPolicyFromThing(THING_ID)
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        final Option<ThingId> copyPolicy = Options.Modify.copyPolicyFromThing(THING_ID);
        client.twin().create(THING_ID_COPY_POLICY, copyPolicy);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testCreateThingWithJsonInlinePolicyAndOptionCopyPolicy() {
        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().create(THING_ID_COPY_POLICY, POLICY_JSON_OBJECT, copyPolicy));
    }

    @Test
    public void testCreateThingWithAllOptionCopyPolicy() {
        final Option<ThingId> copyPolicyFromThing = Options.Modify.copyPolicyFromThing(THING_ID);
        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().create(THING_ID_COPY_POLICY, copyPolicy, copyPolicyFromThing));
    }

    @Test
    public void testPutThingWithOptionCopyPolicy() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        messaging.onSend(m -> {
            assertThat(m)
                    .hasThingId(THING_ID)
                    .hasOptionCopyPolicy(POLICY_ID)
                    .hasNoConditionalHeaders();

            latch.countDown();
        });

        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);
        client.twin().put(THING, copyPolicy);

        Assertions.assertThat(latch.await(TIMEOUT, TIME_UNIT)).isTrue();
    }

    @Test
    public void testPutThingWithJsonInlinePolicyAndOptionCopyPolicy() {
        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().put(THING, POLICY_JSON_OBJECT, copyPolicy));
    }

    @Test
    public void testPutThingWithAllOptionCopyPolicy() {
        final Option<ThingId> copyPolicyFromThing = Options.Modify.copyPolicyFromThing(THING_ID);
        final Option<PolicyId> copyPolicy = Options.Modify.copyPolicy(POLICY_ID);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.twin().put(THING, copyPolicy, copyPolicyFromThing));
    }

}
